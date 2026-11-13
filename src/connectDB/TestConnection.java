
package connectDB;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {

        try (Connection conn = ConnectDB.getConnection()) {

            if (conn != null) {
                System.out.println("Ket noi SQL Server thanh cong!");
            }

        } catch (SQLException e) {
            System.out.println("Ket noi that bai!");
            e.printStackTrace();
        }
    }
}
