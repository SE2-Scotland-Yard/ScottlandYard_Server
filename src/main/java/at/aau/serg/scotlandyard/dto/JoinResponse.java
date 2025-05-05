package at.aau.serg.scotlandyard.dto;

public class JoinResponse {
    private String message;

    public JoinResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
