package lk.ijse.pos_system_backend.dto;

import jakarta.json.bind.serializer.SerializationContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userName;
    private String email;
    private String password;
}
