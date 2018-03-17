package org.cc.torganizer.persistence.converter;

import javax.persistence.AttributeConverter;
import java.util.Objects;

public class BooleanTrueFalseConverter implements
		AttributeConverter<Boolean, Integer> {
	
	private static final Integer TRUE = 1;
	private static final Integer FALSE = 0;
	
	@Override
	public Integer convertToDatabaseColumn(Boolean value) {
		if (value) {
			return TRUE;
		} else {
			return FALSE;
		}
	}

	@Override
	public Boolean convertToEntityAttribute(Integer value) {
		return Objects.equals(value, TRUE);
	}

}
