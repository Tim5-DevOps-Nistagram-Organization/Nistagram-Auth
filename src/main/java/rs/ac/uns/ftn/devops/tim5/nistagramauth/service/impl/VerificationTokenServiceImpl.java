package rs.ac.uns.ftn.devops.tim5.nistagramauth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.VerificationToken;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.repository.VerificationTokenRepository;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.VerificationTokenService;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public void create(User user, String token) {
        VerificationToken myToken = new VerificationToken(user, token);
        verificationTokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) throws ResourceNotFoundException {
        return verificationTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Token"));
    }

    @Override
    public void delete(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }

    @Override
    public VerificationToken getVerificationTokenByUsername(String username) throws ResourceNotFoundException {
        return verificationTokenRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Token"));
    }
}
