package rs.ac.uns.ftn.devops.tim5.nistagramauth.service;

import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;

import javax.mail.MessagingException;

public interface UserService {

    User findByUsername(String username);

    void registration(User user) throws Exception;

    User verifiedUserEmail(String token) throws ResourceNotFoundException;

    void deleteAndSendNotification(String username) throws MessagingException;
}
