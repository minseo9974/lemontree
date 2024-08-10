package com.minseo.lemontree.repository;

import com.minseo.lemontree.entity.AmountUsed;
import com.minseo.lemontree.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountUsedRepository extends JpaRepository<AmountUsed, Long> {

    AmountUsed findAmountUsedByMember(Member member);
}