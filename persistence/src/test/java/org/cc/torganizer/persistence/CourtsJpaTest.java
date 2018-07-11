package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Court;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class CourtsJpaTest extends AbstractDbUnitJpaTest {

    @BeforeEach
    public void before() throws Exception {
        super.initDatabase("test-data-courts.xml");
    }

    @Test
    public void testFindAll() {
        TypedQuery<Court> namedQuery = entityManager.createNamedQuery("Court.findAll", Court.class);

        List<Court> allCourts = namedQuery.getResultList();
        assertThat(allCourts, hasSize(2));
    }
}
