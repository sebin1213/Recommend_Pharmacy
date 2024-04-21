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
    public Post modify(Integer userId, Integer postId, String title, String body) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%d", postId)));
        if (!Objects.equals(postEntity.getUser().getId(), userId)) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("사용자 %s 가 권한이 없는 게시물 %d 에 접근하였습니다.", userId, postId));
        }
        postEntity.modify(title, body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }
}
