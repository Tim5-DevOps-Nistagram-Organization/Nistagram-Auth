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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.AccessTokenDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.LoginDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.security.TokenUtils;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;

    @Autowired
    public UserController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager,
                          TokenUtils tokenUtils) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
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

}
