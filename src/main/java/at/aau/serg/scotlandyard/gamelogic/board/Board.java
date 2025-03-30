package at.aau.serg.scotlandyard.gamelogic.board;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {

    //nodes are the integers 1-199
    ArrayList<Edge> edges = new ArrayList<>();
    public Board() {
        try {
            File myObj = new File("edges.csv");
            Scanner myReader = new Scanner(myObj);
            myReader.nextLine();
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split(",");
                Transport type = Transport.valueOf(data[0].toUpperCase());
                int x = Integer.parseInt(data[1]);
                int y = Integer.parseInt(data[2]);
                edges.add(new Edge(x, y, type));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
