package nl.christiaanpaans.handyhue;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class LightsRestController {
    public interface LightsRestListener {
        void onUpdatedLamps();
    }

    public static void getRoom(final LightsRestListener listener, Context context, final Room room) {
        String url = "http://" + room.getIp() + ":" + room.getPort()
        +"/api/"
        + room.getUserName()
        + "/lights/";

        Log.d("GET_STRING", url);
        Volley.newRequestQueue(context).add(new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray lampsArray = response.names();

                            ArrayList<String> unusedKeys = new ArrayList<>();
                            for (int i = 0; i < lampsArray.length (); ++i) {
                                String key = lampsArray.getString(i); // Here's your key
                                unusedKeys.add(key);

                                for (Pair<String, Room.Lamp> lampPair : room.getLamps()) {
                                    if (lampPair.first.equals(key)) {
                                        try {
                                            unusedKeys.remove(key);
                                        } catch (Exception e) {
                                            Log.e("JSON_PARSE", e.toString());
                                        }
                                    }
                                }
                            }

                            for (String key: unusedKeys) {
                                try {
                                    room.addLamp(key,key);
                                } catch (Exception e) {
                                    Log.e("JSON_PARSE", e.toString());
                                }
                            }

                            updateReceivedLamps(response, room);
                            listener.onUpdatedLamps();
                        } catch (Exception e) {
                            Log.e("VOLLEY_RESPONSE", "Volley Error");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                }
            })
        );
    }

    private static void updateReceivedLamps(JSONObject response, Room room) {
        JSONArray lampsArray = response.names();
        for (int i = 0; i < lampsArray.length (); ++i) {
            try {
                String key = lampsArray.getString(i);
                Room.Lamp selectedLamp;
                for (Pair<String, Room.Lamp> pair : room.getLamps()) {
                    if (pair.first.equals(key)) {
                        selectedLamp = pair.second;

                        JSONObject value = response.getJSONObject(key); // Here's your value
                        selectedLamp.setName(value.getString("name"));

                        JSONObject state = value.getJSONObject("state");
                        if (state.getBoolean("on") != selectedLamp.isOn()) {
                            selectedLamp.setOn(state.getBoolean("on"));
                        }

                        int hue = state.getInt("hue");
                        int sat = state.getInt("sat");
                        int bri = state.getInt("bri");
                        int[] hsv = { hue, sat, bri };
                        selectedLamp.setHsv(hsv);
                    }
                }
            } catch (Exception e) {
                Log.e("JSON_EXCEPTION", e.toString());
            }
        }
    }

    public static void putLamp(Context context, Room room, Pair<String, Room.Lamp> pair) {
        String url = "http://" + room.getIp() + ":" + room.getPort()
                +"/api/"
                + room.getUserName()
                + "/lights/"
                + pair.first
                + "/state";

        Room.Lamp myLamp = pair.second;
        JSONObject putBody = new JSONObject();
        try {
            putBody.put("on", myLamp.isOn());
            putBody.put("hue", myLamp.getHsv()[0]);
            putBody.put("sat", myLamp.getHsv()[1]);
            putBody.put("bri", myLamp.getHsv()[2]);
        } catch (Exception e) {
            Log.e("JSON_EXCEPTION", e.toString());
        }

        Volley.newRequestQueue(context).add(new JsonObjectRequest(Request.Method.PUT, url, putBody,
           new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                   Log.d("VOLLEY_RESPONSE", response.toString());
                }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   Log.e("VOLLEY_RESPONSE", "Volley Error");
               }
           })
        );
    }
}

