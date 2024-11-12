package alatoo.car_booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "driver", uniqueConstraints = @UniqueConstraint(name = "driver_phone_number_unique", columnNames = "driver_phone_number"))
public class DriverEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "driver_id")
  private Long Id;

  @Column(name = "driver_name")
  private String driverName;

  @Column(name = "driver_phone_number")
  private Long driverPhoneNumber;

  @Column(name = "driver_avg_rating")
  private Double avgRating;
}
