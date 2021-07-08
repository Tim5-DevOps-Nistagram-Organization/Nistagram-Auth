package rs.ac.uns.ftn.devops.tim5.nistagramauth.mapper;

import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.AgentRegistrationDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.AgentRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.AgentRegistration;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;

public class AgentMapper {

    private AgentMapper() {
    }

    public static User toEntity(AgentRequestDTO userDTO) {
        return new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getWebsiteUrl());
    }

    public static AgentRegistrationDTO toDTO(AgentRegistration agentRegistration) {
        return new AgentRegistrationDTO(agentRegistration.getId(), agentRegistration.getUsername(),
                agentRegistration.getEmail(), agentRegistration.getWebsiteUrl());
    }
}
