package bssm.db.bssmgit.domain.post.service;

import bssm.db.bssmgit.domain.post.entity.repository.PostRepository;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

}
