package org.fcrepo.ffmodeshapeprototype;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class FedoraRepositoryTest {

    private TJWSEmbeddedJaxrsServer server;
    int SERVER_PORT = 9999;

    @Before
    public void start() {

        server = new TJWSEmbeddedJaxrsServer();
        server.setPort(SERVER_PORT);
        server.getDeployment().getActualResourceClasses().add(FedoraRepository.class);
        server.start();
    }

    @After
    public void stop() {
        server.stop();
    }

    @Test
    public void testDescribeModeshape() throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod("http://localhost:" + SERVER_PORT + "/describe/modeshape");
        int status = client.executeMethod(method);
        assertEquals(200, status);
    }

    @Test
    public void testDescribe() throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod("http://localhost:" + SERVER_PORT + "/describe");
        int status = client.executeMethod(method);
        assertEquals(200, status);
    }

    @Test
    public void testGetObjects() throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod("http://localhost:" + SERVER_PORT + "/objects");
        int status = client.executeMethod(method);
        assertEquals(200, status);
    }
}