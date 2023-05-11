package bssm.db.bssmgit.domain.github.domain.repository;

import bssm.db.bssmgit.domain.github.domain.GitHub;
import bssm.db.bssmgit.domain.github.domain.type.Imaginary;
import bssm.db.bssmgit.domain.github.web.dto.response.GithubResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static bssm.db.bssmgit.domain.github.domain.QGitHub.gitHub;
import static bssm.db.bssmgit.domain.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class CustomGithubRepositoryImpl implements CustomGithubRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GithubResponseDto> getGitHubAndUser() {
        return jpaQueryFactory
                .select(Projections.constructor(GithubResponseDto.class, user, gitHub))
                .from(gitHub)
                .where(gitHub.githubId.isNotNull())
                .innerJoin(user.gitHub)
                .fetchJoin()
                .distinct()
                .orderBy(gitHub.commits.desc())
                .fetch();
    }

    @Override
    public List<GitHub> findGitHubsByImaginary() {
        return jpaQueryFactory
                .selectFrom(gitHub)
                .where(gitHub.imaginary.eq(Imaginary.IMAGINARY_NUMBER))
                .fetch();
    }
}
