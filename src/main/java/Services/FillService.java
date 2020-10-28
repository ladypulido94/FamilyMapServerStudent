package Services;

import DAO.DataAccessException;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import Model.*;
import Result.ErrorResult;
import Result.Result;
import Result.SuccessResult;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class FillService extends Service {

    private int personsAdded;
    private int eventsAdded;

    public Result fill(String username, int generations){
        personsAdded = 0;
        eventsAdded = 0;
        PersonDAO dao = new PersonDAO(conn);
        UserDAO userDAO = new UserDAO(conn);
        EventDAO eventDAO = new EventDAO(conn);
        try {

            User user = userDAO.retrieve(username);

            dao.clearFromUsername(username);
            fillHelper(user.getFirstName(), user.getLastName(), user.getGender(), generations,
                    dao, eventDAO, username,user.getPersonID(), null, 2050, null);

        } catch (DataAccessException | FileNotFoundException e) {
            e.printStackTrace();
            return new ErrorResult("Error while filling");
        } finally {
            try {
                db.closeConnection(true);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
        }
        return new SuccessResult("Successfully added " + personsAdded + " persons and " + eventsAdded + " events to the database.");
    }
    private void fillHelper(String firstName, String lastName, String gender,
                            int generation, PersonDAO dao, EventDAO eventDAO,
                            String username, String id, String spouseID,
                            int childsBirthYear, Event marriage) throws DataAccessException, FileNotFoundException {

        int birthYear = childsBirthYear-20;

        fillEvents(username,id,childsBirthYear,eventDAO, marriage);
        Person person;

        FileReader reader = new FileReader((gender.equals("m")?"json/mnames.json":"json/fnames.json"));
        Gson gson = new Gson();
        NamesData namesData = gson.fromJson(reader, NamesData.class);

        if (firstName==null) firstName = namesData.getRandomName();

        reader = new FileReader("json/snames.json");
        namesData = gson.fromJson(reader, NamesData.class);
        if (lastName == null) lastName = namesData.getRandomName();

        if (generation>0){

            String dadID = dao.generateID();
            String momID = dao.generateID();
            Event parentsMarriage = generateRandomEvent(username, dadID, eventDAO,
                    "marriage", childsBirthYear-3);

            //Filling out the mother and father
            fillHelper(null, null,"m", generation-1,
                    dao, eventDAO, username, dadID, momID, birthYear, parentsMarriage);
            parentsMarriage.setPersonID(momID);
            parentsMarriage.setEventID(dao.generateID());
            fillHelper(null, null, "f", generation-1,
                    dao, eventDAO, username, momID, dadID, birthYear, parentsMarriage);

            person = new Person(username, firstName, lastName, gender, dadID,
                    momID, spouseID, id);
        } else {
            person = new Person(username, firstName, lastName, gender, null,
                    null, spouseID, id);
        }
        dao.insert(person);
        personsAdded++;
    }

    private void fillEvents(String username, String personID, int childsBirthYear, EventDAO dao, Event marriage) throws FileNotFoundException, DataAccessException {
        dao.insert(generateRandomEvent(username, personID, dao, "birth",childsBirthYear-20));
        eventsAdded++;

        if (marriage != null) dao.insert(marriage);
        else dao.insert(generateRandomEvent(username, personID, dao, "marriage", childsBirthYear-3));
        eventsAdded++;

        dao.insert(generateRandomEvent(username, personID, dao, "death", childsBirthYear+60));
        eventsAdded++;
    }

    private Event generateRandomEvent(String username, String personID, EventDAO dao, String type, int year) throws FileNotFoundException {
        FileReader reader = new FileReader("json/locations.json");

        Gson gson = new Gson();
        LocationData locationData = gson.fromJson(reader, LocationData.class);
        Location location = locationData.getRandomLocation();
        return new Event(username, personID, location.getLatitude(), location.getLongitude(),
                location.getCountry(), location.getCity(), type, year, dao.generateID());
    }
}
