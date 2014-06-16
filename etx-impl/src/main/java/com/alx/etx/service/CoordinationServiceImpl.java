package com.alx.etx.service;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alx.etx.Coordination;
import com.alx.etx.repository.MemoryRepository;
import com.alx.etx.repository.Repository;

/**
 * Main endpoint for transaction coordination procedures.
 * @author alemser
 */
@Path("etx")
public class CoordinationServiceImpl implements CoordinationService {

	@GET
	@Path("start")
	public String start() {
		return getRepository().initCoordination();
	}
	
	@POST
	@Path("join")
	public String join(@FormParam("cid") String coordinationId, @FormParam("pname") String participantName) {
		return getRepository().join(coordinationId, participantName);
	}

	@POST
	@Path("end")
	public void end(@FormParam("cid") String coordinationId) {
		getRepository().end(coordinationId);
	}
	
	@POST
	@Path("partState")
	public void partState(@FormParam("cid") String coordinationId, @FormParam("pid") String participantId, @FormParam("st") int state) {
		getRepository().setParticipantState(coordinationId, participantId, state);
	}
	
	@POST
	@Path("coordState")
	public void coordState(@FormParam("cid") String coordinationId, @FormParam("st") int state) {
		getRepository().setCoordinationState(coordinationId, state);
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Coordination coordination(@PathParam("id") String id) {
		return getRepository().getCoordination(id);
	}
	
	/**
	 * Return the coordination repository.
	 * @return the repository.
	 */
	protected Repository getRepository() {
		return new MemoryRepository();
	}
	
}
