package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class Puzzle22 implements Puzzle<Long> {

    List<CombatGamePlayer> players = new ArrayList<>();

    @Override
    public void init(Path file) throws Exception {
        CombatGamePlayer player = null;
        for (String line : Files.readAllLines(file)) {
            if (line.startsWith("Player")) {
                player = new CombatGamePlayer(line.substring(0, line.length() - 1), new ArrayDeque<>());
            } else if (line.isEmpty()) {
                players.add(player);
            } else {
                player.addCards(Integer.parseInt(line));
            }
        }
        // Add last player
        players.add(player);
    }

    @Override
    public Long part1() throws Exception {
        CombatGame game = new CombatGame(getPlayer1(), getPlayer2());
        while (!game.gameEnded()) {
            game.round();
        }
        return game.getGameWinner().calculatePoints();
    }

    @Override
    public Long part2() throws Exception {
        RecursiveCombatGame game = new RecursiveCombatGame(getPlayer1(), getPlayer2());
        while (!game.gameEnded()) {
            game.round();
        }
        return game.getGameWinner().calculatePoints();
    }

    CombatGamePlayer getPlayer1() {
        return newPlayer(players.get(0));
    }

    CombatGamePlayer getPlayer2() {
        return newPlayer(players.get(1));
    }

    CombatGamePlayer newPlayer(CombatGamePlayer player) {
        return new CombatGamePlayer(player.getName(), new ArrayDeque<>(player.getAllCards()));
    }
}

class CombatGamePlayer {
    String name;
    Queue<Integer> cards;

    public CombatGamePlayer(String name, Queue<Integer> cards) {
        this.name = name;
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getAllCards() {
        return new ArrayList<>(cards);
    }

    public String getDeckString() {
        return cards.toString();
    }

    public Integer drawCard() {
        return cards.poll();
    }

    public void addCards(int... newCards) {
        Arrays.stream(newCards).forEach(cards::add);
    }

    public boolean hasCards() {
        return !cards.isEmpty();
    }

    public Long calculatePoints() {
        List<Integer> allCards = getAllCards();
        return allCards.stream().mapToLong(c -> c * (allCards.size() - allCards.indexOf(c))).sum();
    }
}

class CombatGame {
    CombatGamePlayer player1;
    CombatGamePlayer player2;

    public CombatGame(CombatGamePlayer player1, CombatGamePlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Play a round of Combat
     */
    void round() {
        if (!gameEnded()) {
            Integer card1 = player1.drawCard();
            Integer card2 = player2.drawCard();

            if (card1 > card2) {
                player1.addCards(card1, card2);
            } else {
                player2.addCards(card2, card1);
            }
        }
    }

    public boolean gameEnded() {
        return !player1.hasCards() || !player2.hasCards();
    }

    public CombatGamePlayer getGameWinner() {
        if (!player1.hasCards()) {
            return player2;
        } else if (!player2.hasCards()) {
            return player1;
        } else {
            throw new IllegalStateException("Game still in progress");
        }
    }
}

class RecursiveCombatGame extends CombatGame {
    List<String> historyPlayer1 = new ArrayList<>();
    List<String> historyPlayer2 = new ArrayList<>();

    public RecursiveCombatGame(CombatGamePlayer player1, CombatGamePlayer player2) {
        super(player1, player2);
    }

    /**
     * Play a round of Recursive Combat
     */
    @Override
    void round() {
        if (!gameEnded()) {

            updateHistory();

            Integer card1 = player1.drawCard();
            Integer card2 = player2.drawCard();

            CombatGamePlayer winner;
            if (player1.getAllCards().size() >= card1 && player2.getAllCards().size() >= card2) {
                // Play recursive game
                CombatGamePlayer subPlayer1 = new CombatGamePlayer(player1.getName(),
                        new ArrayDeque<>(player1.getAllCards().subList(0, card1)));
                CombatGamePlayer subPlayer2 = new CombatGamePlayer(player2.getName(),
                        new ArrayDeque<>(player2.getAllCards().subList(0, card2)));
                RecursiveCombatGame subgame = new RecursiveCombatGame(subPlayer1, subPlayer2);
                while (!subgame.gameEnded()) {
                    subgame.round();
                }
                winner = subgame.getGameWinner().equals(subPlayer1) ? player1 : player2;
            } else if (card1 > card2) {
                winner = player1;
            } else {
                winner = player2;
            }

            if (winner == player1) {
                player1.addCards(card1, card2);
            } else {
                player2.addCards(card2, card1);
            }
        }
    }

    private void updateHistory() {
        historyPlayer1.add(player1.getDeckString());
        historyPlayer2.add(player2.getDeckString());
    }

    @Override
    public boolean gameEnded() {
        return !player1.hasCards() || !player2.hasCards() || isRepeatedSequence();
    }

    @Override
    public CombatGamePlayer getGameWinner() {
        if (isRepeatedSequence()) {
            return player1;
        } else {
            return super.getGameWinner();
        }
    }

    private boolean isRepeatedSequence() {
        return historyPlayer1.contains(player1.getDeckString()) && historyPlayer2.contains(player2.getDeckString());
    }

}
