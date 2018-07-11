package org.cc.torganizer.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MatchesJpaTest extends AbstractDbUnitJpaTest {

    private MatchesRepository repository;

    @BeforeEach
    public void before() throws Exception {
        super.initDatabase("test-data-matches.xml");

        repository = new MatchesRepository(entityManager);
    }

    @Test
    public void testCount() {
        Long countExpected = 1L;
        Long countActual = repository.count();

        assertThat(countActual, is(countExpected));
    }
}
