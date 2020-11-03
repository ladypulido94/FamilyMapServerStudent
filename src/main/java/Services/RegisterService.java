package Services;

import DAO.AuthDAO;
import DAO.DataAccessException;
import DAO.PersonDAO;
import DAO.UserDAO;
import Model.AuthToken;
import Model.User;
import Request.RegisterRequest;
import Result.ErrorResult;
import Result.RegisterResult;
import Result.Result;

public class RegisterService extends Service {

    public Result register(RegisterRequest request){
        setUp();

        if (conn == null)
            return new ErrorResult("error registering the user or getting the database");

        PersonDAO personDAO = new PersonDAO(conn);
        String id = personDAO.generateID();

        User ourUser = new User(request.getUserName(), request.getPassword(), request.getEmail(),
                request.getFirstName(), request.getLastName(), request.getGender(), id);
        try {
            db.closeConnection(true);
            conn = db.openConnection();
            UserDAO userDAO = new UserDAO(conn);
            userDAO.insert(ourUser);
            db.closeConnection(true);
            conn = db.openConnection();
            AuthDAO authDAO = new AuthDAO(conn);
            AuthToken token = authDAO.newAuthToken(ourUser);
            String authToken = token.getToken();
            RegisterResult registerResult = new RegisterResult(authToken,ourUser.getUserName(), id);
            return registerResult;

        } catch (DataAccessException e) {
            return new ErrorResult("error registering the user or getting the database");
        } finally {
            try {
                db.closeConnection(true);
                FillService fill = new FillService();
                fill.fill(ourUser.getUserName(), 4);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
        }

    }
}
