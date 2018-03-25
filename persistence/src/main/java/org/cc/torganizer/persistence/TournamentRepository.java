package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Entity;
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
public class TournamentRepository {

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  public TournamentRepository(){
  }

  /**
   * Constructor for testing.
   */
  public TournamentRepository(EntityManager entityManager){
    this.entityManager = entityManager;
  }

  public List<Player> getTournamentsPlayersOrderedByLastName(Long tournamentId, int firstResult, int maxResults){
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
