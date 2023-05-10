package bssm.db.bssmgit.domain.github.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGitHub is a Querydsl query type for GitHub
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGitHub extends EntityPathBase<GitHub> {

    private static final long serialVersionUID = -1024784392L;

    public static final QGitHub gitHub = new QGitHub("gitHub");

    public final NumberPath<Integer> commits = createNumber("commits", Integer.class);

    public final StringPath githubId = createString("githubId");

    public final StringPath githubImg = createString("githubImg");

    public final StringPath githubMsg = createString("githubMsg");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<bssm.db.bssmgit.domain.github.domain.type.Imaginary> imaginary = createEnum("imaginary", bssm.db.bssmgit.domain.github.domain.type.Imaginary.class);

    public final NumberPath<Integer> votingCount = createNumber("votingCount", Integer.class);

    public QGitHub(String variable) {
        super(GitHub.class, forVariable(variable));
    }

    public QGitHub(Path<? extends GitHub> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGitHub(PathMetadata metadata) {
        super(GitHub.class, metadata);
    }

}

