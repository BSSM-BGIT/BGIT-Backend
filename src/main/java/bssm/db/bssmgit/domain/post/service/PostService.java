package bssm.db.bssmgit.domain.post.service;

import bssm.db.bssmgit.domain.post.entity.Post;
import bssm.db.bssmgit.domain.post.entity.repository.PostRepository;
import bssm.db.bssmgit.domain.post.web.dto.req.PostCreateRequestDto;
import bssm.db.bssmgit.domain.post.web.dto.res.PostResponseDto;
import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.global.config.security.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CategoryService categoryService;

    @Transactional
    public Long createPost(PostCreateRequestDto request) {
        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        Post post = postRepository.save(request.toEntity());
        post.confirmWriter(user);

        request.getCategories()
                .forEach(c -> categoryService.createCategory(post, c));

        return post.getId();
    }

    @Transactional
    public PostResponseDto detail(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));

        post.upView();
        return new PostResponseDto(post);
    }

    public List<PostResponseDto> findByTitle(String title, Pageable pageable) {
        return postRepository.findByTitle(title, pageable)
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<PostResponseDto> popularPosts(Pageable pageable) {
        return postRepository.findAllByOrderByLikesDesc(pageable)
                .stream()
                // 최근 일주일 인기 게시글
                .filter(p -> ChronoUnit.MINUTES.between(p.getCreatedAt(), LocalDateTime.now()) < 10080)
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 최근에 올라온 게시글 순서
    public List<PostResponseDto> allPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDto update(Long id, PostCreateRequestDto request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));

        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        if (!Objects.equals(post.getWriter().getId(), user.getId())) {
            throw new CustomException(ErrorCode.DONT_ACCESS_OTHER);
        }

        post.update(request.getTitle(), request.getContent());

        // PostCreateRequestDto 에 기존에 있던 카테고리들 + 새로운 카테고리 전부다 넣어야함
        if (!request.getCategories().isEmpty()) {
            categoryService.removeAll(post.getId());
            request.getCategories()
                    .forEach(c -> categoryService.createCategory(post, c));
        }

        return new PostResponseDto(post);
    }
}
