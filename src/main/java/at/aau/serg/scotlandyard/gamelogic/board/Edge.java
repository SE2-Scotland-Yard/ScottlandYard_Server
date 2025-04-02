package at.aau.serg.scotlandyard.gamelogic.board;

public class Edge {
    private int to;
    private Transport transport;


    public Edge() {
    }


    public Edge(int to, Transport transport) {
        this.to = to;
        this.transport = transport;
    }


    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return "Edge{to=" + to + ", transport=" + transport + '}';
    }
}
