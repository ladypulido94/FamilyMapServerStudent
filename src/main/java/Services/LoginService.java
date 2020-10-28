package Services;

import DAO.AuthDAO;
import DAO.DataAccessException;
import DAO.UserDAO;
import Model.User;
import Request.LoginRequest;
import Result.ErrorResult;
import Result.LoginResult;
import Result.Result;

public class LoginService extends Service {

    public Result login(LoginRequest request){
        setUp();

        if (conn == null)
            return new ErrorResult("Error getting the database");

        try {
            UserDAO userDAO = new UserDAO(conn);
            User user = userDAO.retrieve(request.getUserName());

            if(user.getPassword().equals(request.getPassword()))
                return new LoginResult(new AuthDAO(conn).newAuthToken(user).getToken(),
                    user.getUserName(), user.getPersonID());
            else return new ErrorResult("Error wrong password");
        } catch (DataAccessException e) {

            return new ErrorResult("error logging in the user or getting the database");
        } finally {
            try {
                db.closeConnection(true);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
        }
    }
}
