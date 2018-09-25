package org.cc.torganizer.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.cc.torganizer.core.entities.Entity;

public abstract class Repository<T extends Entity> {

  protected static final int DEFAULT_MAX_RESULTS = 10;
  protected static final int DEFAULT_OFFSET = 0;

  @PersistenceContext(name = "torganizer")
  protected EntityManager entityManager;

  protected Repository() {
  }

  /**
   * persisting a new entity.
   */
  public T create(T t) {
    t.setId(null);

    entityManager.persist(t);
    entityManager.flush();

    return t;
  }

  /**
   * Reading the entity with the given id.
   */
  abstract T read(Long id);

  /**
   * Reading <code>maxResults</code> entities beginning with the <code>offset</code>.
   */
  abstract List<T> read(Integer offset, Integer maxResults);

  /**
   * Updating the Database with the entities data.
   */
  public T update(T t) {
    entityManager.merge(t);
    return t;
  }

  /**
   * Deleting the entity from the database.
   */
  public T delete(T t) {
    entityManager.remove(t);
    return t;
  }
}
