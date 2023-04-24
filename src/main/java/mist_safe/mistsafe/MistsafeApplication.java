package mist_safe.mistsafe;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import appdev.firebase.config.FirebaseConfig;

@SpringBootApplication
public class MistsafeApplication {

	public static void main(String[] args) throws IOException {
		FirebaseConfig.configureFirebaseConnection();
		SpringApplication.run(MistsafeApplication.class, args);
	}

}
