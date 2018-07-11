package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Gymnasium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class GymnasiumsJpaTest extends AbstractDbUnitJpaTest {

    @BeforeEach
    public void before() throws Exception {
        super.initDatabase("test-data-gymnasiums.xml");
    }

    @Test
    public void testFindByTournament_withGymnasiums() {
        List<Gymnasium> allGymnasiums = entityManager.createNamedQuery("Gymnasium.findByTournament", Gymnasium.class)
                .setParameter("tournamentId", 1L)
                .getResultList();
        assertThat(allGymnasiums, hasSize(2));
    }

    @Test
    public void testFindByTournament_withoutGymnasiums() {
        List<Gymnasium> allGymnasiums = entityManager.cSreateNamedQuery("Gymnasium.findByTournament", Gymnasium.class)
                .setParameter("tournamentId", 2L)
                .getResultList();
        assertThat(allGymnasiums, hasSize(0));
    }
}
