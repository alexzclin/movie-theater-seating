import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class Theater {

    // customer satisfaction is higher for seats toward the middle
    // customer satisfaction is higher when tickets are together
    // note that we need disability seats
    // handle cases when large party
    // add safety scores at least 3 seats and one row (bubble)
    // consider order of ticket buying
    // have two options, best offer from customer standpoint and best offer from our standpoint
    // had three seating options initially 1. bubble idea 2. 3 seat buffer

    private int[][] seatAvailable; // 0: available, 1: reserved, 2: buffer
    private int numRows;
    private int totalSeats;
    private int reserved;
    private int buffer;
    private int[] seatsLeftInRow;
    private Map<Integer, Map<Integer, List<Integer>>> seatSatisfaction;
    // maps satisfaction score to a map containing seat location (maps row to column)

    public Theater(int rows, int cols) {
        seatAvailable = new int[rows][cols];
        numRows = rows;
        seatSatisfaction = new TreeMap<>(Collections.reverseOrder());
        initSatisfaction(seatAvailable);
        totalSeats = rows * cols;
        reserved = 0;
        seatsLeftInRow = new int[rows];
        for (int i = 0; i < seatsLeftInRow.length; i++)
            seatsLeftInRow[i] = 20;
    }

    // higher satisfaction for the middle seats
    private void initSatisfaction(int[][] seats) {
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                int satisfaction;
                if (i < seats.length / 3) {
                    satisfaction = i + 1;
                } else if (i > 2 * seats.length / 3) {
                    satisfaction = Math.abs(i - 10);
                } else {
                    satisfaction = 5;
                }
                if (j >= seats[i].length / 4 && j < 3 * seats[i].length / 4)
                    satisfaction++;

                Map<Integer, List<Integer>> inner = seatSatisfaction.getOrDefault(satisfaction, new TreeMap<>());
                List<Integer> pos = inner.getOrDefault(i, new LinkedList<>());
                pos.add(j);
                inner.put(i, pos);
                seatSatisfaction.put(satisfaction, inner);
            }
        }
    }

    /*
    Priorities:
    1. Group seated together (in the same row for now)
    2. Have good seats (towards the middle rows)
    */
    public String reserveSeats(int num) {
        if (totalSeats - reserved - buffer < num)
            return null;

        List<int[]> seatLocs = new ArrayList<>(num);
        StringBuilder ansString = new StringBuilder();
        int row = -1, distToMid = numRows;
        for (int i = 0; i < seatsLeftInRow.length; i++) {
            int dist = Math.abs(seatsLeftInRow.length / 2 - i);
            if (seatsLeftInRow[i] >= num && dist < distToMid) {
                row = i;
                distToMid = dist;
            }
        }

        // cannot fit the group in one row, so split group and find rows with most capacity to put them in
        // break ties for rows with most capacity by finding the one closest to the middle
        if (row == -1) {
            // while (num > 0) {
            //     int maxRow = -1, maxSpace = 0;
            //     distToMid = numRows;
            //     for (int i = 0; i < seatsLeftInRow.length; i++) {
            //         int dist = Math.abs(seatsLeftInRow.length / 2 - i);
            //         if (seatsLeftInRow[i] >= maxSpace && dist < distToMid) {
            //             maxRow = i;
            //             maxSpace = seatsLeftInRow[i];
            //             distToMid = dist;
            //         }
            //     }
            //     for (int col = 0; col < seatAvailable[maxRow].length; col++) {
            //         if (seatAvailable[maxRow][col] == 0) {
            //             seatLocs.add(new int[]{row, col});
            //             seatAvailable[row][col] = 1;
            //             num--;
            //             reserved++;
            //         }
            //     }
            //     for (int[] loc: seatLocs) {
            //         createBubble(loc[0], loc[1]);
            //     }
            // }
        } else {
            int col = 0;
            seatsLeftInRow[row] -= num;
            while (num > 0 && col < seatAvailable[row].length) {
                if (seatAvailable[row][col] == 0) {
                    seatLocs.add(new int[]{row, col});
                    seatAvailable[row][col] = 1;
                    num--;
                    reserved++;
                }
                col++;
            }
        }

        for (int[] loc: seatLocs) {
            ansString.append((char) (loc[0] + 65) + Integer.toString(loc[1] + 1) + ",");
            createBubble(loc[0], loc[1]);
        }
        ansString.deleteCharAt(ansString.length()-1);
        return ansString.toString();
    }

    private void createBubble(int row, int col) {
        int[] vertical = {-1, 0, 1};
        int[] horizontal = {-3, -2, -1, 0, 1, 2, 3};
        for (int i: vertical) {
            for (int j: horizontal) {
                if (inBounds(row + i, col + j) && (i != 0 || j != 0)) {
                    if (seatAvailable[row + i][col + j] == 0) {
                        seatAvailable[row + i][col + j] = 2;
                        buffer++;
                        seatsLeftInRow[row + i]--;
                    }
                }
            }
        }
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < numRows && c >= 0 && c < seatAvailable[r].length;
    }

    public void printSeatSatisfaction() {
        for (Map.Entry<Integer, Map<Integer, List<Integer>>> e : seatSatisfaction.entrySet()) {
            System.out.println(e.getKey() + ": ");
            Map<Integer, List<Integer>> inner = e.getValue();
            for (Map.Entry<Integer, List<Integer>> innerE: inner.entrySet()) {
                for (int col: innerE.getValue()) {
                    System.out.println("[" + innerE.getKey() + "," + col + "]");
                }
            }
        }
    }

    public void printSeatMap() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < seatAvailable[i].length; j++) {
                if (seatAvailable[i][j] == 0)
                    System.out.print("_ ");
                else if (seatAvailable[i][j] == 1)
                System.out.print("O ");
                else
                    System.out.print("X ");
            }
            System.out.println();
        }
    }

    public void printData() {
        System.out.println("Total Seats: " + totalSeats);
        System.out.println("Available Seats: " + (totalSeats - reserved - buffer));
        System.out.println("Number of Seats Sold: " + reserved);
        System.out.println("Number of Buffer Seats: " + buffer);
        for (int i = 0; i < seatsLeftInRow.length; i++) {
            System.out.println("Seats left in row " + (char) (i + 65) + ": " + seatsLeftInRow[i]);
        }
    }
}
