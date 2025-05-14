package at.aau.serg.scotlandyard.dto;

public class OwnPositionMessage {
    private final int position;

    public OwnPositionMessage(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
