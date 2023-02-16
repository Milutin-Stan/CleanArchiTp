import java.util.Scanner;
import java.util.Random;
//Milutin Stanojevic
public class GuessTheNumberGame {
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }

    public static class Game{
        NumberGenerator numberGenerator;
        InputReader inputReader;
        OutputWriter outputWriter;
        private boolean continuePlaying;
        private int userGuess;
        private int attempts;
        private int maxAttempts;

        public Game(){
            this.userGuess = 0;
            this.attempts = 0;
            this.maxAttempts = 10;
            this.continuePlaying = true;
            this.numberGenerator = new NumberGenerator();
            this.inputReader = new InputReader();
            this.outputWriter = new OutputWriter();
        }

        public void play(){

           outputWriter.writeTitle();

           gameLoop();

            System.out.println("== Thanks for playing! ==");
            inputReader.close();
        }

        private void gameLoop() {
            this.userGuess = 0;
            this.attempts = 0;
            this.maxAttempts = 10;

            while(continuePlaying) {
                outputWriter.writeRules();
                int number = numberGenerator.generate(1, 100);
                outputWriter.writeDebugNumber(number);

                //TODO On peut decouper même cette partie la dans une roundLoop()
                while (number != userGuess && attempts < maxAttempts) {
                    String input = inputReader.readInput();
                    attempts++;
                    try {
                        userGuess = Integer.parseInt(input);
                        if (userGuess == number) {
                            outputWriter.writeWinMessage(attempts);
                        } else {
                            String divergence = userGuess < number ? "smaller" : "greater";
                            outputWriter.writeNotFoundMessage(divergence, maxAttempts, attempts);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Your input was '" + input + "', please enter a valid integer. " + (maxAttempts-attempts) + "/" + maxAttempts + " tries left");
                    }
                }

                if (number != userGuess) {
                    System.out.println("You lose after " + maxAttempts + " tries, the expected number was " + number);
                }

                outputWriter.writeRetryMessage();
                String userResponse = inputReader.readInput();
                continuePlaying = userResponse.equals("yes");
                if (continuePlaying) {
                    userGuess = 0;
                    attempts = 0;
                }
            }
        }
    }

    public static class NumberGenerator {
        public int generate(int min, int max) {
            Random generator = new Random();
            return generator.nextInt(max - min + 1) + min;
        }
    }

    public static class InputReader {
        Scanner scanner = new Scanner(System.in);
        public String readInput() {
            return scanner.nextLine().trim().toLowerCase();
        }

        public void close() {
            scanner.close();
        }
    }

    public static class OutputWriter {

        //TODO Pour gagner du temps j'ai pas fait tout les messages

        public void writeOutput(String output) {
            System.out.println(output);
        }

        public void writeTitle() {
            System.out.println("-===========================-");
            System.out.println("=== GUESS THE NUMBER GAME ===");
            System.out.println("-===========================-");
        }

        public void writeRules() {
            System.out.println("Guess the number (between 1 and 100)!");
        }

        public void writeNotFoundMessage(String divergence, int maxAttempts, int attempts) {
            System.out.println("Wrong! Your number is " + divergence + " than the correct one. " + (maxAttempts-attempts) + "/" + maxAttempts + " tries left");

        }

        public void writeRetryMessage() {
            System.out.println("----------------------------------------------------");
            System.out.println("Do you want to try again with a new number (yes/no)?");
        }

        public void writeDebugNumber(int number) {
            System.out.println("debug : the expected number is " + number);
        }

        public void writeWinMessage(int attempts) {
            System.out.println("You found it after " + attempts + " tries!");
        }
    }

}