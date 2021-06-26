package rs.ac.uns.ftn.devops.tim5.nistagramauth.service;

import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;

public interface MailService {

    @Async
    void sendMail(String email, String subject, String message) throws MessagingException;
}
