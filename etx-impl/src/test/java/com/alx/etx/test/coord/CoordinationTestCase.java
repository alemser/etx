package com.alx.etx.test.coord;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Assert;
import org.junit.Test;

import com.alx.etx.Coordination;
import com.alx.etx.test.Base;

public class CoordinationTestCase extends Base {

	@Test
	public void testStart() {
		Invocation invocation = target.path("etx/start").request().buildGet();
        Response response = invocation.invoke();
        String coordId = response.readEntity(String.class);
        Assert.assertNotNull(coordId);
	}
	
    @Test
    public void testGet() {
		Invocation invocation = target.path("etx/start").request().buildGet();
        Response response = invocation.invoke();
        String coordId = response.readEntity(String.class);

    	invocation = target.path("etx/" + coordId).register(JacksonFeature.class).request().buildGet();
        response = invocation.invoke();
        Coordination coord = response.readEntity(Coordination.class);
        
        Assert.assertEquals(Coordination.RUNNING, coord.getState());
        Assert.assertEquals(0, coord.getParticipants().size());
        Assert.assertNotNull(coord.getStartTime());
    }
    
}
