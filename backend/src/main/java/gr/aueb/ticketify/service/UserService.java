package gr.aueb.ticketify.service;

import gr.aueb.ticketify.core.exceptions.EntityNotAuthorizedException;
import gr.aueb.ticketify.core.exceptions.EntityNotFoundException;
import gr.aueb.ticketify.dto.UserReadOnlyDTO;
import gr.aueb.ticketify.dto.UserUpdateDTO;
import gr.aueb.ticketify.mapper.Mapper;
import gr.aueb.ticketify.model.User;
import gr.aueb.ticketify.model.static_data.Region;
import gr.aueb.ticketify.repository.RegionRepository;
import gr.aueb.ticketify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    @Override
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserById(Long id, Principal principal) {

        User requester = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotAuthorizedException("User ", "User not authenticated"));

        if (!requester.getRole().name().equals("ADMIN") && !requester.getId().equals(id)) {
            throw new EntityNotAuthorizedException("User ", "Access denied. You can only view your own profile.");
        }

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", "User with id " + id + " not found"));

        return Mapper.mapToUserReadOnlyDTO(targetUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserReadOnlyDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(Mapper::mapToUserReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserReadOnlyDTO updateUser(Long id, UserUpdateDTO updateDTO) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User ", "User with id " + id +
                        " not found"));

        if (updateDTO.getRegionId() != null) {
            Region region = regionRepository.findById(updateDTO.getRegionId())
                    .orElseThrow(() -> new EntityNotFoundException("Region", "Region with id " + updateDTO.getRegionId() +
                            " not found"));
            existingUser.setRegion(region);
        }

        User updatedUser = Mapper.mapUserUpdateToModel(updateDTO, existingUser);
        User savedUser = userRepository.save(updatedUser);
        System.out.println("Lastname after update: " + updatedUser.getLastname());
        return Mapper.mapToUserReadOnlyDTO(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User ", "User with id " + id +
                        " not found"));

        user.setIsActive(false);
        userRepository.delete(user);
    }
}
