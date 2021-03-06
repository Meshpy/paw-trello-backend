package com.paw.trello.services.cards;

import com.paw.trello.dtos.ResponseMessage;
import com.paw.trello.dtos.cards.CardDetailsDto;
import com.paw.trello.dtos.cards.CardDto;
import com.paw.trello.dtos.cards.CreateNewCardDto;
import com.paw.trello.dtos.cards.EditCardDescriptionDto;
import com.paw.trello.entities.Card;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class CardService {

    @PersistenceContext
    private EntityManager entityManager;

    public ResponseMessage createNewCard(CreateNewCardDto createNewCardDto) {
        Card card = new Card();
        card.setListId((long) createNewCardDto.getListId());
        card.setCreatorId((long) createNewCardDto.getCreatorId());
        card.setName(createNewCardDto.getName());
        card.setAuditCd(new Date());
        card.setDescription(createNewCardDto.getDescription());
        card.setIsArchieved("N");

        entityManager.persist(card);

        return ResponseMessage.builder()
                              .message("Pomyślnie utworzono nową kartę!")
                              .build();
    }

    public List<CardDto> getAllCardsForSpecificList(Long listId) {
        List<Card> cards = entityManager.createQuery("SELECT c FROM Card c WHERE c.listId = :listId")
                .setParameter("listId", listId)
                .getResultList();

        return convertCardEntityToCardDto(cards);
    }

    private List<CardDto> convertCardEntityToCardDto(List<Card> cards) {
        List<CardDto> cardDtos = new ArrayList<>();

        for (Card card : cards) {
            cardDtos.add(CardDto.builder()
                                .id(card.getCardId())
                                .listId(card.getListId())
                                .title(card.getName())
                                .description(card.getDescription())
                                .isArchived(card.getIsArchieved())
                                .build());
        }

        return cardDtos;
    }

    public CardDetailsDto getCardDetails(Long cardId) {
        Card card = (Card) entityManager.createQuery("SELECT c FROM Card c WHERE c.cardId = :cardId")
                                        .setParameter("cardId", cardId)
                                        .getSingleResult();

        return CardDetailsDto.builder()
                             .cardId(card.getCardId())
                             .description(card.getDescription())
                             .title(card.getName())
                             .isArchived(card.getIsArchieved())
                             .build();
    }

    public ResponseMessage deleteCardFromList(Long cardId) {
        Card card = (Card) entityManager.createQuery("SELECT c FROM Card c WHERE c.cardId = :cardId")
                            .setParameter("cardId", cardId)
                            .getSingleResult();

        entityManager.remove(card);

        return ResponseMessage.builder()
                              .message("Pomyślnie usunięto kartę o id: " + cardId)
                              .build();
    }

    public ResponseMessage editCardDescription(EditCardDescriptionDto editCardDescriptionDto) {
        Card card = (Card) entityManager.createQuery("SELECT c FROM Card c WHERE c.cardId = :cardId")
                                        .setParameter("cardId", editCardDescriptionDto.getId())
                                        .getSingleResult();

        card.setDescription(editCardDescriptionDto.getDescription());
        card.setName(editCardDescriptionDto.getTitle());

        entityManager.merge(card);

        return ResponseMessage.builder()
                              .message("Pomyślnie zmodyfikowano opis/tytuł karty.")
                              .build();
    }

    public ResponseMessage archiveCard(Long cardId) {
        Card card = (Card) entityManager.createQuery("SELECT c FROM Card c WHERE c.cardId = :cardId")
                                        .setParameter("cardId", cardId)
                                        .getSingleResult();

        card.setIsArchieved("Y");
        entityManager.merge(card);

        return ResponseMessage.builder()
                .message("Pomyślnie zarchiwizowano kartę o nr id: " + cardId)
                .build();
    }

    public ResponseMessage unarchiveCard(Long cardId) {
        Card card = (Card) entityManager.createQuery("SELECT c FROM Card c WHERE c.cardId = :cardId")
                                        .setParameter("cardId", cardId)
                                        .getSingleResult();

        card.setIsArchieved("N");
        entityManager.merge(card);


        return ResponseMessage.builder()
                              .message("Pomyślnie odarchiwizowano kartę o nr id: " + cardId)
                              .build();
    }

}
