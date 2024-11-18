package alatoo.car_booking.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class CarDto {
    private Long id;
    private String name;
    private String color;
    private String brand;
    private String transmission;
    private String type;
    private Date modelYear;
    private String description;
    private Integer price;
    private MultipartFile image;
    private byte[] returnedImage;
}
