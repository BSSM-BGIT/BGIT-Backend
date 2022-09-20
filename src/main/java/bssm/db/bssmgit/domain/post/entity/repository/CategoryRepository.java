package bssm.db.bssmgit.domain.post.entity.repository;

import bssm.db.bssmgit.domain.post.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
