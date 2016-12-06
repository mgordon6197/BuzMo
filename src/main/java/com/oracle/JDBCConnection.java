package com.oracle;

import java.sql.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * C
 */
public class JDBCConnection {

    private static String url="jdbc:oracle:thin:@localhost:1521:xe";
    private static String username = "admin";
    private static String password = "123456";
    private static String dbDriver = "oracle.jdbc.driver.OracleDriver";

    private static Connection connection = null;

    static String convertDate(Date date) {
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sqlstringdate = dateformat.format(date);
        sqlstringdate = "TO_TIMESTAMP('" + sqlstringdate + "','" + "yyyy-mm-dd hh:mi:ss')";
        return sqlstringdate;
    }

    static int maxMid() {
        String query = "select max(mid) as mid from Messages";
        int max = 0;
        try {
            Connection connection = createDBConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            if(result.next())
                max = result.getInt("mid");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Can't find max...: " + e.toString());
        }
        return max;
    }

    static Connection createDBConnection() throws SQLException {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(connection == null)
            connection = DriverManager.getConnection(url, username, password);

        return connection;
    }

//    static ResultSet execQuery(Connection con, String query) {
//        Statement statement = null;
//        ResultSet result = null;
//        try {
//            statement = con.createStatement();
//            result = statement.executeQuery(query);
//            statement.close();
//        } catch (SQLException e) {
//            System.out.println("bad sql exception");
//        }
//        return result;
//    }
}
