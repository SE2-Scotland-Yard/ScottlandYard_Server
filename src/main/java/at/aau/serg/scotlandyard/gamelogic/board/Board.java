package at.aau.serg.scotlandyard.gamelogic.board;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Map<Integer, List<Edge>> adjacencyList = new HashMap<>();

    public Board() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("board.json")) {
            TypeReference<Map<Integer, List<Edge>>> typeRef = new TypeReference<>() {};
            adjacencyList = mapper.readValue(input, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Edge> getConnectionsFrom(int node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }
}
