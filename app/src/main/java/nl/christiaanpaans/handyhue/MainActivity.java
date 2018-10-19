package nl.christiaanpaans.handyhue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RoomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new RoomAdapter(this);
        RecyclerView roomRecycler = findViewById(R.id.roomRecycler);
        roomRecycler.setAdapter(adapter);
        roomRecycler.setLayoutManager(new LinearLayoutManager(this));

        final FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Room> rooms = RoomFactory.getInstance().getRooms();
                Room myRoom = new Room("newdeveloper", "Kamer" + " " +
                        (rooms.size() + 1),"192.168.178.52", 8000 + rooms.size());
                rooms.add(myRoom);
                adapter.notifyItemInserted(RoomFactory.getInstance().getRooms().size());
            }
        });
    }
}
