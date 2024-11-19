package alatoo.car_booking.controllers;

import alatoo.car_booking.dtos.BookACarDto;
import alatoo.car_booking.dtos.CarDto;
import alatoo.car_booking.dtos.SearchCarDto;
import alatoo.car_booking.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getAllCars() {
        List<CarDto> carDtoList = customerService.getAllCars();
        return ResponseEntity.ok(carDtoList);
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long carId) {
        CarDto carDto = customerService.getCarById(carId);
        if (carDto != null) {
            return ResponseEntity.ok(carDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/car/book/{carId}")
    public ResponseEntity<?> bookACar(@PathVariable Long carId, @RequestBody BookACarDto bookACarDto) {
        boolean success = customerService.bookACar(carId, bookACarDto);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/car/bookings/{userId}")
    public ResponseEntity<?> getBookingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(customerService.getBookingsByUserId(userId));
    }

    @PostMapping("/car/search")
    public ResponseEntity<?> searchCar(@RequestBody SearchCarDto searchCarDto) {
        return ResponseEntity.ok(customerService.searchCar(searchCarDto));
    }

}
