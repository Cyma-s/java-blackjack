package blackjack.view;

import blackjack.dto.BlackJackGameResultDTO;
import blackjack.dto.ParticipantCardsDTO;
import blackjack.dto.ParticipantEntireStatusDTO;
import blackjack.dto.ParticipantStatusDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OutputView {
    private static final int NOT_EXIST_MATCH_RESULT = 0;
    private static final String DEALER_DEFAULT_NAME = "딜러";

    private OutputView() {
    }

    public static void printParticipantsInitCards(final ParticipantCardsDTO cardsDTO) {
        List<ParticipantStatusDTO> participantCards = cardsDTO.getParticipantCards();
        System.out.println(String.format("%s와 %s에게 2장을 나누었습니다.", DEALER_DEFAULT_NAME,
                String.join(", ", cardsDTO.getPlayerNames()))
                + System.lineSeparator());
        String participantInitCards = participantCards.stream()
                .map(participantStatus -> getParticipantCards(participantStatus.getName(), participantStatus.getCards())
                        + System.lineSeparator())
                .collect(Collectors.joining());

        System.out.println(participantInitCards + System.lineSeparator());
    }

    public static void printDealerHit(final int hitCount) {
        System.out.printf("딜러는 16이하라 %d장의 카드를 더 받았습니다.\n" + System.lineSeparator(), hitCount);
    }

    public static void printParticipantsCards(final ParticipantStatusDTO participantStatusDTO) {
        String playerCards = getParticipantCards(participantStatusDTO.getName(), participantStatusDTO.getCards());

        System.out.println(playerCards + System.lineSeparator());
    }

    public static void printParticipantCardWithResult(final ParticipantEntireStatusDTO entireStatusDTO) {
        ParticipantStatusDTO statusDTO = entireStatusDTO.getStatusDTO();
        String playerCards = getParticipantCards(statusDTO.getName(), statusDTO.getCards());
        String playerTotalPoint = String.format(" - 결과: %d", entireStatusDTO.getScore());
        System.out.println(playerCards + playerTotalPoint);
    }

    public static void printBlackJackResults(final BlackJackGameResultDTO gameResultDTO) {
        System.out.println(System.lineSeparator() + "## 최종 승패");
        System.out.println(makeBlackJackResultFormat(gameResultDTO.getGameResults()));
    }

    private static String makeBlackJackResultFormat(final Map<String, List<String>> blackJackResults) {
        Set<String> participants = blackJackResults.keySet();
        StringBuilder stringBuilder = new StringBuilder();

        for (final String name : participants) {
            List<String> gameResults = blackJackResults.get(name);
            Map<String, Long> results = gameResults.stream()
                    .collect(Collectors.groupingBy(String::valueOf, Collectors.counting()));
            stringBuilder.append(getParticipantResult(name, results));
        }

        return stringBuilder.toString();
    }

    private static String getParticipantCards(final String participantName, final List<String> participantCards) {
        return participantName
                + " 카드: "
                + String.join(", ", participantCards);
    }

    private static String getParticipantResult(final String name, final Map<String, Long> results) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append(": ");
        results.entrySet().stream()
                .filter(entry -> entry.getValue() > NOT_EXIST_MATCH_RESULT)
                .forEach(entry -> stringBuilder.append(entry.getValue()).append(entry.getKey()).append(" "));
        stringBuilder.append(System.lineSeparator());
        return stringBuilder.toString();
    }
}
