package rs.ac.uns.ftn.devops.tim5.nistagramauth.service;

import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.UniqueFieldUserException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.Role;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;

import javax.mail.MessagingException;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id) throws ResourceNotFoundException;

    User registration(User user, Role role) throws MessagingException, UniqueFieldUserException;

    User verifiedUserEmail(String token) throws ResourceNotFoundException;

    User agentRegistrationApproval(User user) throws ResourceNotFoundException;

    void deleteAndSendNotification(String username) throws MessagingException;
}
