package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Tournament;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class PlayerRepository {
  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  public PlayerRepository(){
  }

  /**
   * Constructor for testing.
   */
  public PlayerRepository(EntityManager entityManager){
    this.entityManager = entityManager;
  }

  public List<Player> getAll(){
    return entityManager.createNamedQuery("Player.findAll", Player.class).getResultList();
  }
}
