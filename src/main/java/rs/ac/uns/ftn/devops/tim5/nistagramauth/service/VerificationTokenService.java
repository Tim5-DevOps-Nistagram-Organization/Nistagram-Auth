package rs.ac.uns.ftn.devops.tim5.nistagramauth.service;

import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.VerificationToken;

public interface VerificationTokenService {
    void create(User user, String token);

    VerificationToken getVerificationToken(String token) throws ResourceNotFoundException;

    void delete(VerificationToken verificationToken);

    VerificationToken getVerificationTokenByUsername(String username) throws ResourceNotFoundException;
}
