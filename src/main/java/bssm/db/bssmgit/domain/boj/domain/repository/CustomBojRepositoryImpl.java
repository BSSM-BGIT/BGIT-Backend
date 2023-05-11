package bssm.db.bssmgit.domain.boj.domain.repository;

import bssm.db.bssmgit.domain.boj.web.dto.response.BojResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static bssm.db.bssmgit.domain.boj.domain.QBoj.boj;
import static bssm.db.bssmgit.domain.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class CustomBojRepositoryImpl implements CustomBojRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BojResponseDto> findAllUserBojDesc() {
        return jpaQueryFactory
                .select(Projections.constructor(BojResponseDto.class, user, boj))
                .from(boj)
                .innerJoin(user)
                .fetchJoin()
                .where(boj.bojId.isNotNull())
                .distinct()
                .orderBy(boj.rating.desc())
                .fetch();
    }
}
