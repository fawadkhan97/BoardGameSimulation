package boardgame;

import java.io.IOException;
import java.util.*;

public class Game {

    private static final Random randomSpecialFields = new Random();
    private static Players[] players;
    private static int playerTurn;
    private final HashMap<Integer, String> specialFieldMap = new HashMap<>();
    private final Random randomDice = new Random();
    boolean gameInProgress = true;
    String playingFieldType;
    private int noOfPlayers;
    private int playerCurrentPosition;
    private int getNextDiceValue;
    private int currentPlayerId;

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        Scanner input = new Scanner(System.in);

        System.out.print("enter number of players between 2-4:  ");
        game.noOfPlayers = input.nextInt();
        game.initializesPlayer(game.noOfPlayers);
        System.out.println("Total players :" + game.noOfPlayers);
        System.out.println(game.specialFieldsLocation());

        playerTurn = 0;
        System.out.println(">>>>>>>>>>>>>>>>> game has started >>>>>>>>>>>>");

        do {
            playerTurn += 1;
            // reset player turn to first player for next round after all players have completed their turn
            if (playerTurn > game.noOfPlayers) {
                playerTurn = 1;
            }

            // check if current player Has Skip Next Round Turn Trap and set its value to false
            if (Boolean.TRUE.equals(players[playerTurn - 1].getHasSkipNextRoundTrap())) {
                System.out.println("player" + playerTurn + " turn will be skipped due to  stepping on a trap in previous round");
                players[playerTurn - 1].setHasSkipNextRoundTrap(false);
                continue;
            }

            System.out.print("\n its player" + playerTurn + " turn ," + "Current position is: " + players[playerTurn - 1].getCurrentPosition() +
                    " \n \n >>>>>> " + "player" + playerTurn + ", press enter key to roll dice : ");
            // read enter key and continue execution
            System.in.read();
            game.getNextDiceValue = game.rollDice();
            System.out.println("\nDice value is " + game.getNextDiceValue);
            game.playerCurrentPosition = players[playerTurn - 1].getCurrentPosition() + game.getNextDiceValue;

            // check if moving current player to next position contain some specials field
            if (Boolean.TRUE.equals(game.checkSpecialField(game.playerCurrentPosition))) {
                // get special field type  is +either "Bonus" or "Trap"
                game.playingFieldType = game.specialFieldMap.get(game.playerCurrentPosition);
                game.currentPlayerId = players[playerTurn - 1].getId();

                // call special field type respective method
                if (game.playingFieldType.equals("Trap")) {
                    if (Boolean.TRUE.equals(players[game.currentPlayerId - 1].hasJoker)) {
                        System.out.println("player has a joker card , Trap will have no effect at this turn");
                    } else
                        trapField(game.playerCurrentPosition, game.currentPlayerId);

                } else bonusField(game.playerCurrentPosition, game.currentPlayerId);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            } else {
                players[playerTurn - 1].setCurrentPosition(game.playerCurrentPosition);
                System.out.println("\nplayer" + playerTurn + " current position updated to " + players[playerTurn - 1].getCurrentPosition());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            }
            // if player reached desired goal
            if (players[playerTurn - 1].getCurrentPosition() > 30) {
                game.gameOver(game);
            }

        } while (game.gameInProgress);
    }

    public static void bonusField(int playerCurrentPosition, int id) {
        int typeValue = randomSpecialFields.nextInt(3) + 1;

        if (typeValue == 1) {
            System.out.println("Yayy!! Bonus type 1, player position will be incremented by 2 , current position is " + players[id - 1].getCurrentPosition());

            players[id - 1].setCurrentPosition(playerCurrentPosition + 2);
            System.out.println("\nplayer" + playerTurn + " current position updated to " + players[playerTurn - 1].getCurrentPosition());

        } else if (typeValue == 3) {
            System.out.println("Yayy!! Bonus type 3,  player now have a joker");

            players[id - 1].setHasJoker(true);
        }
        // decrement all other players position to +2
        else {
            System.out.println("Yayy!! Bonus type 1 , all other player position will be decremented by 2");
            for (Players player : players) {
                // skip current player
                if (player.getId() == id) continue;
                //decrement current position of player ,  if current position of player  after decrement is less than zero reset it to zero
                player.setCurrentPosition(player.getCurrentPosition() - 2 >= 0 ? player.getCurrentPosition() : 0);

            }

        }

    }

    public static void trapField(int playerCurrentPosition, int id) {
        int typeValue = randomSpecialFields.nextInt(3) + 1;

        // increment player position by 2
        if (typeValue == 1) {
            System.out.println("Oops!!! Trap type 1 player position has been decremented by 2 , current position is " + players[id - 1].getCurrentPosition());
            players[id - 1].setCurrentPosition(playerCurrentPosition - 2);
            System.out.println("\nplayer" + playerTurn + " current position updated to " + players[playerTurn - 1].getCurrentPosition());

        }
        // set skip next round turn of player to true and if its true player next round turn will be skipped
        else if (typeValue == 3) {
            System.out.println("Oops!!! Trap type 3, player" +playerTurn +" next round turn will be skipped");
            players[id - 1].setHasSkipNextRoundTrap(true);
        }
        // increment all other players position to +2
        else {
            System.out.println("Oops!! Trap type 2 , all other player position will be incremented by 2");
            for (Players player : players) {
                if (player.getId() == id) continue;
                player.setCurrentPosition(player.getCurrentPosition() + 2);
            }
        }
    }

    public Boolean checkSpecialField(int position) {

        if (specialFieldsLocation().containsKey(position)) {
            playingFieldType = specialFieldMap.get(position);
            System.out.println("player" + playerTurn + "has landed on a  " + playingFieldType);
            return true;
        }
        return false;
    }

    public void gameOver(Game game) {
        // if player score has reached greater than 30
        System.out.println(">>>>>>>>>GAME OVER >>>>>>");
        System.out.println("player" + playerTurn + " has won the game ");

        System.out.println(">>>>>>>Scores are >>>>>>>");
        for (int i = 0; i < game.noOfPlayers; i++) {
            System.out.println("Name = player" + i + 1 + "\t Scores = " + players[i].getCurrentPosition());
        }

        game.gameInProgress = false;
    }

    public int rollDice() {
        return randomDice.nextInt(6) + 1;
    }

    public void initializesPlayer(int noOfPlayers) {
        players = new Players[noOfPlayers];
        // initialize players
        for (int i = 1; i <= players.length; i++) {
            players[i - 1] = new Players(i);
        }


    }

    public Map<Integer, String> specialFieldsLocation() {
        // create a set of 10 unique values
        final Set<Integer> specialFieldSet = new HashSet<>(10);
        while (specialFieldSet.size() < 10) {
            specialFieldSet.add(randomSpecialFields.nextInt(30 + 1));
        }
        //add uniques values as key to Hashmap
        int counter = 0;
        for (Integer s : specialFieldSet) {
            counter++;
            // set first  5 values as traps
            if (counter < 6)
                specialFieldMap.put(s, "Trap");
            else specialFieldMap.put(s, "Bonus");

        }

        return specialFieldMap;
    }
}
