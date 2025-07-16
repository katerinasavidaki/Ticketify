package gr.aueb.ticketify.service;

import gr.aueb.ticketify.dto.UserReadOnlyDTO;
import gr.aueb.ticketify.dto.UserUpdateDTO;

import java.security.Principal;
import java.util.List;

public interface IUserService {
    UserReadOnlyDTO getUserById(Long id, Principal principal);
    List<UserReadOnlyDTO> getAllUsers();
    UserReadOnlyDTO updateUser(Long id, UserUpdateDTO updateDTO);
    void deleteUser(Long id);
}
