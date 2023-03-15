import java.util.Scanner;
import java.util.Random;


class Grade {
    int bull;
    int cow;
    public Grade (int bull, int cow) {
        this.bull = bull;
        this.cow = cow;
    }
}

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}

class Game {
    final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";

    private int readNumber() {
        Scanner scanner = new Scanner(System.in);
        int num;
        
        if (scanner.hasNextInt()) {
            num = scanner.nextInt();
        } else {
            throw new RuntimeException("Error: you should enter the number"); 
        }
        
        return num;
    }

    private  int getSecretLength() {
        System.out.println("Input the length of the secret code: ");
        int secretLength = readNumber();
      
        if (secretLength > 36 ) {
            System.out.println("Error: the length of the secret numner can not be more than 36");
            System.exit(1);
        } else if (secretLength < 0) {
            System.out.println("Error: the length of the secret numner can not be less than 1");
            System.exit(1);
        }

        return secretLength;
    }

    private int getSymbolsRangeLength(int secretLength) {
        System.out.println("Input the number of possible symbols in the code: ");
        int symbolsRangeLength = readNumber();

        if (symbolsRangeLength > 36 ) {
            System.out.println("Error: maximum number of symbol's range is 36 (0-9, a-z). ");
            System.exit(1);
        } else if (symbolsRangeLength < 0) {
            System.out.println("Error: minimum number of symbol's range is 1");
            System.exit(1);
        } else if (secretLength < symbolsRangeLength) {
            System.out.println("Range of number can not be more than the secret code's length");
            System.exit(1);
        }

        return symbolsRangeLength;
    }


    private StringBuilder generateSecret(int secretLength, int symbolsRangeLength) {
        StringBuilder secretCode = new StringBuilder(secretLength);
        Random random = new Random();

        while (secretCode.length() < secretLength) {
            char symbol = CHARS.charAt(random.nextInt(symbolsRangeLength));

            if (secretCode.toString().indexOf(symbol) == -1) {
                secretCode.append(symbol);
            }
        }

        printSecretCode(secretCode, secretLength, symbolsRangeLength);
        return secretCode;
    }

    private void printSecretCode(StringBuilder secretCode, int secretLength, int symbolsRangeLength) {
        System.out.print("The secret is prepared: ");
        for (int i = 0; i < secretLength; i++) {
            System.out.print("*");
        }

        if (symbolsRangeLength <= 10) {
            System.out.println(" (0-" + ( symbolsRangeLength - 1) + ").");
        } else if (symbolsRangeLength == 11) {
            System.out.println(" (0-9, a)." );
        } else {
            System.out.println(" 0-9" + ", " + CHARS.charAt(10) + CHARS.charAt(symbolsRangeLength- 1) );
        }
    }

    private Grade gradeNum(String number, String secretCode) {
        int bull = 0;
        int cow = 0;

        for (int i = 0; i < number.length(); i++) {
            char n = number.charAt(i);

            if (secretCode.charAt(i) == n) {
                bull += 1;
            } else if (secretCode.indexOf(n) >= 0) {
                cow += 1;
            }
        }

        return new Grade(bull, cow);
    }

    private void printGrade(Grade grade) {
        String gradeString;

        if (grade.bull > 0 && grade.cow > 0) {
            gradeString = String.format("%d bull(s) and %d cow(s)", grade.bull, grade.cow);
        } else if (grade.cow > 0) {
            gradeString = String.format("%d cow(s)", grade.cow);
        } else if (grade.bull > 0 ) {
            gradeString = String.format("%d bull(s)", grade.bull);
        } else {
            gradeString = "None";
        }

        System.out.println(gradeString);
    }

    private boolean gameOver(Grade grade,int secretLength){
        return grade.bull == secretLength && grade.cow == 0;
    }

    void play() {
        int secretLength = getSecretLength();
        int symbolsRangeLength = getSymbolsRangeLength(secretLength);
        String secretCode = generateSecret(secretLength, symbolsRangeLength).toString();

        System.out.println("Okay, let's start a game!");
        int turn = 1;

        Scanner scanner = new Scanner(System.in);
        Grade grade;

        do {
            System.out.printf("Turn %d%n", turn);
            String number = scanner.next();
            grade = gradeNum(number, secretCode);
            printGrade(grade);
        } while (!gameOver(grade, secretLength));

        System.out.println("Congratulations! You guessed the secret code.");
    }
}