package bssm.db.bssmgit.domain.post.web.api;

import bssm.db.bssmgit.domain.post.service.PostService;
import bssm.db.bssmgit.domain.post.web.dto.req.PostCreateRequestDto;
import bssm.db.bssmgit.domain.post.web.dto.res.PostResponseDto;
import bssm.db.bssmgit.global.generic.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostApiController {

    private final PostService postService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Long create(@RequestBody PostCreateRequestDto request) {
        return postService.createPost(request);
    }

    @GetMapping("/find/detail/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDto detail(@PathVariable Long id) {
        return postService.detail(id);
    }

    @GetMapping("/find/title")
    @ResponseStatus(HttpStatus.OK)
    public Result<List<PostResponseDto>> findByTitle(@RequestParam String title,
                                                     @PageableDefault(size = 10)
                              Pageable pageable) {
        List<PostResponseDto> post = postService.findByTitle(title, pageable);

        return new Result<>(post.size(), post);
    }

    @GetMapping("/find/popular")
    @ResponseStatus(HttpStatus.OK)
    public Result<List<PostResponseDto>> popularPosts(
            @PageableDefault(size = 10)
            Pageable pageable) {
        List<PostResponseDto> post = postService.popularPosts(pageable);

        return new Result<>(post.size(), post);
    }

    @GetMapping("/find/all")
    @ResponseStatus(HttpStatus.OK)
    public Result<List<PostResponseDto>> allPosts(
            @PageableDefault(size = 10)
            Pageable pageable) {

        List<PostResponseDto> response = postService.allPosts(pageable);
        return new Result<>(response.size(), response);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDto updatePost(
            @PathVariable Long id,
            @RequestBody PostCreateRequestDto request) {
        return postService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

}