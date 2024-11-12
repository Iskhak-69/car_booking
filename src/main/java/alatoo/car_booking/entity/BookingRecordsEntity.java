package alatoo.car_booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "booking_records")
public class BookingRecordsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "booking_records_id")
  private Long Id;

  @Column(name = "journey_start_date")
  private Date journeyStartDate;

  @Column(name = "journey_end_date")
  private Date journeyEndDate;

  // hire or rental
  @Column(name = "booking_type")
  private String bookingType;

  @Column(name = "number_of_mini")
  private Integer numberOfMini;

  @Column(name = "number_of_suv")
  private Integer numberOfSUV;

  @OneToOne
  @JoinColumn(name = "booking_records_id")
  private PaymentEntity payment;

  @OneToOne
  @JoinColumn(name = "booking_records_id")
  private LocationEntity location;

  // 'YES or null'
  @Column(name = "cancellation_status")
  private String cancellationStatus;

}
