package gr.aueb.ticketify.core;

import gr.aueb.ticketify.core.enums.Role;
import gr.aueb.ticketify.model.User;
import gr.aueb.ticketify.model.static_data.Region;
import gr.aueb.ticketify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByUsername("admin@example.com").isEmpty()) {
            User admin = User.builder()
                    .firstname("Admin")
                    .lastname("User")
                    .username("admin@example.com")
                    .password(passwordEncoder.encode("Admin123!"))
                    .phone("2100000000")
                    .region(Region.builder().name("ΑΘΗΝΑ").build())
                    .role(Role.ADMIN)
                    .isActive(true)
                    .build();
            userRepository.save(admin);
        }
    }
}
