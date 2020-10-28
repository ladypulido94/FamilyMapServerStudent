package DAO;

import Model.AuthToken;
import Model.User;

import java.sql.*;

public class AuthDAO {
    private final Connection conn;

    public AuthDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * get a valid auth token
     * @param user
     * @return
     */
    public AuthToken newAuthToken(User user) throws DataAccessException {
        // make the new authtoken
        AuthToken token = new AuthToken();
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO authtokens (token, timestamp, username) VALUES(?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, token.getToken());
            stmt.setString(2, new Timestamp(System.currentTimeMillis()).toString());
            stmt.setString(3, user.getUserName());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting token into the database");
        }
        return token;
    }

    /**
     * verifies your token
     * @param auth token to be authenticated
     * @return
     */
    public boolean verify(AuthToken auth) throws DataAccessException {
        String sql = "SELECT token " +
                "FROM authtokens " +
                "WHERE token=\'" + auth.getToken() + "\'";

        boolean result = false;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            result = rs.next();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while querying in the database");
        }
        return result;
    }

    /**
     * CLEAR *zap* *boom*
     * they're all dead, there's no trace of any authtoken anymore.
     */
    public void clear() throws DataAccessException {
        try(PreparedStatement statement = conn.prepareStatement("DELETE FROM authtokens;")){
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException("Error when trying to clear");
        }
    }

    /**
     * Give it the authtoken, get back the username associated with it.
     * @param authtoken Any valid authtoken
     * @return The username associated with the authtoken given.
     * @throws DataAccessException
     */

    public String getUsernameForAuthtoken(String authtoken) throws DataAccessException {
        String sql = "SELECT username " +
                "FROM authtokens " +
                "WHERE token=\'" + authtoken + "\'";

        String result;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            result = rs.getString(1);
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while querying in the database");
        }
        return result;
    }
}
