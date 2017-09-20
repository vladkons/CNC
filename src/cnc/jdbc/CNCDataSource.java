package cnc.jdbc;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

public class CNCDataSource {
    public CNCDataSource() {
        super();
    }


    public static Connection getConnection() {

        Connection conn = null;
        Context ctx = null;
        DataSource ds;

        try {
            ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("jdbc/CNCDS");
            conn = ds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}
