package lk.ijse.pos_system_backend.dto;

import jakarta.json.bind.serializer.SerializationContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private String userName;
    private String email;
    private String password;
}
