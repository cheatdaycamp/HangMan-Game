package hangman;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.util.Arrays;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hangman {

    //principal method
    public static void HangmanCode() {
        //declaring variables:

        int guesses = 10;
        String[] words = filereader();
        Random word = new Random();
        String choosenword = (words[word.nextInt(words.length)].toUpperCase());
        char[] wordToGuess = choosenword.toCharArray();
        char[] shownword = (choosenword.replaceAll(".", "*").toCharArray());
        boolean inputstory = false;
        Set<String> userguesses = new HashSet<>();

        //System.out.println("Word is: " + choosenword);
        /// uncomment upper line to reveal the word during the game.

        /* do-while: as long as guessed word = false, or user has more trys,
        keeps on asking for input (letters, words)*/
        do {
            PhraseGuesses(guesses);
            String userinput = GameStart(shownword);
            // scenario 1: invalid input ---> Re prompts.
            while (!isAlpha(userinput) | userinput.equals("")) {
                System.out.println("Invalid Input."
                        + "Please insert a word or a letter");
                break;
            }

            // scenario 2: valid input, user input is letter ---> letter check.
            while (isAlpha(userinput) & userinput.length() == 1) {

                char guessChar = (userinput.toUpperCase()).charAt(0);
                inputstory = checkrepeated(userguesses, userinput);
                // checks if value is stored in the hashset. Returns true if so.
                userguesses.add(userinput); // saving the input in hashshet

                //checking for unrepeated input and found character
                if (checkchar(wordToGuess, guessChar) & !inputstory) {
                    for (int i = 0; i < wordToGuess.length; i++) {
                        if (wordToGuess[i] == guessChar) {
                            shownword[i] = guessChar;
                        }
                    }
                    System.out.println("Good job! the letter '" + userinput
                            + "' is in the word");
                    //unrepeated input, character not in word
                } else if (!checkchar(wordToGuess, guessChar) & !inputstory) {
                    System.out.println("Sorry Habibi the letter '"
                            + userinput + "' is not in the word");
                    guesses--;
                } else { //last option: that inputstory == true
                    System.out.println("Neshama Sheli, you have already tried"
                            + " that...");
                }
                break;
            }
            /* scenario 3: valid input, user input is a word
            ---> checks if input was stored.
            then if the length matches.
            if length of input word doesn't match the one to guess,
            it warns the user without penalizing the guesses.
            if the length of the input matches the one of the word to guess,
            proceeds to check if their content matches. */ //

            while (isAlpha(userinput) & userinput.length() >= 2) {
                int diflength = userinput.length() - choosenword.length();
                inputstory = checkrepeated(userguesses, userinput);
                // checks if value is stored in the hashset. Returns true if so.
                userguesses.add(userinput);

                if (userinput.equalsIgnoreCase(choosenword)) {
                    System.out.println("Congratulations! You Guessed!!"
                            + " Effectively, it was '" + choosenword
                            + "'\nThanks for playing!\nBye!");
                    System.exit(0);
                } else if (diflength == 0 & !inputstory) {
                    System.out.println("Sorry! That's not the word Habibi");
                    guesses--;
                } else if (diflength == 0 & inputstory) {
                    System.out.println("Neshama Sheli, you have already tried"
                            + " that...");
                } else {
                    System.out.println("That's not the same length of the word!"
                            + " Don't worry, you won't loose a guess for that");
                }
                break;
            }

        } while (guesses >= 1 & String.valueOf(shownword).compareToIgnoreCase
        (choosenword) != 0);
        if (Arrays.equals(shownword, wordToGuess)) {
            System.out.println("Congratulations! You Guessed!! Effectively,"
                    + " it was '" + choosenword
                    + "'\nThanks for playing!\nBye!");
        } else {
            System.out.println("You loose!\nToo bad, you were so close!"
                    + "\nBy the way, the word was '" + choosenword + "'");
        }
    }

    //presentation methods
    public static void WelcomeScreen() {
        //Welcome Screen

        System.out.println(" _.:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:._."
                + ":*~*:._.:*~*:._ \n");
        System.out.println("dP     dP                                       "
                + "                   ");
        System.out.println("88     88                                        "
                + "                  ");
        System.out.println("88aaaaa88a .d8888b. 88d888b. .d8888b. 88d8b.d8b. "
                + ".d8888b. 88d888b. ");
        System.out.println("88     88  88'  `88 88'  `88 88'  `88 88'`88'`88 "
                + "88'  `88 88'  `88 ");
        System.out.println("88     88  88.  .88 88    88 88.  .88 88  88  88 "
                + "88.  .88 88    88 ");
        System.out.println("dP     dP  `88888P8 dP    dP `8888P88 dP  dP  dP "
                + "`88888P8 dP    dP ");
        System.out.println("                                   88            "
                + "                  ");
        System.out.println("                              d8888P             "
                + "                  ");
        System.out.println("\n _.:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*"
                + ":._.:*~*:._.:*~*:._ ");

    }

    public static void GameRules() {

        try {
            // intro and game explanation
            String intro = "\nHi! We are going to play the classic Hangman Game\n";
            String rules = "In this game you need to guess the hidden word."
                    + "\nYou have 10 trys."
                    + "\nYou can also guess the word."
                    + " \nGood luck!\n\n";
            printWithDelays(intro, TimeUnit.MILLISECONDS, 30);
            printWithDelays(rules, TimeUnit.MILLISECONDS, 30);
        } catch (InterruptedException ex) {
            Logger.getLogger(Hangman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void printWithDelays(String message, TimeUnit unit,
            long delay) throws InterruptedException {
        // Typewriter console effect
        for (char ch : message.toCharArray()) {
            System.out.print(ch);
            unit.sleep(delay);
        }
    }

    //smaller methods that take part in solving the exercise
    public static String[] filereader() {
        String[] wordslist = null;
        try {
            File txt = new File("wordslist.txt");
            Scanner sc = new Scanner(txt);
            ArrayList<String> data = new ArrayList<String>();
            while (sc.hasNextLine()) {
                data.add(sc.nextLine());
            }
            //System.out.println(data); // uncomment to see list data
            wordslist = data.toArray(new String[]{});
            return wordslist;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Hangman.class.getName()).log(Level.SEVERE, null, ex);
        }
        return wordslist;
    }

    public static void PhraseGuesses(int a) {
        //checks how many guesses are left and prints it to user
        switch (a) {
            case 1:
                System.out.println("You have " + a + " last guess");
                break;
            default:
                System.out.println("You have " + a + " guesses");
                break;
        }
    }

    public static String GameStart(char[] a) {

        //prints information to user and generates scanner for input
        System.out.println("Word is:\n" + String.valueOf(a));
        System.out.println("What is your guess?");
        Scanner sc = new Scanner(System.in);
        String b = sc.nextLine();
        return b;
    }

    public static boolean isAlpha(String userinput) {
        //checks if char is letter
        char[] chars = userinput.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkchar(char[] myarray, char myinputchar) {
        //checks if user character is in the array of characters 
        for (int i = myarray.length - 1; i >= 0; i--) {
            if (myarray[i] == myinputchar) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkrepeated(Set<String> a, String b) {
        //checks if the input is repeated, regardless if it is a single char
        // or a string
        boolean contains = false;
        if (a.contains(b.toLowerCase()) | a.contains(b.toUpperCase())) {
            contains = true;
        }
        return contains;
    }

    //main method
    public static void main(String[] args) {
        WelcomeScreen();
        GameRules();
        HangmanCode();
    }
}
