package bssm.db.bssmgit.domain.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1396740776L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final bssm.db.bssmgit.domain.boj.domain.QBoj boj;

    public final StringPath bsmToken = createString("bsmToken");

    public final ListPath<bssm.db.bssmgit.domain.post.entity.Category, bssm.db.bssmgit.domain.post.entity.QCategory> categories = this.<bssm.db.bssmgit.domain.post.entity.Category, bssm.db.bssmgit.domain.post.entity.QCategory>createList("categories", bssm.db.bssmgit.domain.post.entity.Category.class, bssm.db.bssmgit.domain.post.entity.QCategory.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final bssm.db.bssmgit.domain.github.domain.QGitHub gitHub;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<ImaginaryNumber, QImaginaryNumber> imaginaryNumbers = this.<ImaginaryNumber, QImaginaryNumber>createList("imaginaryNumbers", ImaginaryNumber.class, QImaginaryNumber.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final ListPath<bssm.db.bssmgit.domain.post.entity.Post, bssm.db.bssmgit.domain.post.entity.QPost> posts = this.<bssm.db.bssmgit.domain.post.entity.Post, bssm.db.bssmgit.domain.post.entity.QPost>createList("posts", bssm.db.bssmgit.domain.post.entity.Post.class, bssm.db.bssmgit.domain.post.entity.QPost.class, PathInits.DIRECT2);

    public final EnumPath<bssm.db.bssmgit.domain.user.domain.type.Role> role = createEnum("role", bssm.db.bssmgit.domain.user.domain.type.Role.class);

    public final NumberPath<Integer> studentClassNo = createNumber("studentClassNo", Integer.class);

    public final NumberPath<Integer> studentGrade = createNumber("studentGrade", Integer.class);

    public final NumberPath<Integer> studentNo = createNumber("studentNo", Integer.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.boj = inits.isInitialized("boj") ? new bssm.db.bssmgit.domain.boj.domain.QBoj(forProperty("boj")) : null;
        this.gitHub = inits.isInitialized("gitHub") ? new bssm.db.bssmgit.domain.github.domain.QGitHub(forProperty("gitHub")) : null;
    }

}

