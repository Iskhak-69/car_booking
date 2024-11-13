package alatoo.car_booking.controller;

import alatoo.car_booking.dto.endpoint.bookingRecords.BookCarFormDto;
import alatoo.car_booking.dto.message.ResponseDto;
import alatoo.car_booking.entity.*;
import alatoo.car_booking.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
import alatoo.car_booking.service.EmailService;

@RestController
public class BookingRecordsController {

  @Autowired
  private BookingRecordsRepository bookingRecordsRepository;

  @Autowired
  private DriverRepository driverRepository;

  @Autowired
  private CarRepository carRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private BCCDMappingRepository bccdMappingRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private EmailService emailService;

  private static final Logger logger = LoggerFactory.getLogger(BookingRecordsController.class);

  @PostMapping("/bookingRecords/addNewBooking")
  public ResponseEntity<ResponseDto<?>> createBooking(@RequestBody BookCarFormDto bookingForm) {
    List<BookingRecordsEntity> bookingsInConflict = bookingRecordsRepository
        .findBookingsConflictingWithStartDate(bookingForm.getStartDate(), bookingForm.getEndDate());

    List<Long> occupiedCarIds = bookingsInConflict.stream()
        .flatMap(booking -> bccdMappingRepository.findBCCDMappingsByBookingId(booking.getId()).stream())
        .map(mapping -> mapping.getCarId())
        .collect(Collectors.toList());

    List<Long> occupiedDriverIds = bookingsInConflict.stream()
        .flatMap(booking -> bccdMappingRepository.findBCCDMappingsByBookingId(booking.getId()).stream())
        .map(mapping -> mapping.getDriverId())
        .filter(id -> id != null)
        .collect(Collectors.toList());

    List<CarEntity> availableCars = carRepository.findAll().stream()
        .filter(car -> !occupiedCarIds.contains(car.getId()))
        .collect(Collectors.toList());

    List<DriverEntity> availableDrivers = driverRepository.findAll().stream()
        .filter(driver -> !occupiedDriverIds.contains(driver.getId()))
        .collect(Collectors.toList());

    int occupiedSUV = 0;
    int occupiedMini = 0;
    int occupiedDrivers = 0;

    Integer totalDrivers = driverRepository.findTotalNumberOfDrivers();
    Integer totalSUVcars = carRepository.findTotalNumberOfSUVcars();
    Integer totalMinicars = carRepository.findTotalNumberOfMinicars();

    for (BookingRecordsEntity booking : bookingsInConflict) {
      occupiedSUV = occupiedSUV + booking.getNumberOfSUV();
      occupiedMini = occupiedMini + booking.getNumberOfMini();
      if (booking.getBookingType().equals("hire")) {
        occupiedDrivers = occupiedDrivers + booking.getNumberOfSUV() + booking.getNumberOfMini();
      }
    }

    int availableSUV = totalSUVcars - occupiedSUV;
    int availableMini = totalMinicars - occupiedMini;
    int availableDriversCount = totalDrivers - occupiedDrivers;

    if (bookingForm.getNumberOfSUVcars() > availableSUV && bookingForm.getNumberOfMinicars() > availableMini) {
      ResponseDto<String> send = new ResponseDto<>();
      send.setResponse(null);
      send.setError("Only " + availableSUV + " SUV cars and " + availableMini + " mini cars  are available on the date "
          + bookingForm.getStartDate());
      return new ResponseEntity<>(send, HttpStatus.NOT_FOUND);
    }

    if (bookingForm.getNumberOfSUVcars() > availableSUV) {
      ResponseDto<String> send = new ResponseDto<>();
      send.setResponse(null);
      send.setError("Only " + availableSUV + " SUV cars are available on the date " + bookingForm.getStartDate());
      return new ResponseEntity<>(send, HttpStatus.NOT_FOUND);
    }

    if (bookingForm.getNumberOfMinicars() > availableMini) {
      ResponseDto<String> send = new ResponseDto<>();
      send.setResponse(null);
      send.setError("Only " + availableMini + " mini cars are available on the date " + bookingForm.getStartDate());
      return new ResponseEntity<>(send, HttpStatus.NOT_FOUND);
    }

    if (bookingForm.getBookingType().equals("hire")
        && (bookingForm.getNumberOfSUVcars() + bookingForm.getNumberOfMinicars() > availableDriversCount)) {
      ResponseDto<String> send = new ResponseDto<>();
      send.setResponse(null);
      send.setError(
          "Only " + availableDriversCount + " drivers are available on the date " + bookingForm.getStartDate());
      return new ResponseEntity<>(send, HttpStatus.NOT_FOUND);
    }

    // If there are enough cars and drivers available, create the booking
    BookingRecordsEntity newBooking = new BookingRecordsEntity();
    newBooking.setJourneyStartDate(bookingForm.getStartDate());
    newBooking.setJourneyEndDate(bookingForm.getEndDate());
    newBooking.setBookingType(bookingForm.getBookingType());
    newBooking.setNumberOfSUV(bookingForm.getNumberOfSUVcars());
    newBooking.setNumberOfMini(bookingForm.getNumberOfMinicars());

    LocationEntity location = new LocationEntity();
    location.setCountry(bookingForm.getCountry());
    location.setPickupCity(bookingForm.getPickupCity());
    location.setPickupState(bookingForm.getPickupState());
    location.setPickupLocalAddress(bookingForm.getPickupLocalAddress());
    location.setDropCity(bookingForm.getDropCity());
    location.setDropState(bookingForm.getDropState());
    location.setDropLocalAddress(bookingForm.getDropLocalAddress());

    BookingRecordsEntity savedBooking = bookingRecordsRepository.save(newBooking);

    location.setBookingRecordsId(savedBooking.getId());
    locationRepository.save(location);

    savedBooking.setLocation(location);

    Optional<CustomerEntity> customerOptional = customerRepository.findByCustomerEmail(bookingForm.getCustomerEmail());
    CustomerEntity customer;

    if (customerOptional.isPresent()) {
      customer = customerOptional.get();
    } else {
      customer = new CustomerEntity();
      customer.setCustomerName(bookingForm.getCustomerName());
      customer.setCustomerEmail(bookingForm.getCustomerEmail());
      customer.setCustomerPhoneNumber(bookingForm.getCustomerPhoneNumber());
      customer = customerRepository.save(customer);
    }

    Integer neededSUV = bookingForm.getNumberOfSUVcars();
    Integer neededMini = bookingForm.getNumberOfMinicars();
    Integer totalCharge = 0;

    for (CarEntity car : availableCars) {
      if ((neededSUV > 0 && car.getCarType().equals("SUV"))) {
        totalCharge = totalCharge + (car.getPricePerKm() * bookingForm.getDistance());

        DriverEntity driver = null;
        if (!availableDrivers.isEmpty()) {
          driver = availableDrivers.remove(0);
        }

        BCCDMappingEntity newMap = new BCCDMappingEntity();
        newMap.setBookingRecordsId(savedBooking.getId());
        newMap.setCarId(car.getId());
        if (driver != null && bookingForm.getBookingType().equals("hire")) {
          newMap.setDriverId(driver.getId());
        }
        newMap.setCustomerId(customer.getId());

        bccdMappingRepository.save(newMap);

        neededSUV--;
        continue;
      }

      if ((neededMini > 0 && car.getCarType().equals("mini"))) {
        totalCharge = totalCharge + (car.getPricePerKm() * bookingForm.getDistance());

        DriverEntity driver = null;
        if (!availableDrivers.isEmpty()) {
          driver = availableDrivers.remove(0);
        }

        BCCDMappingEntity newMap = new BCCDMappingEntity();
        newMap.setBookingRecordsId(savedBooking.getId());
        newMap.setCarId(car.getId());
        if (driver != null && bookingForm.getBookingType().equals("hire")) {
          newMap.setDriverId(driver.getId());
        }
        newMap.setCustomerId(customer.getId());

        bccdMappingRepository.save(newMap);
        neededMini--;
        continue;
      }
    }

    PaymentEntity payment = new PaymentEntity();
    payment.setBookingRecordsId(savedBooking.getId());
    payment.setTotalCharge(totalCharge);
    payment.setPaymentCurrentStatus("pending");

    payment = paymentRepository.save(payment);

    savedBooking.setPayment(payment);

    savedBooking = bookingRecordsRepository.save(savedBooking);

    ResponseDto<BookingRecordsEntity> send = new ResponseDto<>();
    send.setResponse(savedBooking);
    send.setError(null);

    String emailStatus = emailService.sendMail(null, customer.getCustomerEmail(), null, "Booking Confirmation",
        "<h1 style='background: black;color: white;padding: 1em;'>Your booking has been confirmed!</h1>" +
            "<p style='background: lightgreen;padding: 1em;'>You booked "
            + (bookingForm.getNumberOfSUVcars() + bookingForm.getNumberOfMinicars()) + " cars for the date "
            + bookingForm.getStartDate() + " </p>");

    logger.info("Email Status: {}", emailStatus);

    return new ResponseEntity<>(send, HttpStatus.CREATED);
  }

  @GetMapping("/bookingRecords/{bookingId}")
  public ResponseEntity<?> getBooking(@PathVariable Long bookingId) {
    Optional<BookingRecordsEntity> booking = bookingRecordsRepository.findById(bookingId);
    if (booking.isPresent()) {
      return new ResponseEntity<>(booking.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Booking Not Found", HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/bookingRecords/availableCarsAndDrivers")
  public ResponseEntity<?> getAvailableCarsAndDrivers(
      @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate) {
    List<BookingRecordsEntity> bookingsInConflict = bookingRecordsRepository
        .findBookingsConflictingWithStartDate(startDate, endDate);

    List<Long> occupiedCarIds = bookingsInConflict.stream()
        .flatMap(booking -> bccdMappingRepository.findBCCDMappingsByBookingId(booking.getId()).stream())
        .map(mapping -> mapping.getCarId())
        .collect(Collectors.toList());

    List<Long> occupiedDriverIds = bookingsInConflict.stream()
        .flatMap(booking -> bccdMappingRepository.findBCCDMappingsByBookingId(booking.getId()).stream())
        .map(mapping -> mapping.getDriverId())
        .filter(id -> id != null)
        .collect(Collectors.toList());

    List<CarEntity> availableCars = carRepository.findAll().stream()
        .filter(car -> !occupiedCarIds.contains(car.getId()))
        .collect(Collectors.toList());

    List<DriverEntity> availableDrivers = driverRepository.findAll().stream()
        .filter(driver -> !occupiedDriverIds.contains(driver.getId()))
        .collect(Collectors.toList());

    availableDrivers.sort((d1, d2) -> Double.compare(d2.getAvgRating(), d1.getAvgRating()));

    Map<String, List<?>> response = new HashMap<>();
    response.put("availableCars", availableCars);
    response.put("availableDrivers", availableDrivers);

    return new ResponseEntity<>(response, HttpStatus.OK);

  }

  @GetMapping("/bookingRecords/bookedCarsInTravel")
  public ResponseEntity<?> getBookedCarsInTravel(@RequestParam("givenDate") Date givenDate) {
    // Fetch all bookings that are currently in travel
    List<BookingRecordsEntity> bookingsInTravel = bookingRecordsRepository.findBookingsInTravel(givenDate);

    // Prepare the response
    Map<Long, List<Map<String, Object>>> response = new HashMap<>();

    for (BookingRecordsEntity booking : bookingsInTravel) {
      List<BCCDMappingEntity> mappings = bccdMappingRepository.findBCCDMappingsByBookingId(booking.getId());
      List<Map<String, Object>> bookingInfos = new ArrayList<>();
      for (BCCDMappingEntity mapping : mappings) {
        Map<String, Object> bookingInfo = new HashMap<>();
        CarEntity car = carRepository.findById(mapping.getCarId()).orElse(null);
        DriverEntity driver = driverRepository.findById(mapping.getDriverId()).orElse(null);
        LocationEntity location = locationRepository.findById(booking.getLocation().getId()).orElse(null);
        PaymentEntity payment = paymentRepository.findById(booking.getPayment().getId()).orElse(null);

        bookingInfo.put("carNumber", car.getCarNumber());
        bookingInfo.put("startLocation", location.getPickupLocalAddress());
        bookingInfo.put("dropLocation", location.getDropLocalAddress());
        if (driver != null) {
          bookingInfo.put("driverName", driver.getDriverName());
        }
        bookingInfo.put("paymentAmount", payment.getTotalCharge());
        bookingInfos.add(bookingInfo);
      }
      response.put(booking.getId(), bookingInfos);
    }

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/bookingRecords/bookedCarsNotInTravel")
  public ResponseEntity<?> getBookedCarsNotInTravel(@RequestParam("startDate") Date startDate,
      @RequestParam("endDate") Date endDate) {
    // Fetch all bookings that are currently not in travel but booked for other
    // rides
    List<BookingRecordsEntity> bookingsInTravel = bookingRecordsRepository.findBookingsNotInTravel(startDate, endDate);

    // Prepare the response
    Map<Long, List<Map<String, Object>>> response = new HashMap<>();

    for (BookingRecordsEntity booking : bookingsInTravel) {
      List<BCCDMappingEntity> mappings = bccdMappingRepository.findBCCDMappingsByBookingId(booking.getId());
      List<Map<String, Object>> bookingInfos = new ArrayList<>();
      for (BCCDMappingEntity mapping : mappings) {
        Map<String, Object> bookingInfo = new HashMap<>();
        CarEntity car = carRepository.findById(mapping.getCarId()).orElse(null);
        DriverEntity driver = driverRepository.findById(mapping.getDriverId()).orElse(null);
        LocationEntity location = locationRepository.findById(booking.getLocation().getId()).orElse(null);
        PaymentEntity payment = paymentRepository.findById(booking.getPayment().getId()).orElse(null);

        bookingInfo.put("carNumber", car.getCarNumber());
        bookingInfo.put("startLocation", location.getPickupLocalAddress());
        bookingInfo.put("dropLocation", location.getDropLocalAddress());
        if (driver != null) {
          bookingInfo.put("driverName", driver.getDriverName());
        }
        bookingInfo.put("paymentAmount", payment.getTotalCharge());
        bookingInfos.add(bookingInfo);
      }
      response.put(booking.getId(), bookingInfos);
    }

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("/bookingRecords/cancelBooking/{bookingId}")
  public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {

    Optional<BookingRecordsEntity> bookingOpt = bookingRecordsRepository.findById(bookingId);
    if (!bookingOpt.isPresent()) {
      return new ResponseEntity<>("Booking not found", HttpStatus.NOT_FOUND);
    }
    BookingRecordsEntity booking = bookingOpt.get();

    List<BCCDMappingEntity> mappings = bccdMappingRepository.findBCCDMappingsByBookingId(booking.getId());

    CustomerEntity customer = customerRepository.findById(mappings.get(0).getCustomerId()).orElse(null);
    if (customer == null) {
      return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
    }

    booking.setCancellationStatus("YES");
    bookingRecordsRepository.save(booking);

    Optional<PaymentEntity> paymentOpt = paymentRepository.findById(booking.getPayment().getId());
    if (!paymentOpt.isPresent()) {
      return new ResponseEntity<>("Payment not found", HttpStatus.NOT_FOUND);
    }
    PaymentEntity payment = paymentOpt.get();

    payment.setPaymentCurrentStatus("cancelled");
    paymentRepository.save(payment);

    String emailContent = "<h1 style='background: black;color: white;padding: 1em;'>Your booking has been cancelled!</h1>"
        +
        "<p style='background: lightgreen;padding: 1em;'>Booked "
        + (booking.getNumberOfSUV() + booking.getNumberOfMini()) + " cars for the date "
        + booking.getJourneyStartDate() + " has been cancelled </p>";

    String emailStatus = emailService.sendMail(null, customer.getCustomerEmail(), null, "Booking Cancellation",
        emailContent);

    logger.info("Email Status: {}", emailStatus);

    return new ResponseEntity<>("Booking cancelled successfully", HttpStatus.OK);
  }

}
