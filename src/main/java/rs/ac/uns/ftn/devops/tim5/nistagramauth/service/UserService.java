package rs.ac.uns.ftn.devops.tim5.nistagramauth.service;

import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;

public interface UserService {

    User findByUsername(String username);
}
