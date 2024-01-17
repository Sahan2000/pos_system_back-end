package lk.ijse.pos_system_backend.Db;

import lk.ijse.pos_system_backend.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDb {
    public boolean saveUser(UserDTO userDTO, Connection connection){
        String sql = "insert into user(userName,email,password) values(?,?,?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userDTO.getUserName());
            preparedStatement.setString(2, userDTO.getEmail());
            preparedStatement.setString(3, userDTO.getPassword());

            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDTO getUser(String userName, Connection connection){
        String sql = "select * from user where userName=?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return new UserDTO(
                        resultSet.getString("userName"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
