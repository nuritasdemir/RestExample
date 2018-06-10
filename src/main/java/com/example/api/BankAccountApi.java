package com.example.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/bank-account")
public interface BankAccountApi {
    @GET
    @Path("balance")
    @Produces(MediaType.TEXT_PLAIN)
    long balance(@QueryParam("account-id") long accountId);

    @POST
    @Path("open")
    @Produces(MediaType.TEXT_PLAIN)
    long open(@QueryParam("user-id") long userId);

    @POST
    @Path("withdraw")
    @Produces(MediaType.TEXT_PLAIN)
    long withdraw(@QueryParam("account-id") long accountId, @QueryParam("amount") long amount);

    @POST
    @Path("deposit")
    @Produces(MediaType.TEXT_PLAIN)
    long deposit(@QueryParam("account-id") long accountId, @QueryParam("amount") long amount);
}