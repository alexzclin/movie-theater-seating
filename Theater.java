import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Theater {
    private Status[][] seatAvailable; // available, reserved, or buffer
    private int numRows;
    private int totalSeats;
    private int reserved;
    private int buffer;
    private int[] seatsLeftInRow;

    public Theater(int rows, int cols) {
        seatAvailable = new Status[rows][cols];
        for (int i = 0; i < seatAvailable.length; i++) {
            for (int j = 0; j < seatAvailable[i].length; j++) {
                seatAvailable[i][j] = Status.AVAILABLE;
            }
        }
        numRows = rows;
        totalSeats = rows * cols;
        reserved = 0;
        seatsLeftInRow = new int[rows];
        for (int i = 0; i < seatsLeftInRow.length; i++)
            seatsLeftInRow[i] = 20;
    }

    /*
    Priorities:
    1. Group seated together in the same row
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
        // will be able to rollback if we realize that we cannot fit the group
        if (row == -1) {
            List<int[]> rollbackReserved = new LinkedList<>();
            List<int[]> rollbackBuffered = new LinkedList<>();
            while (num > 0) {
                if (totalSeats - reserved - buffer < num) {
                    for (int[] locs: rollbackReserved) {
                        seatAvailable[locs[0]][locs[1]] = Status.AVAILABLE;
                        seatsLeftInRow[locs[0]]++;
                        reserved--;
                    }
                    for (int[] locs: rollbackBuffered) {
                        seatAvailable[locs[0]][locs[1]] = Status.AVAILABLE;
                        seatsLeftInRow[locs[0]]++;
                        buffer--;
                    }
                    return null;
                }
                int maxRow = -1, maxSpace = 0;
                distToMid = numRows;
                for (int i = 0; i < seatsLeftInRow.length; i++) {
                    int dist = Math.abs(seatsLeftInRow.length / 2 - i);
                    if (seatsLeftInRow[i] == num && dist < distToMid) {
                        maxSpace = Integer.MAX_VALUE;
                        maxRow = i;
                        distToMid = dist;
                    } else if (seatsLeftInRow[i] >= maxSpace && dist < distToMid) {
                        maxRow = i;
                        maxSpace = seatsLeftInRow[i];
                        distToMid = dist;
                    }
                }
                num -= seatsLeftInRow[maxRow];
                reserved += seatsLeftInRow[maxRow];
                seatsLeftInRow[maxRow] = 0;
                
                for (int col = 0; col < seatAvailable[maxRow].length; col++) {
                    if (seatAvailable[maxRow][col] == Status.AVAILABLE) {
                        seatLocs.add(new int[]{maxRow, col});
                        seatAvailable[maxRow][col] = Status.RESERVED;
                    }
                }
                for (int[] loc: seatLocs) {
                    rollbackReserved.add(loc);
                    rollbackBuffered.addAll(createBubble(loc[0], loc[1]));
                }
            }
        } else {
            int col = 0;
            seatsLeftInRow[row] -= num;
            reserved += num;
            while (num > 0 && col < seatAvailable[row].length) {
                if (seatAvailable[row][col] == Status.AVAILABLE) {
                    seatLocs.add(new int[]{row, col});
                    seatAvailable[row][col] = Status.RESERVED;
                    num--;
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

    private List<int[]> createBubble(int row, int col) {
        List<int[]> buffered = new LinkedList<>();
        int[] vertical = {-1, 0, 1};
        int[] horizontal = {-3, -2, -1, 0, 1, 2, 3};
        for (int i: vertical) {
            for (int j: horizontal) {
                if (inBounds(row + i, col + j) && (i != 0 || j != 0)) {
                    if (seatAvailable[row + i][col + j] == Status.AVAILABLE) {
                        seatAvailable[row + i][col + j] = Status.BUFFER;
                        buffer++;
                        seatsLeftInRow[row + i]--;
                        buffered.add(new int[]{row + i, col + j});
                    }
                }
            }
        }
        return buffered;
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < numRows && c >= 0 && c < seatAvailable[r].length;
    }

    enum Status {
        AVAILABLE,
        RESERVED,
        BUFFER
    }

    public void printSeatMap() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < seatAvailable[i].length; j++) {
                if (seatAvailable[i][j] == Status.AVAILABLE)
                    System.out.print("_ ");
                else if (seatAvailable[i][j] == Status.RESERVED)
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
    }
}
