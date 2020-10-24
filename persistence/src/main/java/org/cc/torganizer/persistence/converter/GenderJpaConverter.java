package org.cc.torganizer.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.cc.torganizer.core.entities.Gender;

@Converter
public class GenderJpaConverter implements AttributeConverter<Gender, String> {

  @Override
  public String convertToDatabaseColumn(Gender gender) {
    return gender == null ? "" : gender.getId();
  }

  @Override
  public Gender convertToEntityAttribute(String s) {
    return Gender.byId(s);
  }
}
