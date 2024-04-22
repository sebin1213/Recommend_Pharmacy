package com.project.SNS.service;

import com.project.SNS.exception.ErrorCode;
import com.project.SNS.exception.SimpleSnsApplicationException;
import com.project.SNS.model.Post;
import com.project.SNS.model.entity.PostEntity;
import com.project.SNS.model.entity.UserEntity;
import com.project.SNS.repository.PostEntityRepository;
import com.project.SNS.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;

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
//        likeEntityRepository.deleteAllByPost(postEntity);
//        commentEntityRepository.deleteAllByPost(postEntity);
        postEntityRepository.delete(postEntity);
    }



}
