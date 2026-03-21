package in.ankitsaahariya.WalletLedger.servicesImp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Async
    public void sendActivationEmail(String toEmail, String token, String name) {
        try {
            String activationLink = "http://localhost:8080/api/v1.0/activate?token=" + token;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Activate Your WalletLedger Account");
            message.setText(buildEmailBody(name, activationLink));
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send activation email: " + e.getMessage());
        }
    }

    private String buildEmailBody(String name, String activationLink) {
        return """
                Hi %s,

                Welcome to WalletLedger!

                Please activate your account by clicking the link below:

                %s

                ⚠ This link is valid for 24 hours only.
                If you did not register, please ignore this email.

                Regards,
                WalletLedger Team
                """.formatted(name, activationLink);
    }
}

