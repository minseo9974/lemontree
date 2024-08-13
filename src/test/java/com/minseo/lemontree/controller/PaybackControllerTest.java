package com.minseo.lemontree.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minseo.lemontree.dto.request.PaybackCancelRequest;
import com.minseo.lemontree.dto.request.PaybackRequest;
import com.minseo.lemontree.service.PaybackService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * class: PaybackControllerTest.
 * 페이백 / 페이백 취소 컨트롤러 테스트 입니다.
 *
 * @author devminseo
 * @version 8/13/24
 */
@WebMvcTest(PaybackController.class)
class PaybackControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PaybackService paybackService;

    ObjectMapper obj = new ObjectMapper();

    @Test
    @DisplayName("페이백 - 성공")
    void paybackRequestSuccess() throws Exception {
        PaybackRequest paybackRequest = new PaybackRequest();
        ReflectionTestUtils.setField(paybackRequest, "memberId", 1L);
        ReflectionTestUtils.setField(paybackRequest, "orderId", 10L);

        mockMvc.perform(post("/payback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "lemontree")
                        .content(obj.writeValueAsString(paybackRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("1님의 Payback Success!"));

        verify(paybackService, times(1)).payback(any(PaybackRequest.class));
    }

    @Test
    @DisplayName("페이백 취소 - 성공")
    void paybackCancelRequestSuccess() throws Exception {
        PaybackCancelRequest cancelRequest = new PaybackCancelRequest();
        ReflectionTestUtils.setField(cancelRequest, "memberId", 1L);
        ReflectionTestUtils.setField(cancelRequest, "orderId", 10L);

        mockMvc.perform(post("/payback/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "lemontree")
                        .content(obj.writeValueAsString(cancelRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("1님의 Payback Cancel Success!"));

        verify(paybackService, times(1)).paybackCancel(any(PaybackCancelRequest.class));
    }

    @Test
    @DisplayName("페이백 - validation 실패")
    void paybackRequestValidationFail() throws Exception {
        PaybackRequest paybackRequest = new PaybackRequest();
        ReflectionTestUtils.setField(paybackRequest, "memberId", null);
        ReflectionTestUtils.setField(paybackRequest, "orderId", null);

        mockMvc.perform(post("/payback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "lemontree")
                        .content(obj.writeValueAsString(paybackRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class));
    }

    @Test
    @DisplayName("페이백 취소 - validation 실패")
    void paybackCancelRequestValidationFail() throws Exception {
        PaybackCancelRequest cancelRequest = new PaybackCancelRequest();
        ReflectionTestUtils.setField(cancelRequest, "memberId", null);
        ReflectionTestUtils.setField(cancelRequest, "orderId", null);

        mockMvc.perform(post("/payback/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "lemontree")
                        .content(obj.writeValueAsString(cancelRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class));
    }
}