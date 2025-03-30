package at.aau.serg.scotlandyard.gamelogic.board;

public class Edge {
    int from;
    int to;
    Transport type;
    public Edge(int from, int to, Transport type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }
}
