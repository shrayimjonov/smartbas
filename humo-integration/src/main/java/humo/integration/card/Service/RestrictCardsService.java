package humo.integration.card.Service;

public interface RestrictCardsService {

    Object getBlockedCards();

    Object getCard(Object request);

    Object addCard(Object request);

    Object removeCard(Object request);
}
