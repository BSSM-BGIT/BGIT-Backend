package bssm.db.bssmgit.domain.github.domain.repository;

import bssm.db.bssmgit.domain.github.domain.GitHub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GitHubRepository extends JpaRepository<GitHub, Long> {

    @Query("select g from GitHub g " +
            "where g.imaginary = " +
            "bssm.db.bssmgit.domain.github.domain.type.Imaginary.IMAGINARY_NUMBER")
    List<GitHub> findGitHubsByImaginary();
}
