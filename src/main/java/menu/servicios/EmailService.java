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
    
    /**
     * Envía notificación de nueva reunión creada
     */
    public void sendReunionCreada(String toEmail, String nombreProfesor, String titulo, String fecha, String hora, String aula) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Nueva reunión programada - ElorES");
        message.setText(String.format(
            "Estimado/a,\n\n" +
            "Se ha programado una nueva reunión:\n\n" +
            "Título: %s\n" +
            "Profesor: %s\n" +
            "Fecha: %s\n" +
            "Hora: %s\n" +
            "Aula: %s\n" +
            "Centro: CIFP Elorrieta-Errekamari LHII\n\n" +
            "Estado: Pendiente\n\n" +
            "Por favor, confirma tu asistencia a través de la plataforma.\n\n" +
            "Saludos cordiales,\n" +
            "Sistema ElorES",
            titulo, nombreProfesor, fecha, hora, aula
        ));
        
        try {
            mailSender.send(message);
            System.out.println("✅ Correo de reunión enviado a: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Error enviando correo a " + toEmail + ": " + e.getMessage());
        }
    }
    
    /**
     * Envía notificación de cambio de estado de reunión
     */
    public void sendReunionEstadoCambiado(String toEmail, String titulo, String nuevoEstado, String fecha, String hora) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Actualización de reunión - ElorES");
        
        String estadoTexto = "";
        switch (nuevoEstado.toLowerCase()) {
            case "aceptada":
                estadoTexto = "ha sido ACEPTADA";
                break;
            case "denegada":
                estadoTexto = "ha sido DENEGADA";
                break;
            case "cancelada":
                estadoTexto = "ha sido CANCELADA";
                break;
            default:
                estadoTexto = "ha cambiado a: " + nuevoEstado;
        }
        
        message.setText(String.format(
            "Estimado/a,\n\n" +
            "La reunión '%s' programada para el %s a las %s %s.\n\n" +
            "Saludos cordiales,\n" +
            "Sistema ElorES",
            titulo, fecha, hora, estadoTexto
        ));
        
        try {
            mailSender.send(message);
            System.out.println("✅ Correo de actualización enviado a: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Error enviando correo a " + toEmail + ": " + e.getMessage());
        }
    }
    
    /**
     * Envía notificación de reunión eliminada
     */
    public void sendReunionEliminada(String toEmail, String titulo, String fecha, String hora) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Reunión cancelada - ElorES");
        message.setText(String.format(
            "Estimado/a,\n\n" +
            "La reunión '%s' programada para el %s a las %s ha sido CANCELADA.\n\n" +
            "Saludos cordiales,\n" +
            "Sistema ElorES",
            titulo, fecha, hora
        ));
        
        try {
            mailSender.send(message);
            System.out.println("✅ Correo de cancelación enviado a: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Error enviando correo a " + toEmail + ": " + e.getMessage());
        }
    }
}