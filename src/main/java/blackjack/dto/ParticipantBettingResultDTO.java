package blackjack.dto;

import blackjack.domain.GameResult;
import blackjack.domain.Player;
import blackjack.domain.ResultType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ParticipantBettingResultDTO {
    private static final String DEALER_DEFAULT_NAME = "딜러";

    private final Map<String, Integer> bettingResults;

    private ParticipantBettingResultDTO(final Map<String, Integer> bettingResults) {
        this.bettingResults = bettingResults;
    }

    public static ParticipantBettingResultDTO from(final GameResult gameResult) {
        Map<String, Integer> bettingResults = new LinkedHashMap<>();
        bettingResults.put(DEALER_DEFAULT_NAME, gameResult.getDealerRevenue());
        gameResult.getBettingResult().forEach(calculatePlayersRevenue(bettingResults));
        return new ParticipantBettingResultDTO(bettingResults);
    }

    private static BiConsumer<Player, ResultType> calculatePlayersRevenue(final Map<String, Integer> bettingResults) {
        return (player, resultType) -> bettingResults.put(player.getName().getValue(), resultType.getRevenue(player.getBettingMoney()));
    }

    public Map<String, Integer> getBettingResults() {
        return new LinkedHashMap<>(bettingResults);
    }
}
