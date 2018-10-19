package nl.christiaanpaans.handyhue;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.rtugeek.android.colorseekbar.ColorSeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RoomActivity extends AppCompatActivity implements LightsRestController.LightsRestListener {

    private Room room = new Room("","","",0);
    private LampAdapter adapter;
    private Switch masterSwitch;
    private ColorSeekBar masterColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        int index = intent.getIntExtra("ROOM_INDEX", -1);
        try {
            if (index != -1) {
                room = RoomFactory.getInstance().getRooms().get(index);
                adapter = new LampAdapter(this, room);
                LightsRestController.getRoom(this, getApplicationContext(), room);
            }
        } catch (Error e) {
            Log.e("INDEX_ERROR", "Can't get this index from the Rooms ArrayList");
        }

        RecyclerView lampsRecycler = findViewById(R.id.lampsRecycler);
        lampsRecycler.setAdapter(adapter);
        lampsRecycler.setLayoutManager(new LinearLayoutManager(this));

        masterSwitch = findViewById(R.id.onOffSwitch);
        masterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (Pair<String, Room.Lamp> lampPair: room.getLamps()) {
                    lampPair.second.setOn(isChecked);
                    LightsRestController.putLamp(getApplicationContext(), room, lampPair);
                }
            }
        });

        masterColor = findViewById(R.id.colorSlider);
        masterColor.setOnInitDoneListener(new ColorSeekBar.OnInitDoneListener() {
            @Override
            public void done() {
                masterColor.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    @Override
                    public void onColorChangeListener(int colorBarPos, int alphaBarPos, int color) {
                        float[] convertedColor = { 0,0,0 };
                        Color.colorToHSV(color, convertedColor);
                        int hue = (int) (convertedColor[0] * 65535/360);
                        int sat = (int) (convertedColor[1] * 254);
                        int bri = (int) (convertedColor[2] * 254);

                        for (Pair<String, Room.Lamp> lampPair: room.getLamps()) {
                            lampPair.second.setHsv(new int[] {hue, sat, bri});
                            LightsRestController.putLamp(getApplicationContext(), room, lampPair);
                        }
                    }
                });
            }
        });


        if(room != null) {
            getSupportActionBar().setTitle(room.getName());
        };
    }

    @Override
    public void onUpdatedLamps() {
        adapter.notifyDataSetChanged();
    }
}
