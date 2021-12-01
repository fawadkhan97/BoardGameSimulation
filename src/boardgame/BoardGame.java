package boardgame;

import java.util.Map;

public interface BoardGame {

    public void bonusField(int playerCurrentPosition, int id, Game game);

    public void trapField(int playerCurrentPosition, int id, Game game);

    public void displayPlayerCurrentPosition(Game game);

    public Boolean checkSpecialField(int position, Game game);

    public void gameOver(Game game);

    public int rollDice();

    public void initializesPlayer(int noOfPlayers, Game game);

    public Map<Integer, String> specialFieldsLocation();


}
