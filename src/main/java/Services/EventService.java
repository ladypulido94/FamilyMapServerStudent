package Services;

import DAO.AuthDAO;
import DAO.DataAccessException;
import DAO.EventDAO;
import Model.Event;
import Result.ErrorResult;
import Result.EventResult;
import Result.EventsResult;
import Result.Result;

import java.util.ArrayList;

public class EventService extends Service {

    public Result singleEvent(String eventID, String authtoken){

        EventDAO eventDAO = new EventDAO(conn);
        try {

            Event event = eventDAO.retrieve(eventID);

            AuthDAO authTokenDAO = new AuthDAO(conn);
            if (!authTokenDAO.getUsernameForAuthtoken(authtoken).equals(event.getAssociatedUsername())){
                return new ErrorResult("Error you are not authorized to get that event info");
            }

            return new EventResult(
                    event.getAssociatedUsername(),
                    event.getEventID(),
                    event.getPersonID(),
                    event.getLatitude(),
                    event.getLongitude(),
                    event.getCountry(),
                    event.getCity(),
                    event.getEventType(),
                    event.getYear());
        } catch (DataAccessException e) {

            return new ErrorResult("Error while getting single event");
        } finally {
            try {
                db.closeConnection(true);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
        }
    }


    public Result allEvents(String authtoken){
        EventDAO eventDAO = new EventDAO(conn);
        AuthDAO tokenDAO = new AuthDAO(conn);

        try {
            String username = tokenDAO.getUsernameForAuthtoken(authtoken);
            ArrayList<Event> events = eventDAO.getEventsForUsername(username);
            EventsResult result = new EventsResult();
            result.setMembers(events);

            return result;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ErrorResult("Error while getting events");
        } finally {

            try {
                db.closeConnection(true);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
        }
    }
}
