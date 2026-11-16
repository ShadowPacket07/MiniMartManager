package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static Connection con = null;
    private static final ConnectDB instance = new ConnectDB();

    // Constructor private chong khoi tao tu ben ngoai (Chuan mau Singleton Pattern IUH)
    private ConnectDB() {
    }

    public static ConnectDB getInstance() {
        return instance;
    }

    public synchronized void connect() throws SQLException {
        if (con == null || con.isClosed()) {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=GonMart_DB;encrypt=true;trustServerCertificate=true;";
            String user = "sa";
            String password = "sapassword";
            con = DriverManager.getConnection(url, user, password);
        }
    }

    public static synchronized Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                getInstance().connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public synchronized void disconnect() {
        if (con != null) {
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                con = null;
            }
        }
    }
}
