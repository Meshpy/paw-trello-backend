package com.paw.trello.endpoints.cards;

import com.paw.trello.dtos.ResponseMessage;
import com.paw.trello.dtos.cards.CardDetailsDto;
import com.paw.trello.dtos.cards.CardDto;
import com.paw.trello.dtos.cards.CreateNewCardDto;
import com.paw.trello.dtos.cards.EditCardDescriptionDto;
import com.paw.trello.services.cards.CardService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;

@Path("/card")
public class CardEndpoint {

    @Inject
    private CardService cardService;

    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewCard(JsonObject input) {
        ResponseMessage responseMessage = cardService.createNewCard(new CreateNewCardDto(input));
        return Response
                .ok()
                .entity(responseMessage.getMessage())
                .build();
    }

    @Path("/all/{listId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCardsForSpecificList(@PathParam("listId") Long listId) {
        List<CardDto> cardDtos = cardService.getAllCardsForSpecificList(listId);
        return Response
                .ok()
                .entity(cardDtos)
                .build();
    }

    @Path("/details/{cardId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCardDetails(@PathParam("cardId") Long cardId) {
        CardDetailsDto cardDetailsDto = cardService.getCardDetails(cardId);
        return Response
                .ok()
                .entity(cardDetailsDto)
                .build();
    }

    @Path("/{cardId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCardFromList(@PathParam("cardId") Long cardId) {
        ResponseMessage responseMessage = cardService.deleteCardFromList(cardId);
        return Response
                .ok()
                .entity(responseMessage.getMessage())
                .build();
    }

    @Path("/edit")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response editCardDescription(JsonObject input) {
        ResponseMessage responseMessage = cardService.editCardDescription(new EditCardDescriptionDto(input));
        return Response
                .ok()
                .entity(responseMessage.getMessage())
                .build();
    }

    @Path("/archive-card/{cardId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response archiveCard(@PathParam("cardId") Long cardId) {
        ResponseMessage responseMessage = cardService.archiveCard(cardId);
        return Response
                .ok()
                .entity(responseMessage.getMessage())
                .build();
    }

    @Path("/unarchive-card/{cardId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response unarchiveCard(@PathParam("cardId") Long cardId) {
        ResponseMessage response = cardService.unarchiveCard(cardId);
        return Response
                .ok()
                .entity(response.getMessage())
                .build();
    }

}
