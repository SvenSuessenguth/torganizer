package org.cc.torganizer.persistence;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.core.entities.Tournament;

@Stateless
public class TournamentsRepository extends Repository<Tournament> {

  public TournamentsRepository() {
  }

  /**
   * Constructor for testing purpose.
   *
   * @param entityManager EntityManager
   */
  TournamentsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }


  //-----------------------------------------------------------------------------------------------
  //
  // Tournaments CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Tournament read(Long tournamentId) {
    return entityManager.find(Tournament.class, tournamentId);
  }

  @Override
  public List<Tournament> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery("Tournament.findAll",
        Tournament.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  @Override
  public Tournament delete(Long tournamentId) {
    return super.delete(Tournament.class, tournamentId);
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Tournaments players
  //
  //-----------------------------------------------------------------------------------------------
  public List<Player> getPlayersOrderedByLastName(Long tournamentId, Integer offset,
                                                  Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Player> cq = cb.createQuery(Player.class);
    Root<Tournament> tournament = cq.from(Tournament.class);
    Root<Player> player = cq.from(Player.class);
    Join<Tournament, Player> tournamentOpponentJoin = tournament.join("opponents");

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
    query.setFirstResult(offset);
    query.setMaxResults(maxResults);

    return query.getResultList();
  }

  public Player addPlayer(Long tournamentId, Long playerId) {
    Player player = entityManager.find(Player.class, (Object) playerId);
    Tournament tournament = entityManager.find(Tournament.class, tournamentId);

    // persist tournament
    Set<Opponent> opponents = tournament.getOpponents();
    opponents.add(player);
    entityManager.persist(tournament);
    // to get the id
    entityManager.flush();

    return player;
  }

  public Player removePlayer(Long tournamentId, Long playerId) {
    Player player = entityManager.find(Player.class, playerId);
    Tournament tournament = entityManager.find(Tournament.class, tournamentId);

    // persist tournament
    tournament.getOpponents().remove(player);
    entityManager.persist(tournament);
    // to get the id
    entityManager.flush();

    return player;
  }

  public long countPlayers(Long tournamentId) {
    Query query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", tournamentId);

    return (long) query.getSingleResult();
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Tournaments squads
  //
  //-----------------------------------------------------------------------------------------------
  public Squad addSquad(Long tournamentId, Long squadId) {
    Squad squad = entityManager.find(Squad.class, squadId);
    Tournament tournament = entityManager.find(Tournament.class, tournamentId);

    // persist tournament
    tournament.getOpponents().add(squad);
    entityManager.persist(tournament);

    return squad;
  }

  public List<Squad> getSquads(Long tournamentId, Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Tournament.findSquads",
        Squad.class);
    namedQuery.setParameter("id", tournamentId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    List<Squad> squads = namedQuery.getResultList();
    Collections.sort(squads, new OpponentByNameComparator());

    return squads;
  }

  public long countSquads(Long tournamentId) {
    Query query = entityManager.createNamedQuery("Tournament.countSquads");
    query.setParameter("id", tournamentId);

    return (long) query.getSingleResult();
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Tournaments disciplines
  //
  //-----------------------------------------------------------------------------------------------
  public List<Discipline> getDisciplines(Long tournamentId, Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery(
        "Tournament.findDisciplines", Discipline.class);
    namedQuery.setParameter("id", tournamentId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  public Discipline addDiscipline(Long tournamentId, Discipline discipline) {
    Tournament tournament = entityManager.find(Tournament.class, tournamentId);

    // persist tournament
    tournament.getDisciplines().add(discipline);
    entityManager.persist(tournament);

    return discipline;
  }

  public List<Opponent> getOpponentsForDiscipline(Long tournamentId, Discipline discipline,
                                                  Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    // load tournaments opponents
    Tournament tournament = entityManager.find(Tournament.class, tournamentId);
    Set<Opponent> opponents = tournament.getOpponents();

    // filter opponents
    List<Opponent> assignableOpponents = opponents
        .stream()
        .filter(discipline::isAssignable)
        .collect(Collectors.toList());

    // sort and use offset/length
    Collections.sort(assignableOpponents, new OpponentByNameComparator());

    // check offset/maxResults
    int size = assignableOpponents.size();
    if (offset > size) {
      offset = size;
    }
    if (offset + maxResults > size) {
      maxResults = size - offset;
    }

    return assignableOpponents.subList(offset, offset + maxResults);
  }
}
