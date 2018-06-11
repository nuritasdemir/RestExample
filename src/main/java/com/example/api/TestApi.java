package com.example.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/test")
public interface TestApi {
    @DELETE
    @Path("all")
    @Produces(MediaType.TEXT_PLAIN)
    boolean deleteAll();
}
