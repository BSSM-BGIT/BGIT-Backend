package bssm.db.bssmgit.domain.user.repository;

import bssm.db.bssmgit.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.githubId = :githubId")
    Optional<User> findByGithubId(@Param("githubId") String githubId);

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    @Query("select u from User u order by u.commits DESC")
    Page<User> findAll(Pageable pageable);
}
