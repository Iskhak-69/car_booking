package alatoo.car_booking.repository;

import alatoo.car_booking.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CarRepository extends JpaRepository<CarEntity, Long> {

  @Query("SELECT count(c) from CarEntity c where c.carType = 'SUV'")
  Integer findTotalNumberOfSUVcars();

  @Query("SELECT count(c) from CarEntity c where c.carType = 'mini'")
  Integer findTotalNumberOfMinicars();

}
