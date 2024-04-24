package com.project.SNS.controller;

import com.project.SNS.controller.request.PostCommentRequest;
import com.project.SNS.controller.request.PostModifyRequest;
import com.project.SNS.controller.request.PostWriteRequest;
import com.project.SNS.controller.response.PostResponse;
import com.project.SNS.controller.response.Response;
import com.project.SNS.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostWriteRequest request, Authentication authentication) {
        postService.create(authentication.getName(), request.getTitle(), request.getBody());
        return Response.success();
    }
    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        return Response.success(
                PostResponse.fromPost(
                        postService.modify(authentication.getName(), postId, request.getTitle(), request.getBody())));
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication) {
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> myPosts(Pageable pageable, Authentication authentication) {
        return Response.success(postService.myPosts(authentication.getName(), pageable).map(PostResponse::fromPost));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return Response.success();
    }




    @PostMapping("/{postId}/comments")
    public Response<Void> comment(@PathVariable Integer postId, @RequestBody PostCommentRequest request, Authentication authentication) {
        postService.addComment(postId, authentication.getName(), request.getComment());
        return Response.success();
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable Integer postId, Authentication authentication) {
        postService.addLike(postId, authentication.getName());
        return Response.success();
    }
}