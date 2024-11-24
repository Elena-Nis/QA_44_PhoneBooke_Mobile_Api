package db_mysql;

import java.sql.*;
import java.util.Random;

public class BaseSQL {
    static Connection connection;

    static Statement statement;

    static final String URL = "jdbc:mysql://localhost:8181/alexbase";
    static final String USER = "root";
    static final String PASSWORD = "qwerty";

    public static void main(String[] args) {
        startConnect();
        try {
            int quantity;
            statement = connection.createStatement();
            int id = new Random().nextInt(100) + 20;
            quantity = statement.executeUpdate("INSERT INTO table1 (id, name, family, email) " +
                    "VALUES ('" + id + "', 'alex5', 'med15', 'alex5@mail');");
            System.out.println(quantity);

            ResultSet resultSet  = statement.executeQuery("select * from table1");
            while (resultSet.next()){
                System.out.print(resultSet.getString("id")+" ");
                System.out.print(resultSet.getString(2)+" ");
                System.out.print(resultSet.getString(3)+" ");
                System.out.println(resultSet.getString("email")+" ");
            }

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    static void startConnect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (!connection.isClosed()) {
                System.out.println("create new connection");
            } else
                System.out.println("connection don't create");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}