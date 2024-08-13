package com.minseo.lemontree.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minseo.lemontree.dto.request.PaymentCancelRequest;
import com.minseo.lemontree.dto.request.PaymentRequest;
import com.minseo.lemontree.service.PaymentService;
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
 * class: PaymentControllerTest.
 * 결제/취소 컨트롤러 테스트 입니다.
 *
 * @author devminseo
 * @version 8/12/24
 */
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PaymentService paymentService;

    ObjectMapper obj = new ObjectMapper();

    @Test
    @DisplayName("결제 - 성공")
    void paymentRequestSuccess() throws Exception {
        PaymentRequest paymentRequestDto = new PaymentRequest();
        ReflectionTestUtils.setField(paymentRequestDto, "memberId", 1L);
        ReflectionTestUtils.setField(paymentRequestDto, "productPrice", 1000L);
        ReflectionTestUtils.setField(paymentRequestDto, "orderId", 10L);

        mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "lemontree")
                        .content(obj.writeValueAsString(paymentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("1님의 Payment Success!"));

        verify(paymentService, times(1)).payment(any(PaymentRequest.class));
    }

    @Test
    @DisplayName("결제 취소 - 성공")
    void paymentCancelRequestSuccess() throws Exception{
        PaymentCancelRequest cancelRequest = new PaymentCancelRequest();
        ReflectionTestUtils.setField(cancelRequest, "memberId", 1L);
        ReflectionTestUtils.setField(cancelRequest, "orderId", 10L);

        mockMvc.perform(post("/payment/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "lemontree")
                        .content(obj.writeValueAsString(cancelRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("1님의 Payment Cancel Success!"));

        verify(paymentService, times(1)).paymentCancel(any(PaymentCancelRequest.class));
    }

    @Test
    @DisplayName("결제 요청 - validation 실패")
    void paymentRequestValidationFail()throws Exception {
        PaymentRequest paymentRequestDto = new PaymentRequest();
        ReflectionTestUtils.setField(paymentRequestDto, "memberId", null);
        ReflectionTestUtils.setField(paymentRequestDto, "productPrice", null);
        ReflectionTestUtils.setField(paymentRequestDto, "orderId", null);

        mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "lemontree")
                        .content(obj.writeValueAsString(paymentRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class));
    }

    @Test
    @DisplayName("결제 취소 요청 - validation 실패")
    void paymentCancelRequestValidationFail() throws Exception{
        PaymentCancelRequest cancelRequest = new PaymentCancelRequest();
        ReflectionTestUtils.setField(cancelRequest, "memberId", null);
        ReflectionTestUtils.setField(cancelRequest, "orderId", null);

        mockMvc.perform(post("/payment/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "lemontree")
                        .content(obj.writeValueAsString(cancelRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class));
    }
}