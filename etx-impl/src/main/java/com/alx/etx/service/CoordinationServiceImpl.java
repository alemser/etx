package com.alx.etx.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alx.etx.Coordination;
import com.alx.etx.model.CoordinationEntity;
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
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Coordination getCoordination(@PathParam("id") String id) {
		CoordinationEntity c = new CoordinationEntity();
		c.setId(id);
		c.start();
		return c;
	}
	
	protected Repository getRepository() {
		return new MemoryRepository();
	}
	
}
