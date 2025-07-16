package gr.aueb.ticketify.core;

import gr.aueb.ticketify.core.enums.Role;
import gr.aueb.ticketify.model.User;
import gr.aueb.ticketify.model.static_data.Region;
import gr.aueb.ticketify.repository.RegionRepository;
import gr.aueb.ticketify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile(("test"))
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Region region = regionRepository.getReferenceById(1L);

        if (userRepository.findByUsername("admin@example.com").isEmpty()) {
            User admin = User.builder()
                    .firstname("Admin")
                    .lastname("User")
                    .username("admin@example.com")
                    .password(passwordEncoder.encode("Admin123!"))
                    .phone("6900000000")
                    .role(Role.ADMIN)
                    .isActive(true)
                    .region(region)
                    .build();

            userRepository.save(admin);
        }
    }
}
