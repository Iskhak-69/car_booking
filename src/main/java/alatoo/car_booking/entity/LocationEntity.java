package alatoo.car_booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "location")
public class LocationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "location_id")
  private Long Id;

  @Column(name = "pickup_city")
  private String pickupCity;

  @Column(name = "pickup_state")
  private String pickupState;

  @Column(name = "pickup_local_address")
  private String pickupLocalAddress;

  @Column(name = "drop_city")
  private String dropCity;

  @Column(name = "drop_state")
  private String dropState;

  @Column(name = "drop_local_address")
  private String dropLocalAddress;

  @Column(name = "country")
  private String country;

  @Column(name = "booking_records_id")
  private Long bookingRecordsId;

}
