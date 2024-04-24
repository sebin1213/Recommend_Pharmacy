package com.project.SNS.controller;

import com.project.SNS.controller.request.PostCommentRequest;
import com.project.SNS.controller.response.Response;
import com.project.SNS.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@AllArgsConstructor
public class CommentController {
    private final PostService postService;

    @PostMapping("/{postId}")
    public Response<Void> comment(@PathVariable Integer postId, @RequestBody PostCommentRequest request, Authentication authentication) {
        postService.addComment(postId, authentication.getName(), request.getComment());
        return Response.success();
    }
}
