package alatoo.car_booking.repositories;

import alatoo.car_booking.dtos.BookACarDto;
import alatoo.car_booking.entities.BookACar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookACarRepository extends JpaRepository<BookACar, Long> {
    List<BookACar> findAllByUserId(Long userId);
}
