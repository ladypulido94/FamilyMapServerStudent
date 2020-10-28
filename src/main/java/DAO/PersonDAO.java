package DAO;

import Model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class PersonDAO {
    private final Connection conn;

    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Person person) throws DataAccessException{
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO persons (username, FirstName, LastName, " +
                "Gender, \"father id\", \"mother id\", \"spouse id\", personID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getAssociatedUsername());
            stmt.setString(2, person.getFirstName());
            stmt.setString(3, person.getLastName());
            stmt.setString(4, person.getGender());
            stmt.setString(5, person.getMotherID());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getSpouseID());
            stmt.setString(8, person.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting person into the database");
        }
    }

    public Person retrieve(String personID) throws DataAccessException {
        String sql = "SELECT username, FirstName, LastName, Gender, \"mother id\", \"father id\", \"spouse id\", personID " +
                "FROM persons " +
                "WHERE personID=\'" + personID + "\'";

        Person result;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            result = new Person(rs.getString(1),rs.getString(2),rs.getString(3),
                    rs.getString(4),rs.getString(5),rs.getString(6),
                    rs.getString(7),rs.getString(8));
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while querying in the database");
        }

        return result;
    }

    public void clear() throws DataAccessException {
        try(PreparedStatement statement = conn.prepareStatement("DELETE FROM persons;")){
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException("Error when trying to clear");
        }
    }

    public void clearFromUsername(String username) throws DataAccessException {
        try(PreparedStatement statement = conn.prepareStatement("DELETE FROM persons WHERE username = \""+ username + "\";")){
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException("Error when trying to clear");
        }
    }

    public String generateID(){
        String id;
        boolean go = true;
        do {
            id = UUID.randomUUID().toString();
            try{
                retrieve(id);
                go = true;
            } catch(DataAccessException e){
                go = false;
            }
        } while (go);
        return id;
    }

    public ArrayList<Person> getPersonsForUsername(String username) throws DataAccessException {
        String sql = "SELECT username, FirstName, LastName, Gender, \"mother id\", \"father id\", \"spouse id\", personID " +
                "FROM persons " +
                "WHERE \"username\"=\"" + username + "\"";

        ArrayList<Person> result = new ArrayList<Person>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            // get more persons until there's no more of them
            while(rs.next()) {
                result.add(new Person(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while querying in the database");
        }

        return result;
    }
}
