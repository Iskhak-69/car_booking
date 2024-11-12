package alatoo.car_booking.repository;

import alatoo.car_booking.entity.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DriverRepository extends JpaRepository<DriverEntity, Long> {

  @Query("SELECT count(d) from DriverEntity d")
  Integer findTotalNumberOfDrivers();

}
