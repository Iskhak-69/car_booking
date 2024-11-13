package alatoo.car_booking.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface EmailService {
  String sendMail(MultipartFile[] files, String to, String[] cc, String subject, String body);
}
