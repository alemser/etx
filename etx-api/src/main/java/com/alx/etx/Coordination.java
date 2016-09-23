package com.alx.etx;

import static com.alx.etx.CoordinationState.CREATED;
import static com.alx.etx.CoordinationState.ENDED;
import static com.alx.etx.CoordinationState.ENDED_CANCELLED;
import static com.alx.etx.CoordinationState.ENDED_TIMEOUT;
import static com.alx.etx.CoordinationState.RUNNING;
import static com.alx.etx.ParticipantState.CANCELLED;
import static com.alx.etx.ParticipantState.CONFIRMED;
import static com.alx.etx.ParticipantState.EXECUTED;
import static com.alx.etx.ParticipantState.JOINED;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

/**
 * The client representation of the coordination.
 * The coordination is started when, in case of Java language, when the customized JAX-RS client 
 * detects that the call to the resource which will participate in a coordination. For instance:
 * 
 * <code>
 * Order order = new Order(...);
 * Client client = ClientBuilder.newClient();
 * OrderReport report = client.target("http://company.com/api/order/place")
        .request(MediaType.APPLICATION_XML)
        .header("tcc_participant", "true")
        .post(Entity.xml(order), OrderReport.class);
 * <code>
 * 
 * Some details:
 * <li>When the tcc_participant header is present the API either will start a new coordination or use an existing one<li>
 * <li>The new request, now a participant, will join the coordination<li>
 * <li>The participant details are stored to be invoked latter again<li>
 * <li>All the contextual information about the coordination is passed in the headers<li>
 * <li>The participant will receive in the request headers a property informing the current operation (try, cancel or confirm).<li>
 * <li>The API who creates the coordination first will be responsible to send the confirm or cancel signal to all the current participants. The signal can be sent sync or async using messaging<li>
 */
public class Coordination implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private Date createTime;
	private Date startTime;
	private Date endTime;
	private int state;
	private List<Participant> participants;

	public Coordination() {	
		this.createTime = new Date();
		this.state = CREATED;
	}
	
	public Coordination(String id) {
		this();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	
	/**
	 * Print the current coordination state and info.
	 * 
	 * @param out the output stream the print the coordination info.
	 */
	public void print(OutputStream out) {
		StringBuilder sb = new StringBuilder();
		sb.append("Coordination " + getId() + " is " + getStateDescription());
		if( getStartTime() != null ) {
			sb.append(" and was started at " + getStartTime() + ".");
		} else {
			sb.append(" and is not started yet.");
		}
		
		sb.append("\n");
		
		int psize = getParticipants().size();
		if( psize == 0 ) {
			sb.append("There is no registered participants.");
			
		} else {			
			sb.append("There " + (psize > 1 ? " are " : " is ") + psize + " registered participant" + (psize>1?"s" : "") + ".\n");
			getParticipants().stream().forEach(p-> sb.append("=> Participant " + p.getId() + " state is " + p.getStateDescription()+  "\n"));
		}
		
		try {
			out.write(sb.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@XmlTransient
	public String getStateDescription() {
		return getStateDescription(getState());
	}

	@XmlTransient
	public String getStateDescription(int state) {
		switch (state) {
		case CREATED:
			return "created";
		case ENDED:
			return "ended";
		case ENDED_CANCELLED:
			return "cancelled";
		case ENDED_TIMEOUT:
			return "timeouted";
		case RUNNING:
			return "running";
		default:
			return "undefined";
		}
	}

	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public boolean allConfirmed() {
		return allInDesiredState(CONFIRMED);
	}
	
	public boolean allExecuted() {
		return allInDesiredState(EXECUTED);
	}
	
	public boolean allCancelled() {
		return allInDesiredState(CANCELLED);
	}
	
	public boolean allJoined() {
		return allInDesiredState(JOINED);
	}
	
	private boolean allInDesiredState(int desiredState) {
		return getParticipants().stream().allMatch(p-> p.getState() == desiredState);
	}
}
