package alatoo.car_booking.services.admin;

import alatoo.car_booking.dtos.BookACarDto;
import alatoo.car_booking.dtos.CarDto;
import alatoo.car_booking.dtos.CarDtoList;
import alatoo.car_booking.dtos.SearchCarDto;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    boolean postCar(CarDto carDto);

    List<CarDto> getAllCars();

    void deleteCar(Long carId);

    CarDto getCarById(Long carId);

    boolean updateCar(Long carId, CarDto carDto) throws IOException;

    List<BookACarDto> getBookings();

    boolean changeBookingStatus(Long bookingId, String status);

    CarDtoList searchCar(SearchCarDto searchCarDto);
}
