package bssm.db.bssmgit.domain.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.githubId like %:githubId%")
    List<User> findByGithubId(@Param("githubId") String githubId);

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);
}
