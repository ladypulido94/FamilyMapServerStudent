package Services;

import DAO.DataAccessException;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.ErrorResult;
import Result.Result;
import Result.SuccessResult;

public class LoadService extends Service{
    public Result load(LoadRequest request){
        try {
            db.clearTables();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ErrorResult("Error clearing tables for load.");
        }


        // insert users, persons, and events

        //count users, persons and events
        int users = 0; int persons = 0; int events = 0;
        UserDAO userDAO = new UserDAO(conn);
        PersonDAO personDAO = new PersonDAO(conn);
        EventDAO eventDAO = new EventDAO(conn);
        try {
            // loop through each section of the database and insert the given data
            for (User u : request.getUsers()) {
                userDAO.insert(u);
                users++;
            }
            for (Person p : request.getPersons()) {
                personDAO.insert(p);
                persons++;
            }
            for (Event e : request.getEvents()) {
                eventDAO.insert(e);
                events++;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ErrorResult("Error inserting user, person, or event.");
        } finally {
            try {
                db.closeConnection(true);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
        }

        // return final positive status
        return new SuccessResult("Successfully added " + users + " users, " + persons + " persons, and " + events + " events to the database.");
    }
}
