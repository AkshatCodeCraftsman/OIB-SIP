import java.util.Random;
import java.util.Scanner;

public class GuessTheNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int lowerBound = 1;
        int upperBound = 100;
        int randomNumber = random.nextInt(upperBound - lowerBound + 1) + lowerBound;

        int maxAttempts = 5;
        int attempts = 0;
        int score = 100;

        System.out.println("Welcome to Guess The Number!");
        System.out.println("I've selected a random number between " + lowerBound + " and " + upperBound + ". Can you guess it?");

        while (attempts < maxAttempts) {
            System.out.print("Enter your guess: ");
            int guess = scanner.nextInt();
            attempts++;

            if (guess == randomNumber) {
                System.out.println("Congratulations! You've guessed the number in " + attempts + " attempts.");
                System.out.println("Your score is: " + score);
                break;
            } else if (guess < randomNumber) {
                System.out.println("Too low! Try again.");
            } else {
                System.out.println("Too high! Try again.");
            }

            score -= 20; // Decrease score for each attempt
        }

        if (attempts == maxAttempts) {
            System.out.println("Sorry, you've run out of attempts.");
            System.out.println("The correct number was: " + randomNumber);
        }

        scanner.close();
    }
}
