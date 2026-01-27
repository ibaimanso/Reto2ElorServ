package menu;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.ApplicationRunner;

import server.SocketServer;
import util.HibernateUtil;

@SpringBootApplication

 public class Reto2ElorServApplication {

	public static void main(String[] args) {
		SpringApplication.run(Reto2ElorServApplication.class, args);
		
		
		
	}
	@Bean
    ApplicationRunner socketServerRunner() {
        return args -> {
            SocketServer server = new SocketServer();

            // Iniciar el servidor de sockets en un hilo separado para que no bloquee la app de Spring Boot
            Thread t = new Thread(server::start, "socket-server-thread");
            t.setDaemon(true);
            t.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.shutdown();
                HibernateUtil.shutdown();
            }));
        };
    }
}