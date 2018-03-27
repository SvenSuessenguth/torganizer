package org.cc.torganizer.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Repository {

  protected static final int DEFAULT_MAX_RESULTS = 10;
  protected static final int DEFAULT_OFFSET = 0;

  @PersistenceContext(name = "torganizer")
  protected EntityManager entityManager;

  protected Repository(){}
}
