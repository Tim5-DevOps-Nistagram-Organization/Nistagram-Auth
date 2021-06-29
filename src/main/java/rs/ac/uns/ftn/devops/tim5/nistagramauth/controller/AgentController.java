package rs.ac.uns.ftn.devops.tim5.nistagramauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.AgentRequestDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.UniqueFieldUserException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.mapper.AgentMapper;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.AgentRegistration;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.AgentService;


import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private AgentService agentService;

    @Autowired
    public AgentController(AgentService agentService) {
        this.agentService =agentService;
    }

    @PostMapping(value = "/registration",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registration(@Valid @RequestBody AgentRequestDTO agentRequestDTO)
            throws UniqueFieldUserException, MessagingException {
        agentService.addAgentRegistrationRequest(AgentMapper.toEntity(agentRequestDTO));
        return new ResponseEntity<>("You successfully send registration request", HttpStatus.OK);
    }

    @PutMapping(path="/approve/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> approve(@PathVariable Long requestId)
            throws ResourceNotFoundException, MessagingException {
        agentService.approveAgentRegistration(requestId);
        return new ResponseEntity<>("Request is approved.", HttpStatus.OK);
    }

    @PutMapping(path="/reject/{requestId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> reject(@PathVariable Long requestId)
            throws ResourceNotFoundException, MessagingException {
        agentService.rejectAgentRegistration(requestId);
        return new ResponseEntity<>("Request is successfully rejected.", HttpStatus.OK);
    }

    @GetMapping(path="/requests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Collection<AgentRegistration>> getAllRequest() {
        return new ResponseEntity<>(agentService.findAllAgentRegistrationRequests(), HttpStatus.OK);
    }

}
