package rs.ac.uns.ftn.devops.tim5.nistagramauth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.UniqueFieldUserException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.kafka.saga.UserOrchestrator;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.AgentRegistration;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.Role;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.enums.AgentRegistrationEnum;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.repository.AgentRegistrationRepository;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.AgentService;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.MailService;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.UserService;

import javax.mail.MessagingException;
import java.util.Collection;

@Service
public class AgentServiceImpl implements AgentService {

    private final AgentRegistrationRepository agentRegistrationRepository;
    private final MailService mailService;
    private final UserService userService;
    private final UserOrchestrator userOrchestrator;

    @Autowired
    public AgentServiceImpl(AgentRegistrationRepository agentRegistrationRepository,
                            MailService mailService,
                            UserService userService,
                            UserOrchestrator userOrchestrator) {
        this.agentRegistrationRepository = agentRegistrationRepository;
        this.mailService = mailService;
        this.userService = userService;
        this.userOrchestrator = userOrchestrator;
    }

    @Override
    public AgentRegistration findById(Long id) throws ResourceNotFoundException {
        return agentRegistrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgentRegistration"));
    }

    @Override
    public void addAgentRegistrationRequest(User user)
            throws UniqueFieldUserException, MessagingException {
        if (userService.findByUsername(user.getUsername()) != null) {
            throw new UniqueFieldUserException("Username");
        } else if (userService.findByEmail(user.getEmail()) != null) {
            throw new UniqueFieldUserException("Email");
        }
        user = userService.registration(user, Role.ROLE_AGENT);
        AgentRegistration agentRegistration =
                new AgentRegistration(null, user.getUsername(), user.getEmail(),
                        user.getWebsiteUrl(), AgentRegistrationEnum.REQUESTED);
        agentRegistrationRepository.save(agentRegistration);
    }

    @Override
    public void approveAgentRegistration(Long id) throws ResourceNotFoundException, MessagingException {
        AgentRegistration old = this.findById(id);
        old.setState(AgentRegistrationEnum.ACCEPTED);
        User user = userService.findByEmail(old.getEmail());
        user = userService.agentRegistrationApproval(user);

        // Start saga
        userOrchestrator.startSaga(user);

        // Notify agent
        String subject = "Agent registration approved!";
        String message = "<html><head><meta charset=\"UTF-8\"></head>"
                + "<body><h3>Nistagram app - Agent registration approved!</h3><br>"
                + "<div><p>Welcome to Nistagram</div></body></html>";
        mailService.sendMail(user.getEmail(), subject, message);
        agentRegistrationRepository.save(old);
    }

    @Override
    public void rejectAgentRegistration(Long id) throws ResourceNotFoundException, MessagingException {
        AgentRegistration old = this.findById(id);
        old.setState(AgentRegistrationEnum.REJECTED);
        User user = userService.findByEmail(old.getEmail());
        String subject = "Agent registration rejected!";
        String message = "<html><head><meta charset=\"UTF-8\"></head>"
                + "<body><h3>Nistagram app - Agent registration rejected!</h3><br>"
                + "<div><p>You can try again </div></body></html>";
        mailService.sendMail(user.getEmail(), subject, message);
        agentRegistrationRepository.save(old);
    }

    @Override
    public Collection<AgentRegistration> findAllAgentRegistrationRequests() {
        return agentRegistrationRepository.findAllByState(AgentRegistrationEnum.REQUESTED);
    }
}
