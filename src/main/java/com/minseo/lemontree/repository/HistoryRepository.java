package com.minseo.lemontree.repository;

import com.minseo.lemontree.entity.History;
import com.minseo.lemontree.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HistoryRepository extends JpaRepository<History, Long> {

    boolean existsByMemberAndOrderId(Member member, Long orderId);
}