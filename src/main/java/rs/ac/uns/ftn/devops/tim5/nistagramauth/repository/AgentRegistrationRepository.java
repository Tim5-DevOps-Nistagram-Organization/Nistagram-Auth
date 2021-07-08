package rs.ac.uns.ftn.devops.tim5.nistagramauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.AgentRegistration;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.enums.AgentRegistrationEnum;

import java.util.Collection;

@Repository
public interface AgentRegistrationRepository extends JpaRepository<AgentRegistration, Long> {

    @Query("SELECT r FROM AgentRegistration r WHERE r.state = ?1")
    Collection<AgentRegistration> findAllByState(AgentRegistrationEnum state);


}
