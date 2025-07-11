package gr.aueb.ticketify.authentication;

import gr.aueb.ticketify.core.enums.Role;
import gr.aueb.ticketify.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.ticketify.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.ticketify.core.exceptions.EntityNotAuthorizedException;
import gr.aueb.ticketify.core.exceptions.EntityNotFoundException;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.password(), dto.username()));

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotAuthorizedException("User ", "User not authorized"));

        String token = jwtService.generateToken(authentication.getName(), user.getRole().name());

        return new AuthenticationResponseDTO(user.getUsername(), token, user.getRole());
    }

    @Transactional
    public AuthenticationResponseDTO register(UserRegisterDTO dto) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new EntityInvalidArgumentException("User ", "Passwords do not match");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new EntityAlreadyExistsException("User ", "Username already exists");
        }

        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new EntityNotFoundException("Region ", "Region with id " +
                        dto.getRegionId() + " not found"));

        User user = Mapper.mapUserCreateToModel(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        user.setRegion(region);

        User createdUser = userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());

        return new AuthenticationResponseDTO(createdUser.getUsername(), token, user.getRole());
    }

}
