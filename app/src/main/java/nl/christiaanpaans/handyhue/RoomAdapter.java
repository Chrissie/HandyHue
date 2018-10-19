package nl.christiaanpaans.handyhue;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    private Context context;

    public RoomAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View roomView = inflater.inflate(R.layout.recycler_room_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(roomView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.ViewHolder holder, final int position) {
        final Room room = RoomFactory.getInstance().getRooms().get(position);

        holder.roomNameView.setText(room.getName());
        holder.roomAddressView.setText(room.getIp() + ":" + room.getPort());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RoomActivity.class);
                intent.putExtra("ROOM_INDEX", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return RoomFactory.getInstance().getRooms().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView roomNameView;
        private TextView roomAddressView;
        private Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.roomNameView = itemView.findViewById(R.id.roomName);
            this.roomAddressView = itemView.findViewById(R.id.roomAdress);
            this.button = itemView.findViewById(R.id.selectRoomButton);
        }
    }
}
