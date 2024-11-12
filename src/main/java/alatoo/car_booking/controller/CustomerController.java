package alatoo.car_booking.controller;

import alatoo.car_booking.entity.CustomerEntity;
import alatoo.car_booking.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CustomerController {

  private final CustomerRepository customerRepository;

  public CustomerController(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @PostMapping("/customers")
  public ResponseEntity<CustomerEntity> createCustomer(@RequestBody CustomerEntity customer) {
    CustomerEntity savedCustomer = customerRepository.save(customer);
    return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
  }

  @GetMapping("/customers/{customerId}")
  public ResponseEntity<CustomerEntity> getCustomer(@PathVariable Long customerId) {
    Optional<CustomerEntity> customer = customerRepository.findById(customerId);
    if (customer.isPresent()) {
      return new ResponseEntity<>(customer.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
