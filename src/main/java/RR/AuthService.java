package RR;

import java.sql.Connection;
import java.sql.SQLException;

public interface AuthService {

    String authUser(String login, String password) throws SQLException;

    Connection start() throws SQLException;

    void disconnect();

}
