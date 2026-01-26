package menu.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordReset(String toEmail, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Recuperación de contraseña");
        message.setText("Tu nueva contraseña es: " + newPassword + "\nPor favor, cámbiala después de iniciar sesión.");
        mailSender.send(message);
    }
}
