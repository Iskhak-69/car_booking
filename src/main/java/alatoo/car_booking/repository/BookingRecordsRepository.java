package alatoo.car_booking.repository;

import alatoo.car_booking.entity.BookingRecordsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface BookingRecordsRepository extends JpaRepository<BookingRecordsEntity, Long> {

  @Query("SELECT b from BookingRecordsEntity b where (b.journeyEndDate >= :startDate and b.journeyStartDate <= :endDate and b.cancellationStatus != 'YES')")
  List<BookingRecordsEntity> findBookingsConflictingWithStartDate(@Param("startDate") Date startDate,
      @Param("endDate") Date endDate);

  @Query("SELECT b from BookingRecordsEntity b where b.journeyStartDate <= :currentDate and b.journeyEndDate >= :currentDate and b.cancellationStatus != 'YES'")
  List<BookingRecordsEntity> findBookingsInTravel(@Param("currentDate") Date currentDate);

  @Query("SELECT b from BookingRecordsEntity b where b.journeyEndDate > :startDate and b.journeyStartDate <= :endDate and b.cancellationStatus != 'YES'")
  List<BookingRecordsEntity> findBookingsNotInTravel(@Param("startDate") Date startDate,
      @Param("endDate") Date endDate);

}
