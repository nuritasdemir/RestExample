package com.example.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/money-transfer")
public interface MoneyTransferApi {
    @POST
    @Path("transfer")
    @Produces(MediaType.TEXT_PLAIN)
    long transfer(@QueryParam("source-account-id") long sourceAccountId,
                  @QueryParam("destination-account-id") long destinationAccountId,
                  @QueryParam("amount") long amount);
}
