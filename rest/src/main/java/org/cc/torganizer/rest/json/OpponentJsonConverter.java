package org.cc.torganizer.rest.json;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.cc.torganizer.core.entities.OpponentType;

/**
 * Annotation to collect all Opponents for Opponents and find the correct one in the {@link OpponentJsonConverterProvider}. 
 * @author svens
 */
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface OpponentJsonConverter {
  OpponentType type() default OpponentType.PLAYER;

}
