package lk.ijse.pos_system_backend.Db;

import lk.ijse.pos_system_backend.api.Item;
import lk.ijse.pos_system_backend.dto.CustomerDTO;
import lk.ijse.pos_system_backend.dto.ItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public boolean saveItem(Connection connection, ItemDTO itemDTO){
        String sql = "insert into item(itemCode, description, qty, price) values(?,?,?,?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,itemDTO.getItemCode());
            preparedStatement.setString(2,itemDTO.getDescription());
            preparedStatement.setString(3,itemDTO.getQty());
            preparedStatement.setString(4,itemDTO.getPrice());

            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ItemDTO> getAllItem(Connection connection){
        String sql = "select * from item;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<ItemDTO> itemDTOS = new ArrayList<>();
            while (resultSet.next()){
                ItemDTO itemDTO = new ItemDTO(
                        resultSet.getString("itemCode"),
                        resultSet.getString("description"),
                        resultSet.getString("qty"),
                        resultSet.getString("price")
                );
                itemDTOS.add(itemDTO);
            }
            return itemDTOS;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteItem(Connection connection, String code){
        String sql = "delete from item where itemCode=?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,code);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateItem(Connection connection, ItemDTO itemDTO){
        String sql = "update item set description=?, qty=?, price=? where itemCode=?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,itemDTO.getDescription());
            preparedStatement.setString(2,itemDTO.getQty());
            preparedStatement.setString(3,itemDTO.getPrice());
            preparedStatement.setString(4,itemDTO.getItemCode());

            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemDTO getItem(Connection connection, String code){
        String sql = "select * from item where itemCode=?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,code);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return new ItemDTO(
                        resultSet.getString("itemCode"),
                        resultSet.getString("description"),
                        resultSet.getString("qty"),
                        resultSet.getString("price")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
