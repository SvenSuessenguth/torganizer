package org.cc.torganizer.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Repository {

    protected static final int DEFAULT_MAX_RESULTS = 10;
    protected static final int DEFAULT_OFFSET = 0;

    @PersistenceContext(name = "torganizer")
    protected EntityManager entityManager;

    protected Repository() {
    }

    public final Integer getOffsetToUse(final Integer offset) {
        if (offset == null) {
            return DEFAULT_OFFSET;
        }

        return offset;
    }

    public final Integer getMaxResultsToUse(final Integer maxResults) {
        if (maxResults == null) {
            return DEFAULT_MAX_RESULTS;
        }

        return maxResults;
    }
}
