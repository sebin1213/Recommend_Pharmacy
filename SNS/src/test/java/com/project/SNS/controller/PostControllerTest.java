package com.project.SNS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.SNS.controller.request.PostModifyRequest;
import com.project.SNS.controller.request.PostWriteRequest;
import com.project.SNS.exception.ErrorCode;
import com.project.SNS.exception.SimpleSnsApplicationException;
import com.project.SNS.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void 게시글작성() throws Exception {
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostWriteRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithAnonymousUser
    void 게시글_작성할때_로그인상태_아니면_에러발생() throws Exception {
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostWriteRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }



    @Test
    @WithAnonymousUser
    void 게시글_수정할때_로그인상태_아니면_에러발생() throws Exception {
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }


    @Test
    @WithMockUser
    void 게시글수정할때_본인이_작성한_글이_아니면_에러발생() throws Exception {
        doThrow(new SimpleSnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(any(), eq(1), eq("title"), eq("body"));
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 게시글수정할때_수정하려는_글이_없으면_에러발생() throws Exception {
        doThrow(new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(any(), eq(1), eq("title"), eq("body"));
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

}