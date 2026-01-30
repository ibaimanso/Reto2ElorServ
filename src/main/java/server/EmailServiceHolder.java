package server;

import menu.servicios.EmailService;
import org.springframework.stereotype.Component;

/**
 * Holder estático para el EmailService de Spring
 * Permite acceder al servicio desde clases que no son gestionadas por Spring
 */
@Component
public class EmailServiceHolder {
    private static EmailService emailService;
    
    public EmailServiceHolder(EmailService emailService) {
        EmailServiceHolder.emailService = emailService;
    }
    
    public static EmailService getEmailService() {
        return emailService;
    }
}
