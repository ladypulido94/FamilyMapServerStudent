package DAO;

import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(User user) throws DataAccessException{
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO users (username, password, email, FirstName, LastName, " +
                "Gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender().toLowerCase());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting user into the database");
        }
    }

    public User retrieve(String username) throws DataAccessException {
        String sql = "SELECT username, password, email, FirstName, LastName, Gender, personID " +
                "FROM users " +
                "WHERE username=\'" + username + "\'";

        User result;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            result = new User(rs.getString(1),rs.getString(2),rs.getString(3),
                    rs.getString(4),rs.getString(5),rs.getString(6),
                    rs.getString(7));
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while querying in the database");
        }

        return result;
    }

    public void clear() throws DataAccessException {
        try(PreparedStatement statement = conn.prepareStatement("DELETE FROM users;")){
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException("Error when trying to clear");
        }
    }
}
