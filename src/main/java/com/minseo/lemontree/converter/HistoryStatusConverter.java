package com.minseo.lemontree.converter;

import com.minseo.lemontree.domain.HistoryStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * class: HistoryStatusConverter.
 * 파라미터로 받는 String 상태값과 DB에 저장하는 tinyint 값을 교환해주는 컨버터 클래스입니다.
 *
 * @author devminseo
 * @version 8/10/24
 */
@Converter
public class HistoryStatusConverter implements AttributeConverter<HistoryStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(HistoryStatus historyStatus) {
        if (historyStatus == null) {
            return null;
        }
        return historyStatus.getDbValue();
    }

    @Override
    public HistoryStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return HistoryStatus.fromDbValue(dbData);
    }
}
