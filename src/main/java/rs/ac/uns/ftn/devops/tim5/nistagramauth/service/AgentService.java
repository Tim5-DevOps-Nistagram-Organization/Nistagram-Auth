package rs.ac.uns.ftn.devops.tim5.nistagramauth.service;

import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.UniqueFieldUserException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.AgentRegistration;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;

import javax.mail.MessagingException;
import java.util.Collection;


public interface AgentService {

    AgentRegistration findById(Long id) throws ResourceNotFoundException;
    AgentRegistration addAgentRegistrationRequest(User user) throws MessagingException, UniqueFieldUserException;
    AgentRegistration approveAgentRegistration(Long id) throws ResourceNotFoundException, MessagingException;
    AgentRegistration rejectAgentRegistration(Long id) throws ResourceNotFoundException, MessagingException;
    Collection<AgentRegistration> findAllAgentRegistrationRequests();
}
