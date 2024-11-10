package alatoo.car_booking.dto;

import alatoo.car_booking.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    private String name;

    private String email;

    private UserRole role;
}
