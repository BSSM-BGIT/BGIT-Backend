package bssm.db.bssmgit.domain.post.service;

import bssm.db.bssmgit.domain.post.entity.Category;
import bssm.db.bssmgit.domain.post.entity.Post;
import bssm.db.bssmgit.domain.post.entity.repository.CategoryRepository;
import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final UserFacade userFacade;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void createCategory(Post post, String category) {
        User user = userFacade.getCurrentUser();

        Category ca = categoryRepository.save(
                Category.builder()
                        .name(category)
                        .build());

        ca.confirmManagerPost(post);
        ca.confirmUser(user);

    }

    @Transactional
    public void removeAll(Long postId) {
        categoryRepository.deleteAllCategories(postId);
    }
}
