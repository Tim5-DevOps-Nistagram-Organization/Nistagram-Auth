package rs.ac.uns.ftn.devops.tim5.nistagramauth.mapper;

import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.AgentRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;

public class AgentMapper {

    private AgentMapper(){}

    public static User toEntity(AgentRequestDTO userDTO) {
        return new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getWebsiteUrl());
    }

}
