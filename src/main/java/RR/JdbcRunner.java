package RR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcRunner implements AuthService {


    private final Connection connection;
    private static final String PASSWORD = "fright89";
    private static final String USERNAME = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/auth_data";

    public JdbcRunner() {
        connection = start();
    }

    @Override
    public Connection start() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к базе данных");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String authUser(String login, String password) throws SQLException {
        String nick = "";
        try (final PreparedStatement ps = connection.prepareStatement("select nick from info.users_data where login = ? and password = ?")) {
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nick = rs.getString(1);
                System.out.println("Обнаружен пользователь " + nick);
            }
        }
        return nick;
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
