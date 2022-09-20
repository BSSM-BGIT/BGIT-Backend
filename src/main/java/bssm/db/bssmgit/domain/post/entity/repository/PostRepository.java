package bssm.db.bssmgit.domain.post.entity.repository;

import bssm.db.bssmgit.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"writer"})
    @Query("select m from Post m where m.title = :title order by m.likes.size desc, m.createdAt desc")
    Page<Post> findByTitle(@Param("title") String title, Pageable pageable);

    @NotNull
    @EntityGraph(attributePaths = {"writer"})
    @Query("select m from Post m order by m.likes.size desc, m.createdAt desc")
    Page<Post> findAllByOrderByLikesDesc(@NotNull Pageable pageable);

    @NotNull
    @Query("select m from Post m order by m.createdAt desc")
    Page<Post> findAll(@NotNull Pageable pageable);

}
