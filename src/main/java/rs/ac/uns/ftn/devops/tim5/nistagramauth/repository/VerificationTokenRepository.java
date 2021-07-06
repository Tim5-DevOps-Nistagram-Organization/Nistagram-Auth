package rs.ac.uns.ftn.devops.tim5.nistagramauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.VerificationToken;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    @Query("SELECT r FROM VerificationToken r WHERE r.user.username = ?1")
    Optional<VerificationToken> findByUsername(String username);
}
