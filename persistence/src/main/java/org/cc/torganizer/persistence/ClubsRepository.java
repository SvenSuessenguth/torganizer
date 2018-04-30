package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Club;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ClubsRepository extends Repository{

  public ClubsRepository(){
  }

  /**
   * Constructor for testing purpose.
   */
  public ClubsRepository(EntityManager entityManager){
    this.entityManager = entityManager;
  }


  //--------------------------------------------------------------------------------------------------------------------
  //
  // Tournaments CRUD
  //
  //--------------------------------------------------------------------------------------------------------------------
  public Club create(Club club){
    entityManager.persist(club);
    entityManager.flush();

    return club;
  }

  public Club read(Long clubId){
    return entityManager.find(Club.class, clubId);
  }

  public List<Club> read(Integer offset, Integer maxResults){
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;


    TypedQuery<Club> namedQuery = entityManager.createNamedQuery("Club.findAll", Club.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public Club update(Club club){
    entityManager.merge(club);

    return club;
  }
  public Club delete(Club club){
    entityManager.remove(club);

    return club;
  }

  public List<Club> getClubs(Integer offset, Integer maxResults){
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Club> namedQuery = entityManager.createNamedQuery("Club.findAll", Club.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }
}
