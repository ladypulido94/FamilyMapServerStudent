package Services;

import DAO.DataAccessException;
import DAO.Database;

import java.sql.Connection;

public class Service {
    protected Database db;
    protected Connection conn = null;
    Service() {
        setUp();
    }
    protected void setUp(){
        //lets create a new database
        db = new Database();
        //Here, we'll open the connection in preparation for the register dao to use it.
        try {
            conn = db.getConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
