package com.sockan.websocket;

import com.sockan.model.SensorData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    ResultSet resultSet;
    List<SensorData> dataList = new ArrayList<>();
    String MySQLURL = "jdbc:mysql://localhost:3306/sensor?serverTimezone=UTC";
    String databseUserName = "root";
    //String databasePassword = "dht11_11thd";
    String databasePassword = "swe1618sql";

    public List<SensorData> getLastData() {
        String query = "select * from data order by data_id desc";
        Connection connectionTest = null;
        try {Class.forName("com.mysql.cj.jdbc.Driver");
            connectionTest = DriverManager.getConnection(MySQLURL, databseUserName, databasePassword);
            if (connectionTest != null) {
                System.out.println("Database connection is successful !!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(MySQLURL, databseUserName, databasePassword);
             PreparedStatement statement = connection.prepareStatement(query)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String payload = resultSet.getString("payload");
                String timestamp = resultSet.getString("timestamp");
                String data_type = resultSet.getString("data_type");
                dataList.add(new SensorData(payload, timestamp, data_type));
                /*System.out.println(payload);
                System.out.println(timestamp);
                System.out.println(data_type);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public void setToDatabase(String payload, String timestamp) {
        String query = "insert into data (payload, data_Type) values ('" + payload +"', '1');";
        Connection connectionTest = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connectionTest = DriverManager.getConnection(MySQLURL, databseUserName, databasePassword);
            if (connectionTest != null) {
                System.out.println("Database connection is successful !!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(MySQLURL, databseUserName, databasePassword);
             PreparedStatement statement = connection.prepareStatement(query)) {
            int resultSet = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
