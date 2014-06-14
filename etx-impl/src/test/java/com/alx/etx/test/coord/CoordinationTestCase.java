package com.alx.etx.test.coord;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Assert;
import org.junit.Test;

import com.alx.etx.Coordination;
import com.alx.etx.Participant;
import com.alx.etx.test.Base;

public class CoordinationTestCase extends Base {

	@Test
	public void testStart() {
        String coordId = start();
        Assert.assertNotNull(coordId);
	}

	@Test
	public void testChangeState() {
        String coordId = start();

        String pid = join(coordId, "p1");
        
        changePartState(coordId, pid, Participant.EXECUTED);
        Coordination coord = getCoordination(coordId);
        Assert.assertEquals(Participant.EXECUTED, coord.getParticipants().get(0).getState());
        
        changePartState(coordId, pid, Participant.CONFIRMED);
        coord = getCoordination(coordId);
        Assert.assertEquals(Participant.CONFIRMED, coord.getParticipants().get(0).getState());

        changePartState(coordId, pid, Participant.CANCELLED);
        coord = getCoordination(coordId);
        Assert.assertEquals(Participant.CANCELLED, coord.getParticipants().get(0).getState());

        coord.print(System.out);
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
        Assert.assertEquals(Participant.JOINED, coord.getParticipants().get(0).getState());
        
        coord.print(System.out);
	}
	
    @Test
    public void testGet() {
        String coordId = start();

        Coordination coord = getCoordination(coordId);
        
        Assert.assertEquals(Coordination.RUNNING, coord.getState());
        Assert.assertEquals(0, coord.getParticipants().size());
        Assert.assertNotNull(coord.getStartTime());
        
        coord.print(System.out);
    }

    protected String start() {
        Invocation invocation = target.path("etx/start").request().buildGet();
        Response response = invocation.invoke();
        return response.readEntity(String.class);
    }

    protected void changePartState(String cid, String pid, int state) {
        Form f = new Form("cid", cid).param("pid", pid).param("st", ""+state);
        Invocation invocation = target.path("etx/partState").request().buildPost(Entity.form(f));
        invocation.invoke();
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
