package nl.christiaanpaans.handyhue;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.rtugeek.android.colorseekbar.ColorSeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LampAdapter extends RecyclerView.Adapter<LampAdapter.ViewHolder> {

    private Room room;
    private Context context;

    public LampAdapter(Context context, Room room) {
        this.context = context;
        this.room = room;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View lampView = inflater.inflate(R.layout.recycler_lamp_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(lampView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Pair<String, Room.Lamp> pair = room.getLamps().get(position);
        final Room.Lamp lamp = pair.second;


        holder.lampIdView.setText(pair.second.getName());
        holder.onOffSwitch.setChecked(lamp.isOn());
        holder.onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lamp.setOn(isChecked);
//                Maybe show brightness bar?
                LightsRestController.putLamp(context, room, pair);
            }
        });

        holder.colorSlider.setOnInitDoneListener(new ColorSeekBar.OnInitDoneListener() {
            @Override
            public void done() {
                float[] wantedColor = { 0, 0, 0};
                wantedColor[0] = (float) lamp.getHsv()[0]/(65535/360f);
                wantedColor[1] = (float) lamp.getHsv()[1]/254f;
                wantedColor[2] = (float) lamp.getHsv()[2]/254f;
                holder.colorSlider.setColor(Color.HSVToColor(wantedColor));
                final float[] convertedColor = { 0, 0, 0 };
                holder.colorSlider.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    @Override
                    public void onColorChangeListener(int colorBarPos, int alphaBarPos, int color) {
                        Color.colorToHSV(color, convertedColor);
                        int hue = (int) (convertedColor[0] * 65535/360);
                        int sat = (int) (convertedColor[1] * 254);
                        int bri = (int) (convertedColor[2] * 254);

                        lamp.setHsv(new int[] {hue, sat, bri});
                        LightsRestController.putLamp(context, room, pair);
                    }
                });
            }
        });



    }

    @Override
    public int getItemCount() {
        if(room != null) {
            return room.getLamps().size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView lampIdView;
        private ColorSeekBar colorSlider;
        private Switch onOffSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.lampIdView = itemView.findViewById(R.id.lampIdView);
            this.colorSlider = itemView.findViewById(R.id.colorSlider);
            this.onOffSwitch = itemView.findViewById(R.id.onOffSwitch);

        }
    }
}
