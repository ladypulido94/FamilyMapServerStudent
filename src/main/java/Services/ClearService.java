package Services;

import DAO.*;
import Result.ErrorResult;
import Result.Result;
import Result.SuccessResult;

public class ClearService extends Service {

    public Result clear(){
        setUp();

        try {
            AuthDAO tokenDAO = new AuthDAO(conn);
            tokenDAO.clear();
            db.closeConnection(true);
            conn = db.openConnection();
            EventDAO eventDAO = new EventDAO(conn);
            eventDAO.clear();
            db.closeConnection(true);
            conn = db.openConnection();
            PersonDAO personDAO = new PersonDAO(conn);
            personDAO.clear();
            db.closeConnection(true);
            conn = db.openConnection();
            UserDAO userDAO = new UserDAO(conn);
            userDAO.clear();

            return new SuccessResult("clear succeeded");
        } catch (DataAccessException e) {

            e.printStackTrace();
            return new ErrorResult("failed to clear");
        } finally {

            try {
                db.closeConnection(true);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
