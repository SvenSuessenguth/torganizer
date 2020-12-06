package org.cc.torganizer.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.cc.torganizer.core.entities.Gender;

@Converter(autoApply = true)
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
