package com.alx.etx.event;

import com.alx.etx.data.Coordination;
import org.springframework.context.ApplicationEvent;

public class ParticipantEvent extends ApplicationEvent {
    private Coordination coordination;

    public ParticipantEvent(Object source, Coordination coordination) {
        super(source);
        this.coordination = coordination;
    }

    public Coordination getCoordination() {
        return coordination;
    }
}
