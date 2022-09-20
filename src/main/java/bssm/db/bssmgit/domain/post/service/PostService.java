package bssm.db.bssmgit.domain.post.service;

import bssm.db.bssmgit.domain.post.entity.Post;
import bssm.db.bssmgit.domain.post.entity.repository.PostRepository;
import bssm.db.bssmgit.domain.post.web.dto.req.PostCreateRequestDto;
import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.global.config.security.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

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
}
