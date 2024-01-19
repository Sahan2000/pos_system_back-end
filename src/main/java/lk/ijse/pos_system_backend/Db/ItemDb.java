package lk.ijse.pos_system_backend.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDb {
    public String generateItemCode(Connection connection){
        String sql = "SELECT MAX(itemCode) AS last_item_code FROM item;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                String lastItemCode = resultSet.getString("last_item_code");
                System.out.println(lastItemCode);
                if (lastItemCode == null){
                    return "item-0001";
                }else {
                    int nextId = Integer.parseInt(lastItemCode.substring(5))+1;
                    return "item-" + String.format("%04d",nextId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
