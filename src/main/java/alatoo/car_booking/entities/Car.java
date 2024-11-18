package alatoo.car_booking.entities;

import alatoo.car_booking.dtos.CarDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private String brand;
    private String transmission;
    private String type;
    private Date modelYear;
    private String description;
    private Integer price;

    @Column(columnDefinition = "longblob")
    private byte[] image;

    public CarDto getCarDto(){
        CarDto cartDto = new CarDto();
        cartDto.setId(id);
        cartDto.setName(name);
        cartDto.setColor(color);
        cartDto.setBrand(brand);
        cartDto.setTransmission(transmission);
        cartDto.setType(type);
        cartDto.setModelYear(modelYear);
        cartDto.setDescription(description);
        cartDto.setPrice(price);
        cartDto.setReturnedImage(image);
        return cartDto;
    }


}
