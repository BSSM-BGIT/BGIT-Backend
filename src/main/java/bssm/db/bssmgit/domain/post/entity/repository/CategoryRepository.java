package bssm.db.bssmgit.domain.post.entity.repository;

import bssm.db.bssmgit.domain.post.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Modifying
    @Query("delete from Category c where c.post.id = :postId")
    void deleteAllCategories(@Param("postId") Long postId);

}