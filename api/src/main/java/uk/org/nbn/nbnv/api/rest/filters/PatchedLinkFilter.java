/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.filters;

import com.sun.jersey.server.linking.LinkFilter;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author Matt Debont
 */
public class PatchedLinkFilter extends LinkFilter {

	@Override
	public ContainerResponse filter(ContainerRequest request,
			ContainerResponse response) {
		
		// Don't filter when response entity type is a stream
		if (response.getEntity() != null) {
			if (StreamingOutput.class.isAssignableFrom(response.getEntity().getClass())) {
				return response;
			}
		}
		
		return super.filter(request, response);
	}
}
