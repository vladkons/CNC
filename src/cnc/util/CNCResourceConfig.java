package cnc.util;

import cnc.server.rest.AttendeeService;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

//@ApplicationPath("/")
//public class CNCResourceConfig extends ResourceConfig{
public class CNCResourceConfig {
    public CNCResourceConfig() {
      
//        super(AttendeeService.class);
//        register(RolesAllowedDynamicFeature.class);
    }
}
