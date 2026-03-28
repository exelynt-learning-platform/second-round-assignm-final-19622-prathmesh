package com.backend.ecommerce;

import com.backend.ecommerce.entity.Role;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {

        SpringApplication.run(EcommerceApplication.class, args);
    }

    // Seed Admin Credentials 
    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if(userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                User user = new User();

                user.setFirstName("Super");
                user.setLastName("Admin");
                user.setEmail("admin@gmail.com");
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setRole(Role.ADMIN);

                userRepository.save(user);
                System.out.println("\n Admin Email: admin@gmail.com");
                System.out.println("\n Admin Password: admin123");

            }
        };
    }

}
