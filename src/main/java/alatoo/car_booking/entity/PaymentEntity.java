package alatoo.car_booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "payment", uniqueConstraints = @UniqueConstraint(name = "booking_records_id_unique", columnNames = "booking_records_id"))
public class PaymentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Long Id;

  @Column(name = "booking_records_id")
  private Long bookingRecordsId;

  // pending or paid
  @Column(name = "payment_current_status")
  private String paymentCurrentStatus;

  @Column(name = "total_charge")
  private Integer totalCharge;

}
