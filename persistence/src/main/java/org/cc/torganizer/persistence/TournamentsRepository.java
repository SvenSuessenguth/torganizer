package org.cc.torganizer.persistence;

import static javax.transaction.Transactional.TxType.NEVER;
import static javax.transaction.Transactional.TxType.REQUIRED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.core.filter.OpponentFilter;

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
    this.entityManager = entityManager;
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
  @Transactional(NEVER)
  public Tournament read(Long tournamentId) {
    return entityManager.find(Tournament.class, tournamentId);
  }

  /**
   * Reading some tournaments from offset to maxResults.
   */
  @Override
  @Transactional(NEVER)
  public List<Tournament> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery("Tournament.findAll",
        Tournament.class);
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
  public List<Player> getPlayers(Long tournamentId, Integer offset,
                                 Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Tournament.findPlayers",
        Player.class);
    namedQuery.setParameter("id", tournamentId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  /**
   * Getting the Players (offset to maxResults) of the given tournament ordered by last name.
   */
  @Transactional(NEVER)
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

  /**
   * Add a player to a tournament.
   */
  @Transactional(REQUIRED)
  public Opponent addOpponent(Long tournamentId, Long opponentId) {
    Opponent opponent = entityManager.find(Opponent.class, opponentId);
    Tournament tournament = entityManager.find(Tournament.class, tournamentId);

    Set<Opponent> opponents = tournament.getOpponents();
    opponents.add(opponent);
    entityManager.persist(tournament);
    // to get the id
    entityManager.flush();

    return opponent;
  }

  /**
   * Removing a player from a tournament.
   */
  @Transactional(REQUIRED)
  public Opponent removeOpponent(Long tournamentId, Long opponentId) {
    Opponent opponent = entityManager.find(Opponent.class, opponentId);
    Tournament tournament = entityManager.find(Tournament.class, tournamentId);

    // persist tournament
    tournament.getOpponents().remove(opponent);
    entityManager.persist(tournament);
    // to get the id
    entityManager.flush();

    return opponent;
  }

  /**
   * Removing a discipline from the given tournament.
   */
  @Transactional(REQUIRED)
  public Discipline removeDiscipline(Long tournamentId, Long disciplineId) {
    Discipline discipline = entityManager.find(Discipline.class, disciplineId);
    Tournament tournament = entityManager.find(Tournament.class, tournamentId);

    // persist tournament
    tournament.getDisciplines().remove(discipline);
    entityManager.persist(tournament);
    // to get the id
    entityManager.flush();

    return discipline;
  }

  /**
   * Counting the players of a tournament.
   */
  @Transactional(NEVER)
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

  /**
   * Getting the squads from offset o maxResults from a tournament.
   */
  @Transactional(NEVER)
  public List<Squad> getSquads(Long tournamentId, Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Tournament.findSquads",
        Squad.class);
    namedQuery.setParameter("id", tournamentId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    List<Squad> squads = namedQuery.getResultList();
    squads.sort(new OpponentByNameComparator());

    return squads;
  }

  /**
   * Counting the squads of a tournament.
   */
  @Transactional(NEVER)
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

  /**
   * Gettng the disciplines from offset to maxResults from a tournament.
   */
  @Transactional(NEVER)
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

  /**
   * Adding a discipline to a tournament.
   */
  @Transactional(REQUIRED)
  public Discipline addDiscipline(Long tournamentId, Discipline discipline) {
    Tournament tournament = entityManager.find(Tournament.class, tournamentId);

    // persist tournament
    tournament.getDisciplines().add(discipline);
    entityManager.persist(tournament);

    return discipline;
  }

  /**
   * Getting the opponents, which can be assigned to a discipline.
   */
  @Transactional(NEVER)
  public List<Opponent> getAssignableOpponentsForDiscipline(Long tournamentId,
                                                            Discipline discipline,
                                                            Integer offset,
                                                            Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    // load tournaments opponents
    Tournament tournament = entityManager.find(Tournament.class, tournamentId);
    Set<Opponent> opponents = tournament.getOpponents();

    // filter opponents
    OpponentFilter opponentFilter = new OpponentFilter();
    Collection<Restriction> restrictions = discipline.getRestrictions();
    Collection<Opponent> pass = opponentFilter.pass(opponents, restrictions);
    List<Opponent> assignableOpponents = new ArrayList<>(pass);

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
}
