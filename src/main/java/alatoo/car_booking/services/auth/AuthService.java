package alatoo.car_booking.services.auth;

import alatoo.car_booking.dto.SignupRequest;
import alatoo.car_booking.dto.UserDto;

public interface AuthService {
    UserDto createCustomer(SignupRequest signupRequest);

    boolean hasCustomerWithEmail(String email);
}
