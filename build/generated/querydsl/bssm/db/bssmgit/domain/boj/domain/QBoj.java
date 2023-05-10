package bssm.db.bssmgit.domain.boj.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoj is a Querydsl query type for Boj
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoj extends EntityPathBase<Boj> {

    private static final long serialVersionUID = -570922694L;

    public static final QBoj boj = new QBoj("boj");

    public final StringPath bojAuthId = createString("bojAuthId");

    public final StringPath bojBio = createString("bojBio");

    public final StringPath bojId = createString("bojId");

    public final StringPath bojImg = createString("bojImg");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> maxStreak = createNumber("maxStreak", Integer.class);

    public final StringPath randomCode = createString("randomCode");

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public final NumberPath<Integer> solvedCount = createNumber("solvedCount", Integer.class);

    public final NumberPath<Integer> tier = createNumber("tier", Integer.class);

    public QBoj(String variable) {
        super(Boj.class, forVariable(variable));
    }

    public QBoj(Path<? extends Boj> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoj(PathMetadata metadata) {
        super(Boj.class, metadata);
    }

}

