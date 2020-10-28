package Result;

import Model.Person;

import java.util.ArrayList;

public class PersonsResult extends Result{
    private ArrayList<Person> data;

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
    }

    public PersonsResult() {
        success = true;
    }
}
