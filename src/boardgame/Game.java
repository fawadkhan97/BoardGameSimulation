package boardgame;

import java.io.IOException;
import java.util.*;

public class Game implements BoardGame {

    private final Random randomlyGenerateSpecialFields = new Random();
    private final HashMap<Integer, String> specialFieldMap = new HashMap<>();
    private final Random randomDice = new Random();
    private boolean gameInProgress = true;
    private String specialFieldTypeName;
    private Players[] players;
    private int playerTurn;
    private int noOfPlayers;
    private int playerCurrentPosition;
    private int getNextDiceValue;
    private int currentPlayerId;

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        Scanner input = new Scanner(System.in);
        System.out.print("enter number of players between 2-4:  ");

        do {
            // nested do while loop will ensure that user keep looping until user enter a correct number
            do {
                try {
                    game.noOfPlayers = Integer.parseInt(input.nextLine());
                    break;
                } catch (Exception e) {
                    System.out.print("please enter correctly again , enter number of players between 2-4: ");
                }
            } while (true);

            // if players are between 2 and 4 break out of loop and continue execution
            // and if incorrect number are enter restart from beginning
            if (game.noOfPlayers >= 2 && game.noOfPlayers <= 4)
                break;
            else
                System.out.print(" please enter correctly again , enter number of players between 2-4: ");

        } while (true);

        // initialize each player object
        game.initializesPlayer(game.noOfPlayers, game);
        System.out.println("Total players : " + game.noOfPlayers);
        // print location of trap and bonus fieldsx
        System.out.println(game.specialFieldsLocation());

        game.playerTurn = 0;
        System.out.println(">>>>>>>>>>>>>>>>> game has started >>>>>>>>>>>>");

        do {
            game.playerTurn += 1;
            // reset player turn to first player for next round after all players have completed their turn
            game.playerTurn = game.playerTurn > game.noOfPlayers ? 1 : game.playerTurn;


            // check if current player Has Skip Next Round Turn Trap and set its value to false
            if (Boolean.TRUE.equals(game.players[game.playerTurn - 1].getHasSkipNextRoundTrap())) {
                System.out.println(game.players[game.playerTurn - 1].getPlayerName() + " turn has been skipped due to stepping on a trap in previous round");
                game.players[game.playerTurn - 1].setHasSkipNextRoundTrap(false);
                continue;
            }

            System.out.print("\n its " + game.players[game.playerTurn - 1].getPlayerName() + " turn ," + "Current position is: " + game.players[game.playerTurn - 1].getCurrentPosition() +
                    " \n \n >>>>>> " + game.players[game.playerTurn - 1].getPlayerName() + ", press enter key to roll dice : ");

            // read enter key and continue execution
            System.in.read();

            // roll the dice (generate random number 1-6) and store its value
            game.getNextDiceValue = game.rollDice();
            System.out.println("\nDice value is " + game.getNextDiceValue);
            game.playerCurrentPosition = game.players[game.playerTurn - 1].getCurrentPosition() + game.getNextDiceValue;

            // check if moving current player to next position contain some specials field
            if (Boolean.TRUE.equals(game.checkSpecialField(game.playerCurrentPosition, game))) {
                // get special field type name it is either "Bonus" or "Trap"
                game.specialFieldTypeName = game.specialFieldMap.get(game.playerCurrentPosition);
                // get current player id
                game.currentPlayerId = game.players[game.playerTurn - 1].getId();
                // call special field type respective method
                if (game.specialFieldTypeName.equals("Trap")) {
                    if (Boolean.TRUE.equals(game.players[game.currentPlayerId - 1].getHasJoker())) {
                        System.out.println(game.players[game.currentPlayerId - 1].getPlayerName() + " has a joker card , Trap will have no effect at this turn");

                    } else {
                        game.trapField(game.playerCurrentPosition, game.currentPlayerId, game);
                    }

                } else {
                    game.bonusField(game.playerCurrentPosition, game.currentPlayerId, game);
                }

                // update player position to latest
                game.players[game.playerTurn - 1].setCurrentPosition(game.playerCurrentPosition);
                // display current position
                game.displayPlayerCurrentPosition(game);

            } else {
                game.players[game.playerTurn - 1].setCurrentPosition(game.playerCurrentPosition);
                game.displayPlayerCurrentPosition(game);

            }
            // if player reached desired goal
            if (game.players[game.playerTurn - 1].getCurrentPosition() > 30) {
                game.gameOver(game);
            }

        } while (game.gameInProgress);
    }

    @Override
    public void bonusField(int playerCurrentPosition, int id, Game game) {
        int typeValue = randomlyGenerateSpecialFields.nextInt(3) + 1;

        if (typeValue == 1) {
            System.out.println("Yayy!! Bonus type 1, players position will be incremented by 2 , current position is " + game.players[id - 1].getCurrentPosition());
            game.playerCurrentPosition = game.playerCurrentPosition + 2;
        } else if (typeValue == 3) {
            System.out.println("Yayy!! Bonus type 3,  " + game.players[game.playerTurn - 1].getPlayerName() + " now have a joker");
            game.players[id - 1].setHasJoker(true);
        }
        // decrement all other players position to +2
        else {
            System.out.println("Yayy!! Bonus type 1 , all other players position will be decremented by 2");
            // increment position of all other players (excluding our current player) by +2
            for (Players player : players) {
                // skip current player
                if (player.getId() == id) continue;
                //decrement current position of player ,  if current position of player  after decrement is less than zero reset it to zero
                player.setCurrentPosition(player.getCurrentPosition() - 2 >= 0 ? player.getCurrentPosition() : 0);

            }

        }

    }

    @Override
    public void trapField(int playerCurrentPosition, int id, Game game) {
        int typeValue = randomlyGenerateSpecialFields.nextInt(3) + 1;

        // increment player position by 2
        if (typeValue == 1) {
            System.out.println("Oops!!! Trap type 1 " + game.players[game.playerTurn - 1].getPlayerName() + " position has been decremented by 2 , current position is " + game.players[id - 1].getCurrentPosition());
            game.playerCurrentPosition = game.playerCurrentPosition - 2;

        }
        // set skip next round turn of player to true and if its true player next round turn will be skipped
        else if (typeValue == 3) {
            System.out.println("Oops!!! Trap type 3, its " + game.players[game.playerTurn - 1].getPlayerName() + " next round turn will be skipped");
            game.players[id - 1].setHasSkipNextRoundTrap(true);
        }
        // increment all other players position to +2
        else {
            System.out.println("Oops!! Trap type 2 , all other players position will be incremented by 2");
            for (Players player : players) {
                if (player.getId() == id) continue;
                player.setCurrentPosition(player.getCurrentPosition() + 2);
            }
        }
    }

    @Override
    public Boolean checkSpecialField(int position, Game game) {

        if (specialFieldsLocation().containsKey(position)) {
            specialFieldTypeName = specialFieldMap.get(position);
            System.out.println(game.players[game.playerTurn - 1].getPlayerName() + " has landed on a  " + specialFieldTypeName + " player current position is " + position);
            return true;
        }
        return false;
    }

    @Override
    public void gameOver(Game game) {
        // if player score has reached greater than 30
        System.out.println(">>>>>>>>>GAME OVER >>>>>>");
        System.out.println(game.players[game.playerTurn - 1].getPlayerName() + " has won the game ");

        System.out.println(">>>>>>>Scores are >>>>>>>");
        for (int i = 0; i < game.noOfPlayers; i++) {
            System.out.println("Name = " + game.players[i].getPlayerName() + "\t Scores = " + game.players[i].getCurrentPosition());
        }

        game.gameInProgress = false;
    }

    @Override
    public int rollDice() {
        return randomDice.nextInt(6) + 1;
    }

    @Override
    public void initializesPlayer(int noOfPlayers, Game game) {
        players = new Players[noOfPlayers];
        // initialize players
        for (int i = 0; i < players.length; i++) {
            System.out.print("enter player" + (i + 1) + " name: ");
            Scanner inputPlayerName = new Scanner(System.in);

            game.players[i] = new Players(i + 1, inputPlayerName.nextLine());
            System.out.println("player name : " + game.players[i].getPlayerName() + "\t player id: " + game.players[i].getId());
        }


    }

    @Override
    public Map<Integer, String> specialFieldsLocation() {
        // create a set of 10 unique values
        final Set<Integer> specialFieldSet = new HashSet<>(10);
        while (specialFieldSet.size() < 10) {
            specialFieldSet.add(randomlyGenerateSpecialFields.nextInt(30 + 1));
        }
        //add uniques values as key to Hashmap
        int counter = 0;
        for (Integer s : specialFieldSet) {
            counter++;
            // set first  5 values as traps
            if (counter % 2 == 0)
                specialFieldMap.put(s, "Trap");
            else specialFieldMap.put(s, "Bonus");

        }


        return specialFieldMap;
    }

    @Override
    public void displayPlayerCurrentPosition(Game game) {
        System.out.println(game.players[game.playerTurn - 1].getPlayerName() + " current position updated to " + game.players[game.playerTurn - 1].getCurrentPosition());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
