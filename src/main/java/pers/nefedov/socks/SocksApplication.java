package pers.nefedov.socks;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Demo Socks accounting application",
				description = "REST API для учета носков на складе магазина", version = "1.0.0",
				contact = @Contact(
						name = "Sergei Nefedov",
						email = "post.fvyf6@slmail.me"
				)
		)
)
public class SocksApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocksApplication.class, args);
	}

}
