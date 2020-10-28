package Request;

import Model.Event;
import Model.Person;
import Model.User;

import java.util.ArrayList;

public class LoadRequest extends Request {
    ArrayList<User> users;
    ArrayList<Person> persons;
    ArrayList<Event> events;

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
