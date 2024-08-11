package com.minseo.lemontree.repository;

import com.minseo.lemontree.domain.HistoryType;
import com.minseo.lemontree.entity.History;
import com.minseo.lemontree.entity.Member;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;


public interface HistoryRepository extends JpaRepository<History, Long> {

    boolean existsByMemberAndOrderIdAndHistoryType(Member member, Long orderId, HistoryType historyType);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
    Optional<History> findWithPessimisticLockByMemberAndOrderIdAndHistoryType(Member member, Long orderId,
                                                                              HistoryType historyType);

    Optional<History> findByMemberAndOrderIdAndHistoryType(Member member, Long orderId, HistoryType historyType);
}
