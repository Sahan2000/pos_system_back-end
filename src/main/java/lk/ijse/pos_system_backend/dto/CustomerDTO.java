package lk.ijse.pos_system_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO{
    private String customerId;
    private String customerName;
    private String city;
    private String email;
}
