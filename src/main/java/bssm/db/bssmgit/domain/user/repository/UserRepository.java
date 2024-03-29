package bssm.db.bssmgit.domain.user.repository;

import bssm.db.bssmgit.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.githubId = :githubId")
    Optional<User> findByGithubId(@Param("githubId") String githubId);

    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("delete from User u where u.email = :email")
    void deleteByEmail(String email);

    @Query("select u from User u order by u.commits DESC")
    List<User> findGitAll();

    @Query("select u from User u order by u.rating DESC")
    List<User> findBojAll();

    @Query("select u from User u " +
            "where u.imaginary = " +
            "bssm.db.bssmgit.domain.user.domain.type.Imaginary.IMAGINARY_NUMBER")
    List<User> findByUserImaginaryUser();

    void deleteByGithubId(String githubId);

}
