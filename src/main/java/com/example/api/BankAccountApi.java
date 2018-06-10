package com.example.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/bank-account")
public interface BankAccountApi {
    @GET
    @Path("balance")
    @Produces(MediaType.TEXT_PLAIN)
    long balance(@QueryParam("accountId") long accountId);

    @POST
    @Path("open")
    @Produces(MediaType.TEXT_PLAIN)
    long withdraw(@QueryParam("userId") long userId);

    @POST
    @Path("withdraw")
    @Produces(MediaType.TEXT_PLAIN)
    long withdraw(@QueryParam("accountId") long accountId, @QueryParam("amount") long amount);

    @POST
    @Path("deposit")
    @Produces(MediaType.TEXT_PLAIN)
    long deposit(@QueryParam("accountId") long accountId, @QueryParam("amount") long amount);
}