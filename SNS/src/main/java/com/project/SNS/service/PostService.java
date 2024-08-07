package com.project.SNS.service;

import com.project.SNS.exception.ErrorCode;
import com.project.SNS.exception.SimpleSnsApplicationException;
import com.project.SNS.model.Comment;
import com.project.SNS.model.NotificationType;
import com.project.SNS.model.Post;
import com.project.SNS.model.entity.*;
import com.project.SNS.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final NotificationEntityRepository notificationEntityRepository;


    @Transactional
    public void create(String userName, String title, String body) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s", userName)));
        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postEntityRepository.save(postEntity);
    }

    @Transactional
    public Post modify(String userName, Integer postId, String title, String body) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));
        if (postEntity.getUser() != userEntity) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userName, postId));
        }

        postEntity.modify(title, body);

        return Post.fromEntity(postEntityRepository.save(postEntity));
    }


    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> myPosts(String userName, Pageable pageable) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));
        return postEntityRepository.findAllByUserId(userEntity.getId(), pageable).map(Post::fromEntity);
    }

    @Transactional
    public void delete(String userName, Integer postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));

        if (postEntity.getUser() != userEntity) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userEntity.getId(), postId));
        }
        // TOBO: 추후 추가예정
        likeEntityRepository.deleteAllByPost(postEntity);
        commentEntityRepository.deleteAllByPost(postEntity);
        postEntityRepository.delete(postEntity);
    }

    @Transactional
    public void addComment(Integer postId, String userName, String comment) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));

        commentEntityRepository.save(CommentEntity.of(comment, postEntity, userEntity));
        notificationEntityRepository.save(NotificationEntity.of(NotificationType.NEW_COMMENT_ON_POST, userEntity.getId(), postId, postEntity.getUser()));
    }

    @Transactional
    public void addLike(Integer postId, String userName) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));

        // TOBO: like 취소
        likeEntityRepository.save(LikeEntity.of(postEntity, userEntity));
        notificationEntityRepository.save(NotificationEntity.of(NotificationType.NEW_LIKE_ON_POST, userEntity.getId(), postId, postEntity.getUser()));
    }

    public Integer getLikeCount(Integer postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        List<LikeEntity> likes = likeEntityRepository.findAllByPost(postEntity);
        return likes.size();
    }

    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }
}
