package gr.aueb.ticketify.authentication;

import gr.aueb.ticketify.core.enums.Role;
import gr.aueb.ticketify.core.exceptions.*;
import gr.aueb.ticketify.dto.UserRegisterDTO;
import gr.aueb.ticketify.dto.authentication.AuthenticationRequestDTO;
import gr.aueb.ticketify.dto.authentication.AuthenticationResponseDTO;
import gr.aueb.ticketify.mapper.Mapper;
import gr.aueb.ticketify.model.User;
import gr.aueb.ticketify.model.static_data.Region;
import gr.aueb.ticketify.repository.RegionRepository;
import gr.aueb.ticketify.repository.UserRepository;
import gr.aueb.ticketify.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

            User user = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new EntityNotAuthorizedException("User ", "User not authorized"));

            String token = jwtService.generateToken(authentication.getName(), user.getRole().name());

            log.info("User {} successfully authenticated", user.getUsername());

            return new AuthenticationResponseDTO(user.getUsername(), token, user.getRole());
        } catch (BadCredentialsException e) {
            log.warn("Invalid credentials for username: {}", dto.username());
            throw new EntityNotAuthorizedException("Authentication", "Invalid username or password");
        } catch (Exception e) {
            log.error("Authentication failed for username: {}", dto.username(), e);
            throw new AppServerException("Authentication", "An error occurred during authentication");
        }
    }

    @Transactional
    public AuthenticationResponseDTO register(UserRegisterDTO dto) {

        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new EntityNotFoundException("Region ", "Region with id " +
                        dto.getRegionId() + " not found"));

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new EntityInvalidArgumentException("User ", "Passwords do not match");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new EntityAlreadyExistsException("User ", "Username already exists");
        }

        User user = Mapper.mapUserCreateToModel(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        user.setRegion(region);

        User createdUser = userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());

        return new AuthenticationResponseDTO(createdUser.getUsername(), token, user.getRole());
    }

}
