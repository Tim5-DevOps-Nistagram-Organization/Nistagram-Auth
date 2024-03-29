package rs.ac.uns.ftn.devops.tim5.nistagramauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);

    Optional<User> findByEmail(String email);
}
