package com.alx.etx.model;

import com.alx.etx.data.Coordination;
import com.alx.etx.data.CoordinationState;
import com.alx.etx.service.exception.InvalidCoordinationStateException;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.alx.etx.data.CoordinationState.CREATED;
import static com.alx.etx.data.CoordinationState.ENDED;
import static com.alx.etx.data.CoordinationState.ENDED_CANCELLED;
import static com.alx.etx.data.CoordinationState.ENDED_TIMEOUT;
import static com.alx.etx.data.CoordinationState.INCONSISTENT;
import static com.alx.etx.data.CoordinationState.ROLLEDBACK;
import static com.alx.etx.data.CoordinationState.RUNNING;
import static com.alx.etx.data.ParticipantState.CANCELLED_BY_TIMEOUT;

/**
 * An coordination.
 * 
 * @author alemser
 */
public class CoordinationEntity extends Coordination {

	/**
	 * Starts the coordination.
	 */
	public Coordination start() {
		setId(UUID.randomUUID().toString());
		updateState(RUNNING);
		setTimeout(getStartTime().plus(1, ChronoUnit.SECONDS));
		return this;
	}

	/**
	 * Ends the coordination.
	 */
	public Boolean end() {
		switch (getState()) {
			case RUNNING:
				if( allConfirmed() ) {
					updateState(ENDED);
					return true;
				}

				updateState(INCONSISTENT);
				return end();

			case ROLLEDBACK:
				if (allCancelled()) {
					updateState(ENDED_CANCELLED);
					return end();
				}

				return false;
			case INCONSISTENT:
				if (isTimedOut()) {
					getParticipants().values().forEach(p -> p.updateState(CANCELLED_BY_TIMEOUT));
					updateState(ENDED_TIMEOUT);
				}
				return false;

			default:
				return false;
		}
	}

	/**
	 * Update the coordination state.
	 *
	 * @param state the new state of the coordination.
	 * @return the previous state.
	 */
	private CoordinationState updateState(CoordinationState state) {
		if (!isValidStateToGo(state)) {
			throw new InvalidCoordinationStateException(getState(), state);
		}

		CoordinationState previousState = getState();
		setState(state);
		switch (state) {
			case CREATED:
				setCreateTime(OffsetDateTime.now());
				break;
			case RUNNING:
				setStartTime(OffsetDateTime.now());
				break;
			case ENDED:
			case ENDED_CANCELLED:
			case ENDED_TIMEOUT:
				setEndTime(OffsetDateTime.now());
			default:
		}
		return previousState;
	}

	/**
	 * Checks if the desired state is applicable.
	 * @param state the desired state.
	 * @return true if it is a valid state to go.
	 */
	private boolean isValidStateToGo(CoordinationState state) {
		switch (state) {
			case INCONSISTENT:
				return CREATED == getState() || RUNNING == getState();
			case CREATED:
				return CREATED == getState();
			case RUNNING:
				return CREATED == getState();
			case ENDED:
				return RUNNING == getState() || CREATED == getState();
			case ENDED_CANCELLED:
				return ROLLEDBACK == getState();
			case ENDED_TIMEOUT:
				return RUNNING == getState() || INCONSISTENT == getState();
			default:
				return false;
		}		
	}
}
