package org.cc.torganizer.persistence;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.*;
import org.cc.torganizer.core.filter.OpponentFilter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.transaction.Transactional.TxType.*;

/**
 * Accessing the Repository for Tournaments and related entities.
 */
@RequestScoped
public class TournamentsRepository extends Repository<Tournament> {

  public TournamentsRepository() {
  }

  /**
   * Constructor for testing purpose.
   *
   * @param entityManager EntityManager
   */
  TournamentsRepository(EntityManager entityManager) {
    this.em = entityManager;
  }


  //-----------------------------------------------------------------------------------------------
  //
  // Tournaments CRUD
  //
  //-----------------------------------------------------------------------------------------------

  /**
   * Reading the Tournament with the given id.
   */
  @Override
  @Transactional(SUPPORTS)
  public Tournament read(Long tournamentId) {
    return em.find(Tournament.class, tournamentId);
  }

  /**
   * Reading some tournaments from offset to maxResults.
   */
  @Override
  @Transactional(SUPPORTS)
  public List<Tournament> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    var namedQuery = em.createNamedQuery("Tournament.findAll", Tournament.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Tournaments players
  //
  //-----------------------------------------------------------------------------------------------

  /**
   * reading all players from the given tournament.
   */
  @Transactional(NEVER)
  public List<Player> getPlayers(Long tournamentId, Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    var namedQuery = em.createNamedQuery("Tournament.findPlayers", Player.class);
    namedQuery.setParameter("id", tournamentId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  /**
   * Getting the Players (offset to maxResults) of the given tournament ordered by last name.
   */
  @Transactional(NEVER)
  public List<Player> getPlayersOrderedByLastName(Long tournamentId, Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    var cb = em.getCriteriaBuilder();
    var cq = cb.createQuery(Player.class);
    var tournament = cq.from(Tournament.class);
    var player = cq.from(Player.class);
    var tournamentOpponentJoin = tournament.join("opponents");

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

    var query = em.createQuery(cq);
    query.setFirstResult(offset);
    query.setMaxResults(maxResults);

    return query.getResultList();
  }

  /**
   * Add a player to a tournament.
   */
  @Transactional(REQUIRED)
  public void addOpponent(Long tournamentId, Long opponentId) {
    var opponent = em.find(Opponent.class, opponentId);
    var tournament = em.find(Tournament.class, tournamentId);

    var opponents = tournament.getOpponents();
    opponents.add(opponent);
    em.persist(tournament);
    // to get the id
    em.flush();
  }

  /**
   * Removing a player from a tournament.
   */
  @Transactional(REQUIRED)
  public Opponent removeOpponent(Long tournamentId, Long opponentId) {
    var opponent = em.find(Opponent.class, opponentId);
    var tournament = em.find(Tournament.class, tournamentId);

    // persist tournament
    tournament.getOpponents().remove(opponent);
    em.persist(tournament);
    // to get the id
    em.flush();

    return opponent;
  }

  /**
   * Removing a discipline from the given tournament.
   */
  @Transactional(REQUIRED)
  public Discipline removeDiscipline(Long tournamentId, Long disciplineId) {
    var discipline = em.find(Discipline.class, disciplineId);
    var tournament = em.find(Tournament.class, tournamentId);

    // persist tournament
    tournament.getDisciplines().remove(discipline);
    em.persist(tournament);
    // to get the id
    em.flush();

    return discipline;
  }

  /**
   * Counting the players of a tournament.
   */
  @Transactional(NEVER)
  public long countPlayers(Long tournamentId) {
    var query = em.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", tournamentId);

    return (long) query.getSingleResult();
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Tournaments squads
  //
  //-----------------------------------------------------------------------------------------------

  /**
   * Getting the squads from offset o maxResults from a tournament.
   */
  @Transactional(NEVER)
  public List<Squad> getSquads(Long tournamentId, Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    var namedQuery = em.createNamedQuery("Tournament.findSquads", Squad.class);
    namedQuery.setParameter("id", tournamentId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    var squads = namedQuery.getResultList();
    squads.sort(new OpponentByNameComparator());

    return squads;
  }

  /**
   * Counting the squads of a tournament.
   */
  @Transactional(NEVER)
  @SuppressWarnings("unused")
  public long countSquads(Long tournamentId) {
    var query = em.createNamedQuery("Tournament.countSquads");
    query.setParameter("id", tournamentId);

    return (long) query.getSingleResult();
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Tournaments disciplines
  //
  //-----------------------------------------------------------------------------------------------

  /**
   * Gettng the disciplines from offset to maxResults from a tournament.
   */
  @Transactional(NEVER)
  public List<Discipline> getDisciplines(Long tournamentId, Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    var namedQuery = em.createNamedQuery("Tournament.findDisciplines", Discipline.class);
    namedQuery.setParameter("id", tournamentId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  /**
   * Adding a discipline to a tournament.
   */
  @Transactional(REQUIRED)
  public void addDiscipline(Long tournamentId, Discipline discipline) {
    var tournament = em.find(Tournament.class, tournamentId);

    // persist tournament
    tournament.getDisciplines().add(discipline);
    em.persist(tournament);
  }

  /**
   * Getting the opponents, which can be assigned to a discipline.
   */
  @Transactional(NEVER)
  public List<Opponent> getAssignableOpponentsForDiscipline(Long tournamentId, Discipline discipline, Integer offset,
                                                            Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    // load tournaments opponents
    var tournament = em.find(Tournament.class, tournamentId);
    var opponents = tournament.getOpponents();

    // filter opponents
    var opponentFilter = new OpponentFilter();
    var restrictions = discipline.getRestrictions();
    var pass = opponentFilter.pass(opponents, restrictions);
    var assignableOpponents = new ArrayList<>(pass);

    // sort and use offset/length
    assignableOpponents.sort(new OpponentByNameComparator());

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

  /**
   * counting the opponents for the given tournament.
   */
  @SuppressWarnings("unused")
  public Long countOpponents(Long id) {
    var cb = em.getCriteriaBuilder();
    var cq = cb.createTupleQuery();
    var tournament = cq.from(Tournament.class);
    var tournamentOpponentJoin = tournament.join("opponents", JoinType.LEFT);
    cq.select(cb.tuple(tournament, cb.count(tournamentOpponentJoin)));
    cq.where(cb.equal(tournament.get("id"), id));

    var result = em.createQuery(cq).getResultList();

    return (Long) result.getFirst().get(1);
  }
}
