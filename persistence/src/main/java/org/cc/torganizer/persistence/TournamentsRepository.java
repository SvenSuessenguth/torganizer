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
public class TournamentsRepository {

  public static final Integer DEFAULT_OFFSET = 0;
  public static final Integer DEFAULT_MAX_RESULTS = 10;

  private static final String TOURNAMENT_FIND_BY_ID_QUERY_NAME = "Tournament.findById";

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  public TournamentsRepository(){
  }

  /**
   * Constructor for testing purpose.
   */
  public TournamentsRepository(EntityManager entityManager){
    this.entityManager = entityManager;
  }


  public Tournament getTournament(Long tournamentId){
    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME, Tournament.class);
    namedQuery.setParameter("id", tournamentId);
    return namedQuery.getSingleResult();
  }

  public List<Tournament> getTournaments(Integer offset, Integer maxResults){
    if (offset == null || maxResults == null) {
      offset = DEFAULT_OFFSET;
      maxResults = DEFAULT_MAX_RESULTS;
    }

    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery("Tournament.findAll", Tournament.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public List<Player> getPlayersOrderedByLastName(Long tournamentId, int firstResult, int maxResults){
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Player> cq = cb.createQuery(Player.class);
    Root<Tournament> tournament = cq.from(Tournament.class);
    Root<Player> player = cq.from(Player.class);
    Join<Tournament, Player> tournamentOpponentJoin = tournament.join ("opponents");

    cq.select(player);
    cq.where(
      cb.and(
        cb.equal(tournament.get("id"), tournamentId),
        cb.equal(tournamentOpponentJoin.type(), Player.class),
        cb.equal(player.get("id"), tournamentOpponentJoin.get("id"))
      )
    );

    cq.orderBy(
      cb.asc(
        player.get("person").get("lastName")
      )
    );

    TypedQuery<Player> query = entityManager.createQuery(cq);
    query.setFirstResult(firstResult);
    query.setMaxResults(maxResults);

    return query.getResultList();
  }

}
