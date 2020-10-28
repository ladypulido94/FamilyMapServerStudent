package Services;

import DAO.AuthDAO;
import DAO.DataAccessException;
import DAO.PersonDAO;
import Model.Person;
import Result.ErrorResult;
import Result.PersonResult;
import Result.PersonsResult;
import Result.Result;

import java.util.ArrayList;

public class PersonService extends Service {

    public Result singlePerson(String personID, String authtoken){
        PersonDAO personDAO = new PersonDAO(conn);
        try {
            Person person = personDAO.retrieve(personID);

            // validate that the user has the rights to get this person
            AuthDAO authTokenDAO = new AuthDAO(conn);
            if (!authTokenDAO.getUsernameForAuthtoken(authtoken).equals(person.getAssociatedUsername()))
                return new ErrorResult("Error you arecertainlynot authorized to get that person info");
            return new PersonResult(person.getAssociatedUsername(),
                    person.getPersonID(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getGender(),
                    person.getFatherID(),
                    person.getMotherID(),
                    person.getSpouseID());
        } catch (DataAccessException e) {
            return new ErrorResult("Error while getting single person");
        } finally {
            try {
                db.closeConnection(true);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
        }
    }

    /**
     * Returns ALL family members of the current user.
     * The current user is determined from the provided auth token.
     * @param authtoken Used to ascertain the user from which to pull the person data out of.
     * @return
     */
    public Result allPersons(String authtoken){
        PersonDAO personDAO = new PersonDAO(conn);
        // get username for authtoken
        AuthDAO tokenDAO = new AuthDAO(conn);
        try {
            // find username, then person list by username, then place that into the result and send it out
            String username = tokenDAO.getUsernameForAuthtoken(authtoken);
            ArrayList<Person> persons = personDAO.getPersonsForUsername(username);
            PersonsResult result = new PersonsResult();
            result.setData(persons);

            return result;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ErrorResult("Error while getting people");
        } finally {
            try {
                db.closeConnection(true);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
        }
    }
}
