package com.minseo.lemontree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.minseo.lemontree.dto.request.PaymentCancelRequest;
import com.minseo.lemontree.dto.request.PaymentRequest;
import com.minseo.lemontree.dummy.MemberDummy;
import com.minseo.lemontree.entity.AmountUsed;
import com.minseo.lemontree.entity.Member;
import com.minseo.lemontree.repository.AmountUsedRepository;
import com.minseo.lemontree.repository.HistoryRepository;
import com.minseo.lemontree.repository.MemberRepository;
import com.minseo.lemontree.service.PaymentService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * class: ConcurrencyTest.
 * 동시성 제어 테스트 입니다.
 *
 * @author devminseo
 * @version 8/15/24
 */

@SpringBootTest
@ActiveProfiles("test") //H2 활성화
class ConcurrencyTest {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AmountUsedRepository amountUsedRepository;
    @Autowired
    private HistoryRepository historyRepository;
    private Member member;
    private int executeNumber = 1000; // 동시 요청 개수
    private PaymentRequest paymentRequestDto;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();
        memberRepository.save(member);
        AmountUsed amountUsed = MemberDummy.amountUsedDummy(member);
        amountUsedRepository.save(amountUsed);

        paymentRequestDto = new PaymentRequest();
        ReflectionTestUtils.setField(paymentRequestDto, "memberId", member.getMemberId());
        ReflectionTestUtils.setField(paymentRequestDto, "productPrice", 1000L);
        ReflectionTestUtils.setField(paymentRequestDto, "orderId", 10L);
    }

    @AfterEach
    void after() {
        historyRepository.deleteAll();
        amountUsedRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("결제 Lock Test - 1000번 동시 결제")
    void paymentWithLock() throws Exception {

        final ExecutorService executorService = Executors.newFixedThreadPool(50); // 스레드 풀 생성
        final CyclicBarrier barrier = new CyclicBarrier(50); // 스레드가 준비될때까지 대기하다 동시에 실행
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber); // 총 작업의 수

        AtomicInteger successCount = new AtomicInteger(); // 원자성 보장
        AtomicInteger failCount = new AtomicInteger(); // 일관된 결과 보장

        for (int i = 0; i < executeNumber; i++) {
            executorService.execute(() -> {
                try {
                    barrier.await(); // 모든 쓰레드가 시작하게 기다림
                    paymentService.payment(paymentRequestDto);
                    successCount.getAndIncrement();
                } catch (Exception e) {
                    failCount.getAndIncrement();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        assertEquals(1, successCount.get());
        assertEquals(999, failCount.get());

        assertEquals(executeNumber, Math.addExact(successCount.get(), failCount.get()));
    }

    @Test
    @DisplayName("결제 취소 Lock Test - 1000번 동시 결제 취소")
    void paymentCancelWithLock() throws Exception {
        paymentService.payment(paymentRequestDto);

        PaymentCancelRequest cancelRequest = new PaymentCancelRequest();
        ReflectionTestUtils.setField(cancelRequest, "memberId", member.getMemberId());
        ReflectionTestUtils.setField(cancelRequest, "orderId", 10L);

        final ExecutorService executorService = Executors.newFixedThreadPool(50);
        final CyclicBarrier barrier = new CyclicBarrier(50);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < executeNumber; i++) {
            executorService.execute(() -> {
                try {
                    barrier.await(); // 모든 쓰레드가 시작하게 기다림
                    paymentService.paymentCancel(cancelRequest);
                    successCount.getAndIncrement();
                } catch (Exception e) {
                    failCount.getAndIncrement();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        assertEquals(1, successCount.get());
        assertEquals(999, failCount.get());

        assertEquals(executeNumber, Math.addExact(successCount.get(), failCount.get()));
    }
}
