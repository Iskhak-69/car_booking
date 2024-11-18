package alatoo.car_booking.services.auth;

import alatoo.car_booking.dtos.SignupRequest;
import alatoo.car_booking.dtos.UserDto;

public interface AuthService {
    UserDto createCustomer(SignupRequest signupRequest);

    boolean hasCustomerWithEmail(String email);
}
