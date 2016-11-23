package com.oracle;
import java.sql.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] argv) throws Exception {
        String url="jdbc:oracle:thin:@localhost:1521:xe";
        String username = "me";
        String password = "pass";
        Connection conn = DriverManager.getConnection(url, username, password);

    }
}
