package org.cc.torganizer.frontend.tournaments.actions;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.tournaments.TournamentsBacking;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.cc.torganizer.persistence.CreateEntityException;
import org.cc.torganizer.persistence.TournamentsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTournamentTest {

    @Mock
    private TournamentsRepository tournamentsRepository;

    @Mock
    private TournamentsState state;

    @Mock
    private TournamentsBacking tournamentsBacking;

    @Mock
    private FacesContext facesContext;

    @InjectMocks
    private CreateTournament createTournament;

    @Test
    void executeWithException() {
        when(state.getCurrent()).thenReturn(new Tournament());
        when(tournamentsRepository.create(any(Tournament.class)))
                .thenThrow(CreateEntityException.class);
        when(tournamentsBacking.getNameClientId()).thenReturn("1");
        var uiInput = mock(UIInput.class);
        when(tournamentsBacking.getNameInputText()).thenReturn(uiInput);

        createTournament.execute();

        verify(facesContext, times(1))
                .addMessage(any(String.class), any(FacesMessage.class));
    }
}