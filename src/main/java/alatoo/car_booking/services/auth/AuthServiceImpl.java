package alatoo.car_booking.services.auth;

import alatoo.car_booking.dto.SignupRequest;
import alatoo.car_booking.dto.UserDto;
import alatoo.car_booking.entity.User;
import alatoo.car_booking.enums.UserRole;
import alatoo.car_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Override
    public UserDto createCustomer(SignupRequest signupRequest) {
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        user.setRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        return userDto;
    }

    @Override
    public boolean hasCustomerWithEmail(String email) {
        Optional<User> user = userRepository.findFirstByEmail(email);
        return user.isPresent();
    }
}
