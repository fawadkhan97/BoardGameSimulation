package boardgame;

public class Players {
   private int id;
   private String playerName;
   private int currentPosition;
   private Boolean hasJoker;
   private Boolean hasSkipNextRoundTrap;

    public Players(int id, String name) {
        this.id = id;
        this.playerName = name;
        currentPosition = 0;
        hasJoker = false;
        hasSkipNextRoundTrap = false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Boolean getHasJoker() {
        return hasJoker;
    }

    public void setHasJoker(Boolean hasJoker) {
        this.hasJoker = hasJoker;
    }

    public Boolean getHasSkipNextRoundTrap() {
        return hasSkipNextRoundTrap;
    }

    public void setHasSkipNextRoundTrap(Boolean hasSkipNextRoundTrap) {
        this.hasSkipNextRoundTrap = hasSkipNextRoundTrap;
    }
}
