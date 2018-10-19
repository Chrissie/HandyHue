package nl.christiaanpaans.handyhue;

import java.util.ArrayList;

public class RoomFactory {
    private static final RoomFactory ourInstance = new RoomFactory();

    public static RoomFactory getInstance() {
        return ourInstance;
    }
    private ArrayList<Room> rooms;

    private RoomFactory() {
        rooms = new ArrayList<Room>();
//        rooms.add(new Room("newdeveloper", "Kamer" + " " +
//                (rooms.size() + 1),"192.168.178.52", 8000));
//        rooms.add(new Room("newdeveloper", "Kamer" + " " +
//                (rooms.size() + 1),"192.168.178.52", 8001));
        rooms.add(new Room("0h3Gr4yO6l1R1-WRt5YgqzBeux4JYA6ViOMYyKHC", "LA-134",
                "192.168.1.179", 80));
        rooms.add(new Room("iYrmsQq1wu5FxF9CPqpJCnm1GpPVylKBWDUsNDhB","LA-0",
                "145.48.205.33", 80));
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }
}
