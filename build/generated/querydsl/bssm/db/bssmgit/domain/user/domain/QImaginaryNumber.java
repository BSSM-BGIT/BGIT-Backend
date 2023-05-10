package bssm.db.bssmgit.domain.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QImaginaryNumber is a Querydsl query type for ImaginaryNumber
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImaginaryNumber extends EntityPathBase<ImaginaryNumber> {

    private static final long serialVersionUID = -140866875L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QImaginaryNumber imaginaryNumber = new QImaginaryNumber("imaginaryNumber");

    public final bssm.db.bssmgit.domain.post.entity.QBaseTimeEntity _super = new bssm.db.bssmgit.domain.post.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Long> reportedUserId = createNumber("reportedUserId", Long.class);

    public final QUser user;

    public QImaginaryNumber(String variable) {
        this(ImaginaryNumber.class, forVariable(variable), INITS);
    }

    public QImaginaryNumber(Path<? extends ImaginaryNumber> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QImaginaryNumber(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QImaginaryNumber(PathMetadata metadata, PathInits inits) {
        this(ImaginaryNumber.class, metadata, inits);
    }

    public QImaginaryNumber(Class<? extends ImaginaryNumber> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

