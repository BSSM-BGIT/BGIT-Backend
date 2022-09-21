package bssm.db.bssmgit.domain.post.web.dto.req;

import bssm.db.bssmgit.domain.post.entity.Post;
import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostCreateRequestDto {

    private final String title;
    private final String content;
    private final User writer;
    private final List<String> categories;

    @Builder
    public PostCreateRequestDto(String title, String content, User writer, List<String> categories) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.categories = categories;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .build();
    }
}
