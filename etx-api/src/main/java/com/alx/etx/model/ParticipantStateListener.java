package com.alx.etx.model;

import com.alx.etx.data.Participant;
import com.alx.etx.data.ParticipantState;
import com.alx.etx.event.ParticipantEvent;
import com.alx.etx.service.CoordinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.alx.etx.data.CoordinationState.ROLLEDBACK;

@Component
public class ParticipantStateListener implements ApplicationListener<ParticipantEvent>{

    @Autowired
    private CoordinationService coordinationService;

    @Override
    public void onApplicationEvent(ParticipantEvent participantEvent) {
        Participant participant = (Participant) participantEvent.getSource();
        if (participant.getState() == ParticipantState.CANCELLED) {
            coordinationService.get(participantEvent.getCoordination().getId())
                    .subscribe(coord -> coord.setState(ROLLEDBACK));
        }

    }
}
