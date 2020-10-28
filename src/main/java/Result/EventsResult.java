package Result;

import Model.Event;

import java.util.ArrayList;

public class EventsResult extends Result{
    private ArrayList<Event> data;

    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    public void setMembers(ArrayList<Event> data) { this.data = data;}

    public EventsResult() {
        success = true;
    }
}
