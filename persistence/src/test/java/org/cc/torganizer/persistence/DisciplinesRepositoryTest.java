package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Opponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DisciplinesRepositoryTest extends AbstractDbUnitJpaTest {

    private DisciplinesRepository repository;

    @BeforeEach
    public void before() throws Exception {
        super.initDatabase("test-data-tournament.xml");

        repository = new DisciplinesRepository(entityManager);
    }

    @Test
    public void testGetOpponents() {
        List<Opponent> opponents = repository.getOpponents(1L, null, null);

        assertThat(opponents, is(not(nullValue())));
        assertThat(opponents, hasSize(2));
    }

    @Test
    public void testGetDisciplineId_existing() {
        // testdata:
        // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
        Long id = repository.getDisciplineId(1L);

        assertThat(id, is(1L));
    }

    @Test
    public void testGetRoundId_roundDoesNotExist() {
        // testdata:
        // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
        Long id = repository.getDisciplineId(-1L);

        assertThat(id, is(nullValue()));
    }
}
