package alatoo.car_booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "bccd_mapping")
public class BCCDMappingEntity {
  // Booking, car, customer, driver = bccd
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "bccd_mapping")
  private Long Id;

  @Column(name = "booking_records_id")
  private Long bookingRecordsId;

  @Column(name = "customer_id")
  private Long customerId;

  @Column(name = "driver_id")
  private Long driverId;

  @Column(name = "car_id")
  private Long carId;

  @Column(name = "rideRating")
  private Double rideRating;
}
