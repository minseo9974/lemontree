package com.minseo.lemontree.converter;

import com.minseo.lemontree.domain.HistoryType;
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
public class HistoryTypeConverter implements AttributeConverter<HistoryType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(HistoryType historyType) {
        if (historyType == null) {
            return null;
        }

        return historyType.getDbValue();
    }

    @Override
    public HistoryType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return HistoryType.fromDbValue(dbData);
    }
}
