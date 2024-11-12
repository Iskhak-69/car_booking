package alatoo.car_booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "car")
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long Id;

    @Column(name = "car_model")
    private String Model;

    @Column(name = "car_manufacturer")
    private String Manufacturer;

    // mini or SUV
    @Column(name = "car_type")
    private String carType;

    @Column(name = "price_per_km")
    private Integer pricePerKm;

    @Column(name = "car_number")
    private String carNumber;
}
