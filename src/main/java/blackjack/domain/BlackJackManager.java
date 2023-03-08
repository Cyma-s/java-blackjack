package blackjack.domain;

import blackjack.domain.card.Deck;
import blackjack.dto.BlackJackGameResultDTO;
import blackjack.dto.ParticipantEntireStatusDTO;
import blackjack.dto.ParticipantStatusDTO;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BlackJackManager {
    private final Deck deck;
    private final Participants participants;

    public BlackJackManager(final List<String> playerNames) {
        this.deck = new Deck();
        this.participants = new Participants(deck, playerNames);
    }

    public void hitByDealer(final Consumer<Integer> printDealerHit) {
        Dealer dealer = participants.getDealer();
        int hitCount = 0;
        while (dealer.isHittable()) {
            dealer.hit(deck.draw());
            hitCount++;
        }

        if (hitCount > 0) {
            printDealerHit.accept(hitCount);
        }
    }

    public void hitByPlayers(final Predicate<String> checkPlayerHit, final Consumer<ParticipantStatusDTO> printPlayerCards) {
        participants.getPlayers()
                .forEach(player -> hitByPlayer(player, checkPlayerHit, printPlayerCards));
    }

    public void showGameResult(final Consumer<BlackJackGameResultDTO> printGameResults) {
        BlackJackResults blackJackResults = new BlackJackResults(participants.getDealer(), participants.getPlayers());
        printGameResults.accept(BlackJackGameResultDTO.of(blackJackResults));
    }

    private void hitByPlayer(
            final Player player,
            final Predicate<String> checkPlayerHit,
            final Consumer<ParticipantStatusDTO> printPlayerCards
    ) {
        while (player.isHittable() && checkPlayerHit.test(player.getName().getValue())) {
            player.hit(deck.draw());
            printPlayerCards.accept(ParticipantStatusDTO.of(player));
        }
        if (player.isHittable()) {
            printPlayerCards.accept(ParticipantStatusDTO.of(player));
        }
    }

    public void showParticipantStatus(final Consumer<ParticipantEntireStatusDTO> printParticipantStatus) {
        printParticipantStatus.accept(ParticipantEntireStatusDTO.of(participants.getDealer()));
        participants.getPlayers().forEach(player -> printParticipantStatus.accept(ParticipantEntireStatusDTO.of(player)));
    }

    public Dealer getDealer() {
        return participants.getDealer();
    }

    public List<Player> getPlayers() {
        return participants.getPlayers();
    }
}
