package alatoo.car_booking.service.impl;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alatoo.car_booking.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

  @Value("${spring.mail.username}")
  private String fromEmail;

  @Autowired
  private JavaMailSender javaMailSender;

  @Override
  public String sendMail(MultipartFile[] files, String to, String[] cc, String subject, String body) {
    try {
      if (to == null || to.isEmpty()) {
        throw new IllegalArgumentException("Recipient email address is required.");
      }

      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

      mimeMessageHelper.setFrom(fromEmail);
      mimeMessageHelper.setTo(to);

      if (cc != null) {
        mimeMessageHelper.setCc(cc);
      }

      mimeMessageHelper.setSubject(subject);
      mimeMessageHelper.setText(body, true);

      if (files != null && files.length > 0) {
        for (MultipartFile file : files) {
          if (file.isEmpty()) {
            continue;  // Skip empty files
          }

          String filename = file.getOriginalFilename();
          if (filename != null && !filename.isEmpty()) {
            mimeMessageHelper.addAttachment(filename, new ByteArrayResource(file.getBytes()));
          }
        }
      }

      javaMailSender.send(mimeMessage);
      logger.info("Mail sent to: {}", to);
      return "Mail sent successfully";

    } catch (Exception e) {
      logger.error("Error sending email", e);
      throw new RuntimeException("Error sending email: " + e.getMessage(), e);
    }
  }
}
