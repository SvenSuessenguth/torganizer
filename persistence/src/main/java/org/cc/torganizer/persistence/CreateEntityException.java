package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Entity;

/**
 * Exception on creating (first time persisting via JPA) an entity.
 */
public class CreateEntityException extends RuntimeException {
  public CreateEntityException(Entity e, Throwable throwable) {
    super("Error on creating entity with type '%s'".formatted(e.getClass().getSimpleName()),
        throwable);
  }
}
