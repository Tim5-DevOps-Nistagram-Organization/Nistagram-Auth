package rs.ac.uns.ftn.devops.tim5.nistagramauth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.UniqueFieldUserException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.Role;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.VerificationToken;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.repository.UserRepository;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.MailService;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.UserService;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.VerificationTokenService;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;
    private final MailService mailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, VerificationTokenService verificationTokenService,
                           MailService mailService) {
        this.userRepository = userRepository;
        this.verificationTokenService = verificationTokenService;
        this.mailService = mailService;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void registration(User user) throws Exception {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UniqueFieldUserException("Username");
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UniqueFieldUserException("Email");
        }
        user.setRole(Role.ROLE_REGULAR);
        user.setVerified(false);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user = userRepository.save(user);
        String token = UUID.randomUUID().toString();
        verificationTokenService.create(user, token);
        //todo localhost8080 ---> prebaciti da ide kroz gateway
        String subject = "Welcome!";
        String message = "<html><head><meta charset=\"UTF-8\"></head>" + "<body><h3>Nistagram app - Welcome!</h3><br>"
                + "<div><p>You can verify your email "
                + "<a target=\"_blank\" href = \"http://localhost:8080/user/verify/" + token
                + "\"><u>here</u></a>!.</p></div></body></html>";
        mailService.sendMail(user.getEmail(), subject, message);
    }

    @Override
    public User verifiedUserEmail(String token) throws ResourceNotFoundException {
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        User user = verificationToken.getUser();

        verificationTokenService.delete(verificationToken);
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            userRepository.deleteById(user.getId());
            throw new ResourceNotFoundException("User");
        }
        user.setVerified(true);
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteAndSendNotification(String username) throws MessagingException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new org.apache.kafka.common.errors.ResourceNotFoundException("User"));
        String email = user.getEmail();
        userRepository.delete(user);
        String subject = "Something went wrong!";
        String message = "<html><head><meta charset=\"UTF-8\"></head>"
                + "<body><h3>Nistagram app - Something went wrong!</h3><br>"
                + "<div><p>Something went wrong with your registration, please try again!"
                + "</p></div></body></html>";
        mailService.sendMail(email, subject, message);
    }
}
