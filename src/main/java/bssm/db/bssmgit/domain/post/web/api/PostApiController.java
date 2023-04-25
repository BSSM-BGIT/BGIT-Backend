package bssm.db.bssmgit.domain.post.web.api;

import bssm.db.bssmgit.domain.post.service.PostService;
import bssm.db.bssmgit.domain.post.web.dto.req.PostCreateRequestDto;
import bssm.db.bssmgit.domain.post.web.dto.res.PostResponseDto;
import bssm.db.bssmgit.global.generic.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostApiController {

    private final PostService postService;

    @PostMapping("/create")
    public Long create(@RequestBody PostCreateRequestDto request) {
        return postService.createPost(request);
    }

    @GetMapping("/find/detail/{id}")
    public PostResponseDto detail(@PathVariable Long id) {
        return postService.detail(id);
    }

    @GetMapping("/find/title")
    public Result<List<PostResponseDto>> findByTitle(@RequestParam String title,
                                                     @PageableDefault(size = 10)
                              Pageable pageable) {
        List<PostResponseDto> post = postService.findByTitle(title, pageable);

        return new Result<>(post.size(), post);
    }

    @GetMapping("/find/popular")
    public Result<List<PostResponseDto>> popularPosts(
            @PageableDefault(size = 10)
            Pageable pageable) {
        List<PostResponseDto> post = postService.popularPosts(pageable);

        return new Result<>(post.size(), post);
    }

    @GetMapping("/find/all")
    public Result<List<PostResponseDto>> allPosts(
            @PageableDefault(size = 10)
            Pageable pageable) {

        List<PostResponseDto> response = postService.allPosts(pageable);
        return new Result<>(response.size(), response);
    }

    @PutMapping("/update/{id}")
    public PostResponseDto updatePost(
            @PathVariable Long id,
            @RequestBody PostCreateRequestDto request) {
        return postService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

}
