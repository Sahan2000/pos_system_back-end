package lk.ijse.pos_system_backend.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDb {

    public String generateOrderId(Connection connection){
        String sql = "SELECT MAX(orderId) AS last_order_id FROM orders;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                String getLastId = resultSet.getString("last_order_id");
                System.out.println(getLastId);
                if (getLastId == null){
                    return "order-0001";
                }else {
                    int nextId = Integer.parseInt(getLastId.substring(5))+1;
                    return "order-" + String.format("%05d",nextId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
