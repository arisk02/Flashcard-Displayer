import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Aris Karamustafic
 */

public class FlashcardDisplayer {

  private static FlashcardPriorityQueue flashcards;
  private static DateTimeFormatter formatter;
    /**
   * Creates a flashcard displayer with the flashcards in file.
   * File has one flashcard per line. On each line, the date the flashcard
   * should next be shown is first (format: YYYY-MM-DDTHH-MM), followed by a tab,
   * followed by the text for the front of the flashcard, followed by another tab.
   * followed by the text for the back of the flashcard. You can assume that the
   * front/back text does not itself contain tabs. (I.e., a properly formatted file
   * has exactly 2 tabs per line.)
   * The time may be more precise (e.g., seconds may be included). The parse method
   * in LocalDateTime can deal with this situation without any changes to your code.
   */
  public FlashcardDisplayer(String filePath) {
    String theFilePath = filePath;
    flashcards = new FlashcardPriorityQueue();         // creates a new instance of FlashcardPriorityQueue
    formatter = DateTimeFormatter.ISO_DATE_TIME;       // formatter that formats LocalDateTime variables
    Scanner fileData = null;
    try {
      fileData = new Scanner(new File(filePath));                               // this line tries to find a file under the filePath path
    } catch (FileNotFoundException e) {
      System.out.println("Scanner error opening the file " + filePath);
      System.out.println(e.getMessage()); // optional                           // if file is not found or can not be read, a helpful error
      System.exit(1); // optional                                               // message helpful error message is shown and the program ends
    }
    while (fileData.hasNextLine()) {
      String line = fileData.nextLine();                                        // if the file is found, every one of its lines represent one flashcard
      String[] arguments = new String[3];                                       // for which the arhuments are divided by a tab, if it is properly .
      arguments = line.split("\t");
      String dueDate = arguments[0];
      String frontText = arguments[1];
      String backText = arguments[2];
      Flashcard flashcard = new Flashcard(dueDate, frontText, backText);        // makes a new Flashcard object with those arguments,
      flashcards.add(flashcard);                                                // and adds it to the heap.
    }
    fileData.close();
  }
  /**
   * Writes out all flashcards to a file so that they can be loaded
   * by the FlashcardDisplayer(String filePath) constructor. Returns true
   * if the file could be written. The FlashcardDisplayer should still
   * have all of the same flashcards after this method is called as it
   * did before the method was called. However, flashcards with the same
   * exact same next display date may later be displayed in a different order.
   */
  public boolean saveFlashcards(String outFilePath) {
    FlashcardPriorityQueue copyOfFlashcards = new FlashcardPriorityQueue();     // makes a copy of the heap array.
    String content = "";
    FileWriter toFile = null;
    File fileToWrite = new File(outFilePath);                    // creates a new file under the given name.
    boolean canWrite = true;                                     // boolean variable that returns true if the file could
    try {                                                        // be written, or false otherwise.
      toFile = new FileWriter(fileToWrite);
      while (!flashcards.isEmpty()) {                      // takes out flaschards one by one and writes them into the file
        Flashcard flashcard = flashcards.poll();
        copyOfFlashcards.add(flashcard);
        String s = flashcard.getDueDate() + "\t" + flashcard.getFrontText() + "\t" + flashcard.getBackText();
        if (!content.equals("")) {
          content = content + "\n" + s;      // if it is not the first flashcard in the file, add it to the new line
        } else {
          content = content + s;
        }
      }
      toFile.write(content);
      toFile.close();
    } catch (IOException e) {
      canWrite = false;                             // canWrite is false if the file could not be written.
      System.out.println(e.getMessage());           // if the file could not be written, a helpful message is displayed.
    }
    while (!copyOfFlashcards.isEmpty()) {
      Flashcard flashcardCopy = copyOfFlashcards.poll();
      flashcards.add(flashcardCopy);
    }
    return canWrite;
  }

  /**
   * Displays any flashcards that are currently due to the user, and
   * asks them to report whether they got each card correct. If the
   * card was correct (if the user entered "1"), it is added back to the
   * deck of cards with a new due date that is one day later than the current
   * date and time; if the card was incorrect (if the user entered "2"), it is
   * added back to the card with a new due date that is one minute later than
   * that the current date and time.
   */
  public void displayFlashcards() {
    LocalDateTime timeNow = LocalDateTime.now();        // this is the time at the moment given as a LocalDateTime variable.
      while (!flashcards.isEmpty() && timeNow.isAfter(flashcards.peek().getDueDate())) {  // if the command word is "quiz", the program
        System.out.println("\nCard:");                                                        // displays all the flashcards that are currently
        Flashcard flashcard = flashcards.poll();                                              // due, starting from the earliest onwards.
        System.out.println(flashcard.getFrontText());
        Scanner keyboard1 = new Scanner(System.in);
        System.out.print("[Press return for back of the card]");
        String returnKey = keyboard1.nextLine();
        while (!returnKey.equals("")) {
          System.out.print("[Press return for back of the card]");                            // the program only displays the back text after the
          returnKey = keyboard1.nextLine();                                                   // user allows it to, that is after the user thinks
        }                                                                                     // of the answer.
        System.out.println(flashcard.getBackText());
        Scanner keyboard2 = new Scanner(System.in);
        System.out.println("Press 1 if you got the card correct and 2 if you got the card incorrect.");
        int correctOrNot = keyboard2.nextInt();
        while (correctOrNot != 1 && correctOrNot != 2) {
          System.out.println("Press 1 if you got the card correct and 2 if you got the card incorrect.");       // if the user got the card correct, the user inputs 1,
          correctOrNot = keyboard2.nextInt();                                                                   // otherwise they input 2. If they got the card correct,
        }                                                                                                       // the card is added back into the heap with a dueDate
        if (correctOrNot == 1) {                                                                                // that is 24 hours from current time, and if they got it
          LocalDateTime newDueDateCorrect = timeNow.plusDays(1);                                                // wrong, it is added with a dueDate 1 minute from current time.
          String newDueDateCorrectString = newDueDateCorrect.format(formatter);
          Flashcard correctFlashcard = new Flashcard(newDueDateCorrectString, flashcard.getFrontText(), flashcard.getBackText());
          flashcards.add(correctFlashcard);
        } else if (correctOrNot == 2) {
          LocalDateTime newDueDateWrong = timeNow.plusMinutes(1);
          String newDueDateWrongString = newDueDateWrong.format(formatter);
          Flashcard wrongFlashcard = new Flashcard(newDueDateWrongString, flashcard.getFrontText(), flashcard.getBackText());
          flashcards.add(wrongFlashcard);
        }
      }
      String commandWord2 = enterCommandWord();             // after finishing the quiz, the user chooses what the program is going to do next
      if (commandWord2.equals("quiz")) {                    // if the user types "quiz", the quiz starts over with any flashcards that are due at that time.
        displayFlashcards();
      } else if (commandWord2.equals("save")) {             // if the user types "save", the flashcards are saved to a file with name of their choice
        saveCommand();
        enterCommandWord();
      } else if (commandWord2.equals("exit")) {             // if the user types "exit", the program ends
        exitCommand();
      }
  }

  private void saveCommand() {
    Scanner keyboard = new Scanner(System.in);
    System.out.println("Type a filename where you'd like to save the flashcards:");
    String fileName = keyboard.nextLine();                                               // this allows user to input the name of the file to save flashcards
    while (!saveFlashcards(fileName)) {                                                  // in, and if the file could not be made under that name, it asks
      System.out.println("Please input a different file name:");                         // the user for a different file name.
      fileName = keyboard.nextLine();
    }
    String commandWord = enterCommandWord();
    if (commandWord.equals("quiz")) {
      displayFlashcards();
    } else if (commandWord.equals("save")) {
      saveCommand();
    } else if (commandWord.equals("exit")) {
      exitCommand();
    }
  }

   private void exitCommand() {                // this prints "Goodbye" and ends the program.
     System.out.println("Goodbye!");
     System.exit(1);
   }

   private String enterCommandWord() {                     // this asks the user to input a command word, and if it is not equal to one of the three
     Scanner keyboard = new Scanner(System.in);            // available command words ("quiz", "save", "exit"), it asks the user to input a command word again.
     System.out.println("Enter a command:");
     String commandWord = keyboard.nextLine();
     while (!commandWord.equals("quiz") && !commandWord.equals("save") && !commandWord.equals("exit")) {
       System.err.println("That command word does not exist. Please try either 'quiz', 'save' or 'exit'");
       commandWord = keyboard.nextLine();
     }
     return commandWord;
   }

  public static void main(String[] args) {
    if (args.length != 1) {                                                                                   // since this program only takes one command line
      System.err.println("The number of arguments in the command line must be equal to 1 (file path).");      // argument, any other case will throw this error message.
    } else {
      FlashcardDisplayer flashcardDisplayer = new FlashcardDisplayer(args[0]);
      System.out.println("Time to practice flashcards! The computer will display your flashcards,");          // this greets the user with a welcome message before
      System.out.println("you generate the response in your head, and then see if you got it right.");        // asking them what they want the program to do next.
      System.out.println("The computer will show you cards that you miss more often than those you know!");
      String commandWord = flashcardDisplayer.enterCommandWord();           // enter a command based on what a user wants the program to do next.
      if (commandWord.equals("quiz")) {
        flashcardDisplayer.displayFlashcards();
      } else if (commandWord.equals("save")) {                // these do the exact same thing as the two above, but only if the user never chose to do
        flashcardDisplayer.saveCommand();                                        // the quiz in the first place
      } else if (commandWord.equals("exit")) {
        flashcardDisplayer.exitCommand();
      }
    }
  }

}
