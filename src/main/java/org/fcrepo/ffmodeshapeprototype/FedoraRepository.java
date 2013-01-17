package org.fcrepo.ffmodeshapeprototype;

import java.io.IOException;

import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.modeshape.common.logging.Logger;
import org.modeshape.jcr.ConfigurationException;

import com.google.common.collect.ImmutableMap;

import freemarker.template.TemplateException;

/**
 * 
 * @author cabeer
 * @author ajs6f
 */

@Path("/")
public class FedoraRepository extends AbstractResource {

	public FedoraRepository() throws ConfigurationException,
			RepositoryException {
		super();
	}

	private final Logger logger = Logger.getLogger(FedoraRepository.class);

	@GET
	@Path("/describe")
	public Response describe() {
		return Response.ok().entity(ws.getName()).build();
	}

	@GET
	@Path("/objects")
	public Response getObjects() throws RepositoryException {
		Session session = ws.getSession();
		Node root = session.getRootNode();
		StringBuffer nodes = new StringBuffer();

		for (NodeIterator i = root.getNodes(); i.hasNext();) {
			Node n = i.nextNode();
			nodes.append("Name: " + n.getName() + ", Path:" + n.getPath()
					+ "\n");
		}

		return Response.ok().entity(nodes.toString()).build();

	}

	@POST
	@Path("/namespaces/{ns}")
	public Response registerObjectNamespace(@PathParam("ns") String ns)
			throws RepositoryException {
		Session session = ws.getSession();
		Workspace w = session.getWorkspace();
		NamespaceRegistry r = w.getNamespaceRegistry();

		r.registerNamespace(ns, "info:fedora/" + ns);

		return Response.ok().entity(ns).build();
	}

	@GET
	@Path("/namespaces")
	public Response getObjectNamespaces() throws RepositoryException {
		Session session = ws.getSession();
		Workspace w = session.getWorkspace();
		NamespaceRegistry r = w.getNamespaceRegistry();

		StringBuffer out = new StringBuffer();
		String[] uris = r.getURIs();
		String[] prefixes = r.getPrefixes();
		for (int i = 0; i < uris.length; i++) {
			out.append(prefixes[i] + " : " + uris[i] + "\n");
		}

		return Response.ok().entity(out.toString()).build();
	}

	@GET
	@Path("/namespaces")
	@Produces("text/xml")
	public Response getObjectNamespacesInXML() throws RepositoryException,
			IOException, TemplateException {
		final NamespaceRegistry reg = ws.getNamespaceRegistry();
		ImmutableMap.Builder<String, Object> b = ImmutableMap.builder();
		for (String prefix : reg.getPrefixes()) {
			b.put(prefix, reg.getURI(prefix));
		}
		return Response
				.ok()
				.entity(renderTemplate("namespaceRegistry.ftl",
						ImmutableMap.of("namespaces", (Object) b.build())))
				.build();
	}

}