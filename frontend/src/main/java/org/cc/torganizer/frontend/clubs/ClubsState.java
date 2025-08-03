package org.cc.torganizer.frontend.clubs;

import jakarta.enterprise.inject.Vetoed;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import org.cc.torganizer.core.entities.Club;

@Vetoed
@Data
public class ClubsState implements Serializable {
  private List<Club> clubs;
  private Club current;
}
