package nl.christiaanpaans.handyhue;

import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class Room {

    public class Lamp {

        private String name;
        private int hsv[] = { 0, 0, 0 };
        private boolean isOn;

        public Lamp(String name) {
            this.name = name;
        }

        public void setLamp(int[] hsv, boolean isOn)
        {
            this.hsv = hsv;
            this.isOn = isOn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int[] getHsv() {
            return hsv;
        }

        public void setHsv(int[] hsv) {
            this.hsv = hsv;
        }

        public boolean isOn() {
            return isOn;
        }

        public void setOn(boolean on) {
            isOn = on;
        }
    }

    private String userName;
    private String name;
    private String ip;
    private int port;
    private ArrayList<Pair<String, Lamp>> lamps;

    public Room(String userName, String name, String ip, int port) {
        this.userName = userName;
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.lamps = new ArrayList<>();
    }

    public Pair<String, Lamp> addLamp(String lampNumber, String lampName) {
        Pair<String, Lamp> addedLamp = new Pair<String, Lamp>(lampNumber, new Lamp(lampName));
        lamps.add(addedLamp);
        return addedLamp;
    }

    public ArrayList<Pair<String, Lamp>> getLamps() {
        return lamps;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
