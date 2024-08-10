package com.minseo.lemontree.converter;

import com.minseo.lemontree.domain.MemberStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * class: MemberStatusConverter.
 * 파라미터로 받는 String 상태값과 DB에 저장하는 tinyint 값을 교환해주는 컨버터 클래스입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */
@Converter
public class MemberStatusConverter implements AttributeConverter<MemberStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MemberStatus memberStatus) {
        if (memberStatus == null) {
            return null;
        }

        return memberStatus.getDbValue();
    }

    @Override
    public MemberStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return MemberStatus.fromDbValue(dbData);
    }
}
