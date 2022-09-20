package bssm.db.bssmgit.domain.post.web.dto.res;

import bssm.db.bssmgit.domain.post.entity.Post;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostResponseDto {

    private final Long postId;
    private final String title;
    private final String content;
    private final int view;
    private final int likes;
    private final String createMinutesAgo;
    private final List<CategoryResponseDto> postCategories;
    private final UserResponseDto writer;

    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = new UserResponseDto(post.getWriter());
        this.view = post.getView();
        this.likes = post.getLikes().size();
        this.createMinutesAgo = ChronoUnit.MINUTES.between(post.getCreatedAt(), LocalDateTime.now()) + "분전";
        this.postCategories = post.getCategories().stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

}
