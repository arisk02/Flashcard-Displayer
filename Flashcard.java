import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Aris Karamustafic
 */

public class Flashcard implements Comparable<Flashcard> {

    private String frontText;
    private String backText;
    private LocalDateTime dueDate;

      /**
     * Creates a new flashcard with the given dueDate, text for the front
     * of the card (front), and text for the back of the card (back).
     * dueDate must be in the format YYYY-MM-DDTHH:MM. For example,
     * 2019-11-04T13:03 represents 1:03PM on November 4, 2019. It's
     * okay if this method crashes if the date format is incorrect.
     * In the format above, the time may be more precise (e.g., seconds
     * or milliseconds may be included). The parse method in LocalDateTime
     * can deal with these situations without any changes to your code.
     */

    public Flashcard(String dueDate, String front, String back) {
      frontText = front;
      backText = back;
      this.dueDate = LocalDateTime.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);    // turns the dueDate from a String object to a LocalDateTime object
    }

    /**
     * Gets the text for the front of this flashcard.
     */
    public String getFrontText() {
      return frontText;
    }

    /**
     * Gets the text for the Back of this flashcard.
     */
    public String getBackText() {
      return backText;
    }

    /**
     * Gets the time when this flashcard is next due.
     */
    public LocalDateTime getDueDate() {
      return dueDate;
    }

    /**
     * Returns a string displayinf flashcard's due date, front and back text.
     */
    @Override
    public String toString() {
      String string = "DueDate: " + dueDate + " front: " + frontText + " back: " + backText;
      return string;
    }

    /**
     * Compares due dates of two flashcards.
     */
    @Override
    public int compareTo(Flashcard otherFlashcard) {
      return (dueDate.compareTo(otherFlashcard.dueDate));
    }


}
