package org.cc.torganizer.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.cc.torganizer.persistence.Repository.DEFAULT_MAX_RESULTS;
import static org.cc.torganizer.persistence.Repository.DEFAULT_OFFSET;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class RepositoryTest {

    private Repository repository;

    @BeforeEach
    public void before(){
        repository = new Repository();
    }

    @Test
    void getOffsetToUse_null() {
        Integer offset = repository.getOffsetToUse(null);
        assertThat(offset, is(DEFAULT_OFFSET));
    }

    @Test
    void getOffsetToUse_notNull() {
        Integer offset = repository.getOffsetToUse(1);
        assertThat(offset, is(1));
    }

    @Test
    void getMaxResultsToUse_null() {
        Integer maxResults = repository.getMaxResultsToUse(null);
        assertThat(maxResults, is(DEFAULT_MAX_RESULTS));
    }

    @Test
    void getMaxResultsToUse_notNull() {
        Integer maxResults = repository.getMaxResultsToUse(1);
        assertThat(maxResults, is(1));
    }
}