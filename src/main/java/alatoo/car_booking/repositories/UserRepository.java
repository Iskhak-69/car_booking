package alatoo.car_booking.repositories;

import alatoo.car_booking.entities.Users;
import alatoo.car_booking.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findFirstByEmail(String email);

    Users findByUserRole(UserRole userRole);

    Optional<Users> findById(Users userId);
}
