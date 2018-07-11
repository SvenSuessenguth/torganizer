package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ResultsJpaTest extends AbstractDbUnitJpaTest {

    @BeforeEach
    public void before() throws Exception {
        super.initDatabase("test-data-results.xml");
    }

    @Test
    public void testFindAll() {
        List<Result> results = entityManager.createNamedQuery("Result.findAll", Result.class).getResultList();
        assertThat(results, hasSize(3));
    }
}
