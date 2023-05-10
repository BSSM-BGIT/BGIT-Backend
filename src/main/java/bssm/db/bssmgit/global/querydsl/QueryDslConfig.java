package bssm.db.bssmgit.global.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;

import javax.persistence.EntityManager;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryDslConfig {

    private final EntityManager em;

    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(em);
    }
}
