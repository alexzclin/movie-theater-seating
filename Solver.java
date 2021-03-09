import java.io.*;

class Solver {

    public static void main(String[] args) {
        Theater movie = new Theater(10, 20);

        try (FileReader file = new FileReader("input.txt")) {
            BufferedReader in = new BufferedReader(file);
            String curLine;
            while ((curLine = in.readLine()) != null) {
                System.out.println(curLine);
            }
        } catch (IOException e) {
            System.out.println("No input file to be loaded.");
            System.exit(0);
        }

        try (PrintWriter out = new PrintWriter("output.txt")) {
            out.write("Hello World");
        } catch (FileNotFoundException e) {
            System.exit(0);
        }
    }
}