package rs.ac.uns.ftn.devops.tim5.nistagramauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.AccessTokenDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.LoginDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.UserDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.kafka.saga.UserOrchestrator;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.mapper.UserMapper;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.security.TokenUtils;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private final UserOrchestrator userOrchestrator;

    @Autowired
    public UserController(UserService userService, UserDetailsService userDetailsService,
                          AuthenticationManager authenticationManager, TokenUtils tokenUtils,
                          UserOrchestrator userOrchestrator) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.userOrchestrator = userOrchestrator;
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessTokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) throws UsernameNotFoundException {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                loginDTO.getPassword());
        authenticationManager.authenticate(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
        return new ResponseEntity<>(tokenUtils.generateToken(userDetails), HttpStatus.OK);
    }


    @PostMapping(value = "/registration",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registration(@Valid @RequestBody UserDTO userDTO) throws Exception {
        userService.registration(UserMapper.toEntity(userDTO));
        return new ResponseEntity<>("You are registered, now you need to verify your email", HttpStatus.OK);
    }

    @GetMapping(value = "/verify/{token}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> verifiedEmail(@PathVariable("token") String token) throws ResourceNotFoundException {
        User user = userService.verifiedUserEmail(token);
        userOrchestrator.startSaga(user);
        return new ResponseEntity<>("Email verified", HttpStatus.OK);
    }

}
