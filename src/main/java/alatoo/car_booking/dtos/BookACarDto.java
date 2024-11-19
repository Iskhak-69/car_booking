package alatoo.car_booking.dtos;

import alatoo.car_booking.entities.Car;
import alatoo.car_booking.entities.Users;
import alatoo.car_booking.enums.BookCarStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BookACarDto {
    private Long id;
    private Date fromDate;
    private Date toDate;
    private Long days;
    private Long amount;
    private BookCarStatus bookCarStatus;
    private Long userId;
    private String username;
    private String email;
}
