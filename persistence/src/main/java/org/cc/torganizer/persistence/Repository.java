package org.cc.torganizer.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.transaction.Transactional;
import org.cc.torganizer.core.entities.Entity;

import java.util.Date;
import java.util.List;

import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

/**
 * Abstract class for accessing the Repository for entities.
 */
public abstract class Repository<T extends Entity> {

  protected static final Integer DEFAULT_MAX_RESULTS = 10;
  protected static final Integer DEFAULT_OFFSET = 0;

  @PersistenceContext(unitName = "torganizer", type = PersistenceContextType.TRANSACTION)
  protected EntityManager em;

  protected Repository() {
  }

  /**
   * persisting a new entity.
   */
  @Transactional(REQUIRES_NEW)
  public T create(T t) throws CreateEntityException {
    try {
      t.setId(null);
      t.setLastUpdate(new Date(System.currentTimeMillis()));

      em.persist(t);
      em.flush();

      return t;
    } catch (Exception exc) {
      throw new CreateEntityException(t, exc);
    }
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
  @Transactional(REQUIRED)
  public T delete(Long entityId) {
    return delete(read(entityId));
  }

  /**
   * Deleting the entity from the database.
   */
  @Transactional(REQUIRED)
  public T delete(T t) {
    em.remove(t);
    return t;
  }

  /**
   * Updating the Database with the entities data.
   */
  @Transactional(REQUIRES_NEW)
  public T update(T t) {
    t.setLastUpdate(new Date(System.currentTimeMillis()));
    return em.merge(t);
  }
}
