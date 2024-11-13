package alatoo.car_booking.controller;

import alatoo.car_booking.entity.DriverEntity;
import alatoo.car_booking.repository.DriverRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class DriverController {

  private final DriverRepository driverRepository;

  public DriverController(DriverRepository driverRepository) {
    this.driverRepository = driverRepository;
  }

  @PostMapping("/drivers/addNewDriver")
  public ResponseEntity<DriverEntity> createDriver(@RequestBody DriverEntity driver) {
    DriverEntity savedDriver = driverRepository.save(driver);
    return new ResponseEntity<>(savedDriver, HttpStatus.CREATED);
  }

  @GetMapping("/drivers/{driverId}")
  public ResponseEntity<DriverEntity> getDriver(@PathVariable Long driverId) {
    Optional<DriverEntity> driver = driverRepository.findById(driverId);
    if (driver.isPresent()) {
      return new ResponseEntity<>(driver.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/drivers/getAllDrivers")
  public ResponseEntity<List<DriverEntity>> getAllDrivers() {
    List<DriverEntity> drivers = driverRepository.findAll();
    drivers.sort((d1, d2) -> Double.compare(d2.getAvgRating(), d1.getAvgRating()));
    return new ResponseEntity<>(drivers, HttpStatus.OK);
  }
}
