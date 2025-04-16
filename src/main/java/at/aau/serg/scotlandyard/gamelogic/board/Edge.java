package at.aau.serg.scotlandyard.gamelogic.board;

import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;

public class Edge {
    private int to;
    private Ticket ticket;


    public Edge() {
    }


    public Edge(int to, Ticket ticket) {
        this.to = to;
        this.ticket = ticket;
    }


    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTransport(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Edge{to=" + to + ", transport=" + ticket + '}';
    }
}
