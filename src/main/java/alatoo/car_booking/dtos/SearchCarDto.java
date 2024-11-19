package alatoo.car_booking.dtos;

import lombok.Data;

@Data
public class SearchCarDto {
    private String brand;
    private String type;
    private String color;
    private String transmission;

}
