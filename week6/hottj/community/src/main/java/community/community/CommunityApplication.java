package community.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class CommunityApplication {

	public static void main(String[] args) {

		SpringApplication.run(CommunityApplication.class, args);
	}

	@GetMapping
	public String HelloWorld(){
		return "Hello World";
	}
}
