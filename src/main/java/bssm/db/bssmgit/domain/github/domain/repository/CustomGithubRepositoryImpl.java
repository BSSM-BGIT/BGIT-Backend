package bssm.db.bssmgit.domain.github.domain.repository;

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
                .innerJoin(user.gitHub)
                .fetchJoin()
                .fetch();
    }
}
