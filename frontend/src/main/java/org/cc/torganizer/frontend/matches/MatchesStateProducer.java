package org.cc.torganizer.frontend.matches;

import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

@RequestScoped
public class MatchesStateProducer {

  @Produces
  @ConversationScoped
  @Default
  @Named
  public MatchesState matchesState() {
    return new MatchesState();
  }
}
