package org.cc.torganizer.persistence;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.cc.torganizer.core.entities.Entity;

public abstract class Repository<T extends Entity> {

  protected static final Integer DEFAULT_MAX_RESULTS = Integer.valueOf(10);
  protected static final Integer DEFAULT_OFFSET = Integer.valueOf(0);

  @PersistenceContext(name = "torganizer")
  protected EntityManager entityManager;

  protected Repository() {
  }

  /**
   * persisting a new entity.
   */
  public T create(T t) {
    t.setId(null);
    t.setLastUpdate(new Date(System.currentTimeMillis()));

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
   * Deleting an entity with the given id.
   */
  public T delete(Long entityId) {
    return delete(read(entityId));
  }

  /**
   * Deleting the entity from the database.
   */
  public T delete(T t) {
    entityManager.remove(t);
    return t;
  }

  /**
   * Updating the Database with the entities data.
   */
  public T update(T t) {
    t.setLastUpdate(new Date(System.currentTimeMillis()));
    return entityManager.merge(t);
  }
}
