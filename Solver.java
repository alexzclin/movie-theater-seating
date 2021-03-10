import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

class Solver {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please provide one input file and one output file!");
            System.exit(0);
        }

        Theater movie = new Theater(10, 20);

        try (FileReader file = new FileReader(args[0])) {
            BufferedReader in = new BufferedReader(file);
            PrintWriter out = new PrintWriter(args[1]);
            String curLine;
            while ((curLine = in.readLine()) != null) {
                String[] info = curLine.split(" ");
                String confirmation = movie.reserveSeats(Integer.parseInt(info[1]));
                if (confirmation == null) {
                    out.append(info[0] + " " + "FULL" + "\n");
                    System.out.println("Max capacity reached, cannot complete order " + info[0]);
                } else {
                    out.append(info[0] + " " + confirmation + "\n");
                }
            }
            out.close();
            movie.printSeatMap();
            movie.printData();
        } catch (IOException e) {
            System.out.println("No input file to be loaded.");
            System.exit(0);
        }
    }
}
