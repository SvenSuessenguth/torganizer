package org.cc.torganizer.frontend.disciplines.rounds;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.core.entities.System;
import org.cc.torganizer.frontend.State;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesState;

@ConversationScoped
@Named
public class RoundsState implements Serializable, State {

    @Inject
    private DisciplinesState disciplinesState;

    private Round round;
    private int newGroupsCount;

    @PostConstruct
    public void postConstruct() {
        synchronize();
    }

    @Override
    public void synchronize() {
        Discipline discipline = disciplinesState.getDiscipline();
        round = discipline.getRound(0);
    }

    public List<Round> getRounds() {
        Discipline discipline = disciplinesState.getDiscipline();
        return discipline.getRounds();
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    /**
     * Checking, if the current round is the last round.
     */
    public boolean isLastRound() {
        Discipline discipline = disciplinesState.getDiscipline();
        int highestPostion = 0;
        for (Round r : discipline.getRounds()) {
            if (r.getPosition() > highestPostion) {
                highestPostion = r.getPosition();
            }
        }

        return round.getPosition() == highestPostion;
    }

    public int getNewGroupsCount() {
        return newGroupsCount;
    }

    public void setNewGroupsCount(int newGroupsCount) {
        this.newGroupsCount = newGroupsCount;
    }

    public System[] getSystems() {
        return System.values();
    }

}
