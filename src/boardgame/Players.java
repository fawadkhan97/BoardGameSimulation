package boardgame;

public class Players {
    int id;
    int currentPosition;
    Boolean hasJoker;
    Boolean hasSkipNextRoundTrap;

    public Players(int id) {
        this.id = id;
        currentPosition = 0;
        hasJoker = false;
        hasSkipNextRoundTrap = false;
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
