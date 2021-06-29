package rs.ac.uns.ftn.devops.tim5.nistagramauth.mapper;

import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.AgentRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.UserDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;

public class UserMapper {

    public static User toEntity(UserDTO userDTO) {
        return new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail());
    }
}
