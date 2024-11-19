package alatoo.car_booking.services.customer;

import alatoo.car_booking.dtos.BookACarDto;
import alatoo.car_booking.dtos.CarDto;
import alatoo.car_booking.dtos.CarDtoList;
import alatoo.car_booking.dtos.SearchCarDto;

import java.util.List;

public interface CustomerService {
    List<CarDto> getAllCars();

    CarDto getCarById(Long carId);

    boolean bookACar(Long carId, BookACarDto bookACarDto);

    List<BookACarDto> getBookingsByUserId(Long userId);

    CarDtoList searchCar(SearchCarDto searchCarDto);
}
