package com.project.SNS.service;

import com.project.SNS.exception.ErrorCode;
import com.project.SNS.exception.SimpleSnsApplicationException;
import com.project.SNS.fixture.TestUserEntity;
import com.project.SNS.model.entity.PostEntity;
import com.project.SNS.model.entity.UserEntity;
import com.project.SNS.repository.PostEntityRepository;
import com.project.SNS.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @MockBean
    UserEntityRepository userEntityRepository;

    @MockBean
    PostEntityRepository postEntityRepository;
    @Test
    @WithMockUser
    void 게시글작성() throws Exception {
        String userName = "name";
        String password = "password";
        String title = "title";
        String body = "body";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(TestUserEntity.get(userName, password)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        Assertions.assertDoesNotThrow(() -> postService.create(userName, title, body));
    }


    @Test
    @WithAnonymousUser
    void 게시글_작성할때_작성자정보가_없으면_에러발생() throws Exception {
        String userName = "name";
        String title = "title";
        String body = "body";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class, () -> postService.create(userName, title, body));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
    
    @Test
    @WithMockUser
    void 게시글수정할때_작성자가없으면_에러발생() throws Exception {
        Integer postId = 1;
        String userName = "name";
        String title = "title";
        String body = "body";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(mock(PostEntity.class)));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class, () -> postService.modify(userName, postId, title, body));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    @WithMockUser
    void 게시글수정할때_본인이_작성한_글이_아니면_에러발생() throws Exception {
        PostEntity mockPostEntity = mock(PostEntity.class);
        UserEntity mockUserEntity = mock(UserEntity.class);

        Integer postId = 1;
        String userName = "name";
        String title = "title";
        String body = "body";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(mockPostEntity));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mockUserEntity));
        when(mockPostEntity.getUser()).thenReturn(mock(UserEntity.class));
        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class, () -> postService.modify(userName, postId, title, body));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @WithMockUser
    void 게시글수정할때_수정하려는_글이_없으면_에러발생() throws Exception {
        Integer postId = 1;
        String userName = "name";
        String title = "title";
        String body = "body";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());
        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class, () -> postService.modify(userName, postId, title, body));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    @WithAnonymousUser
    void 삭제할_게시글이_없으면_에러발생() throws Exception {
        Integer postId = 1;
        String userName = "name";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());
        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class, () -> postService.delete(userName, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @WithMockUser
    void 게시글삭제할때_본인이_작성한_글이_아니면_에러발생() throws Exception {
        PostEntity mockPostEntity = mock(PostEntity.class);
        UserEntity mockUserEntity = mock(UserEntity.class);

        Integer postId = 1;
        String userName = "name";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(mockPostEntity));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mockUserEntity));
        when(mockPostEntity.getUser()).thenReturn(mock(UserEntity.class));
        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class, () -> postService.delete(userName, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @WithMockUser
    void 게시글삭제할때_작성자가_정보가_없으면_에러발생() throws Exception {
        Integer postId = 1;
        String userName = "name";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(mock(PostEntity.class)));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class, () -> postService.delete(userName, postId));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}