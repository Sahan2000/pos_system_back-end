package lk.ijse.pos_system_backend.Db;

import lk.ijse.pos_system_backend.api.Order;
import lk.ijse.pos_system_backend.dto.CombineDTo;
import lk.ijse.pos_system_backend.dto.OrderDTO;
import lk.ijse.pos_system_backend.dto.OrderDetailsDTO;

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
                    int nextId = Integer.parseInt(getLastId.substring(6))+1;
                    System.out.println("order-" + String.format("%04d",nextId));
                    return "order-" + String.format("%04d",nextId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean purchaseOrder(Connection connection, CombineDTo combineDTo){
        try {
            connection.setAutoCommit(false);

            OrderDTO orderDTO = new OrderDTO(combineDTo.getOrderId(), combineDTo.getOrderDate(), combineDTo.getCustomerId());
            OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(combineDTo.getOrderId(), combineDTo.getItemCode(), combineDTo.getOrderQty(), combineDTo.getPrice());
            if (saveOrder(connection,orderDTO)){
                boolean isSaveOrderDetails = new OrderDetailsDb().saveOrderDetails(connection, orderDetailsDTO);

                if (isSaveOrderDetails){
                    boolean isUpdateItemQty = new ItemDb().updateItemQty(connection,orderDetailsDTO.getItemCode(),orderDetailsDTO.getGetQty());
                    if (!isUpdateItemQty){
                        return false;
                    }
                }else {
                    return false;
                }

                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean saveOrder(Connection connection, OrderDTO orderDTO){
        String sql = "insert into orders(orderId, orderDate, customerId) values(?,?,?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,orderDTO.getOrderId());
            preparedStatement.setString(2,orderDTO.getOrderDate());
            preparedStatement.setString(3,orderDTO.getCustomerId());

            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
