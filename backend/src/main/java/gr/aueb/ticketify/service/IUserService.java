package gr.aueb.ticketify.service;

import gr.aueb.ticketify.dto.UserReadOnlyDTO;
import gr.aueb.ticketify.dto.UserUpdateDTO;

import java.util.List;

public interface IUserService {
    UserReadOnlyDTO getUserById(Long id);
    List<UserReadOnlyDTO> getAllUsers();
    UserReadOnlyDTO updateUser(Long id, UserUpdateDTO updateDTO);
    void deleteUser(Long id);
}
