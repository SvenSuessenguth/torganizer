package org.cc.torganizer.core.entities;

import java.util.Set;

@FunctionalInterface
interface QualifiedOpponentsCalculator {
  Set<Opponent> calculate(Round round);
}