package alatoo.car_booking.repository;

import alatoo.car_booking.entity.BCCDMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BCCDMappingRepository extends JpaRepository<BCCDMappingEntity, Long> {

  @Query("SELECT b from BCCDMappingEntity b where b.bookingRecordsId = :bookingId")
  List<BCCDMappingEntity> findBCCDMappingsByBookingId(@Param("bookingId") Long bookingId);
}
