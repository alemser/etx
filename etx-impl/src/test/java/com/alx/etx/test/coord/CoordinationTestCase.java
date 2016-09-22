package com.alx.etx.test.coord;

import static com.alx.etx.CoordinationState.CREATED;
import static com.alx.etx.CoordinationState.ENDED;
import static com.alx.etx.CoordinationState.RUNNING;
import static com.alx.etx.ParticipantState.CANCELLED;
import static com.alx.etx.ParticipantState.CONFIRMED;
import static com.alx.etx.ParticipantState.EXECUTED;
import static com.alx.etx.ParticipantState.JOINED;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Assert;
import org.junit.Test;

import com.alx.etx.Coordination;
import com.alx.etx.test.Base;

public class CoordinationTestCase extends Base {

	@Test
	public void testStart() {
		String coordId = start();
		Assert.assertNotNull(coordId);
	}

	@Test
	public void testEndSuccessful() {
		String coordId = start();

		String pid = join(coordId, "p1");

		changePartState(coordId, pid, EXECUTED);
		changePartState(coordId, pid, CONFIRMED);

		end(coordId);

		Coordination coord = getCoordination(coordId);

		Assert.assertNotNull(coord);

		Assert.assertEquals(ENDED, coord.getState());
	}

	@Test
	public void testChangeState() {
		String coordId = start();

		String pid = join(coordId, "p1");

		changePartState(coordId, pid, EXECUTED);
		Coordination coord = getCoordination(coordId);
		Assert.assertEquals(EXECUTED, coord.getParticipants().get(0).getState());

		changePartState(coordId, pid, CONFIRMED);
		coord = getCoordination(coordId);
		Assert.assertEquals(CONFIRMED, coord.getParticipants().get(0).getState());

		changePartState(coordId, pid, CANCELLED);
		coord = getCoordination(coordId);
		Assert.assertEquals(CANCELLED, coord.getParticipants().get(0).getState());

		coord.print(System.out);
	}

	@Test
	public void testUpdateCoordState() {
		String coordId = start();
		try {
			changeCoordState(coordId, CREATED);
			Assert.fail("Expected and exception...");
		} catch (Exception e) {

		}

		changeCoordState(coordId, ENDED);

		try {
			changeCoordState(coordId, RUNNING);
			Assert.fail("Expected and exception...");
		} catch (Exception e) {

		}

	}

	@Test
	public void testJoin() {
		String coordId = start();

		String pid = join(coordId, "p1");
		Assert.assertNotNull(pid);

		pid = join(coordId, "p2");
		Assert.assertNotNull(pid);

		Coordination coord = getCoordination(coordId);
		Assert.assertEquals(2, coord.getParticipants().size());
		Assert.assertEquals(JOINED, coord.getParticipants().get(0).getState());

		coord.print(System.out);
	}

	@Test
	public void testGet() {
		String coordId = start();

		Coordination coord = getCoordination(coordId);

		Assert.assertEquals(RUNNING, coord.getState());
		Assert.assertEquals(0, coord.getParticipants().size());
		Assert.assertNotNull(coord.getStartTime());

		coord.print(System.out);
	}

	protected String start() {
		Invocation invocation = target.path("etx/start").request().buildGet();
		Response response = invocation.invoke();
		return response.readEntity(String.class);
	}

	protected void end(String coordId) {
		Form f = new Form("cid", coordId);
		Invocation invocation = target.path("etx/end").request().buildPost(Entity.form(f));
		Response response = invocation.invoke();
		if (response.getStatus() > 204) {
			throw new RuntimeException(response.getStatusInfo().getReasonPhrase());
		}
	}

	protected void changePartState(String cid, String pid, int state) {
		Form f = new Form("cid", cid).param("pid", pid).param("st", "" + state);
		Invocation invocation = target.path("etx/partState").request().buildPost(Entity.form(f));
		invocation.invoke();
	}

	protected void changeCoordState(String cid, int state) {
		Form f = new Form("cid", cid).param("st", "" + state);
		Invocation invocation = target.path("etx/coordState").request().buildPost(Entity.form(f));
		Response response = invocation.invoke();
		if (response.getStatus() > 204) {
			throw new RuntimeException(response.getStatusInfo().getReasonPhrase());
		}
	}

	protected Coordination getCoordination(String cid) {
		Invocation invocation = target.path("etx/" + cid).register(JacksonFeature.class).request().buildGet();
		Response response = invocation.invoke();
		return response.readEntity(Coordination.class);

	}

	protected String join(String coordId, String pname) {
		Form f = new Form("cid", coordId).param("pname", pname);
		Invocation invocation = target.path("etx/join").request().buildPost(Entity.form(f));
		Response response = invocation.invoke();
		return response.readEntity(String.class);
	}

}
