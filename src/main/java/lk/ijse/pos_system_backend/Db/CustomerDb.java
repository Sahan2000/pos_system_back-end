package lk.ijse.pos_system_backend.Db;

import lk.ijse.pos_system_backend.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDb {
    public String generateCustomerId(Connection connection){
        String sql = "SELECT MAX(customerId) AS last_customer_id FROM customer;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                String lastCustomerId = resultSet.getString("last_customer_id");
                System.out.println(lastCustomerId);
                if (lastCustomerId == null){
                    return "cust-0001";
                }else {
                    int nextId = Integer.parseInt(lastCustomerId.substring(5))+1;
                    return "cust-" + String.format("%04d",nextId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean saveCustomer(Connection connection, CustomerDTO customerDTO){
        String sql = "insert into customer(customerId,customerName,city,email) value(?,?,?,?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,customerDTO.getCustomerId());
            preparedStatement.setString(2,customerDTO.getCustomerName());
            preparedStatement.setString(3,customerDTO.getCity());
            preparedStatement.setString(4,customerDTO.getEmail());

            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CustomerDTO getCustomer(Connection connection, String custId){
        String sql = "select * from customer where customerId=?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,custId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return new CustomerDTO(
                        resultSet.getString("customerId"),
                        resultSet.getString("customerName"),
                        resultSet.getString("city"),
                        resultSet.getString("email")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public ArrayList<CustomerDTO> getAllCustomer(Connection connection){
        String sql = "select * from customer;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();
             while (resultSet.next()){
                 CustomerDTO customerDTO = new CustomerDTO(
                         resultSet.getString("customerId"),
                         resultSet.getString("customerName"),
                         resultSet.getString("city"),
                         resultSet.getString("email")
                 );
                 customerDTOS.add(customerDTO);
             }
             return customerDTOS;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteCustomer(Connection connection, String custId){
        String sql = "delete from customer where customerId=?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,custId);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateCustomer(Connection connection, CustomerDTO customerDTO){
        String sql = "update customer set customerName=?, city=?, email=? where customerId=?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,customerDTO.getCustomerName());
            preparedStatement.setString(2,customerDTO.getCity());
            preparedStatement.setString(3,customerDTO.getEmail());
            preparedStatement.setString(4,customerDTO.getCustomerId());

            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
