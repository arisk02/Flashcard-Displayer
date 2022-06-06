import java.lang.Math;
import java.util.NoSuchElementException;

/**
 * @author Aris Karamustafic
 */

 public class FlashcardPriorityQueue implements PriorityQueue<Flashcard> {

    private Flashcard[] heap;                       // creates an empty heap of Flashcard objects
    private int lastIndex;                          // index of the last enrty
    private static final int DEFAULT_CAPACITY = 1;

    public FlashcardPriorityQueue() {
      Flashcard[] flashcardHeap = new Flashcard[DEFAULT_CAPACITY + 1]; // sets the size of the array to Default capacity + 1
      heap = flashcardHeap;
      heap[0] = null;                                                  // sets the first node to null because child of 0 can not be calculated with a formula (index+1)/3.
      lastIndex = 0;
    }

    /** Adds the given item to the queue. */
    public void add(Flashcard flashcard) {
        ensureCapacity();
        lastIndex++;                                  // after adding one item to the array, its last index is increased by 1.
        int addedFlashcardIndex = lastIndex;          // Flashcard is added to the end of the array, so its index is the last index.
        heap[addedFlashcardIndex] = flashcard;
        reheapForAdd(addedFlashcardIndex);            // It rearranges the heap so no child has an earlier due than its parent.
    }

    /** Recursive method that, after adding an item to the queue checks if it has an earlier
     * due date than its parent, and if it does, it switches their places.
     */
    private void reheapForAdd(int index) {
      int parentIndex = (index + 1) / 3;                                              // The parent index of a node is calculated by the formula (index+1)/3.
      if (parentIndex > 0 && heap[index].compareTo(heap[parentIndex]) < 0) {         // This statement checks if the node has a parent and if the node has an earlier dueDate than the parent.
        Flashcard parent = heap[parentIndex];                                         // Stores the value of the parent.
        heap[parentIndex] = heap[index];
        heap[index] = parent;                                                         // If the node has an earlier dueDate, switch the places of the parent and the node.
        reheapForAdd(parentIndex);                                                    // check if the parent has a parent and if it has an earlier dueDate thatn the node.
      }
    }

    /** Removes the first item according to compareTo from the queue, and returns it.
     * Throws a NoSuchElementException if the queue is empty.
     */
     public Flashcard poll() {
      if (isEmpty()) {
        throw new NoSuchElementException();
      } else {
        Flashcard flashcard = heap[1];                  // stores the value of the root.
        heap[1] = heap[lastIndex];                      // Puts the last flashcard into the root, and removes the leaf.
        heap[lastIndex] = null;
        lastIndex--;
        reheapForPoll(1);                               // It rearranges the heap so no child has an earlier due than its parent.
        return flashcard;
      }
    }

    /** Recursive method that, after the removal of the first item of the queue, puts the last flashcard
     * as a root and resorts the trre so the earliest child is first again.
     */
    private void reheapForPoll(int rootIndex) {
      int leftChildIndex = rootIndex * 3 - 1;               // Index of the left child in a ternary tree is index*3-1.
      int middleChildIndex = rootIndex * 3;
      int rightChildIndex = rootIndex * 3 + 1;
      int earliestChildIndex = leftChildIndex;
      Flashcard root = heap[rootIndex];                     // set left child of the root as the earliestChild variable
      if(leftChildIndex <= lastIndex) {                     // check if the root has any children
        if (rightChildIndex <= lastIndex && heap[rightChildIndex].compareTo(heap[earliestChildIndex]) < 0) {    // compares the left and right child, if right child exists
          earliestChildIndex = rightChildIndex;                                                                 // if right child is earlier, it becomes earliestChild variable
        }
        if (middleChildIndex <= lastIndex && heap[middleChildIndex].compareTo(heap[earliestChildIndex]) < 0) {  // compares the earliestChild variable and middle child, if middle child exists
          earliestChildIndex = middleChildIndex;                                                                // if middle child is earlier, it becomes earliestChild variable
        }
        if (heap[earliestChildIndex].compareTo(root) < 0) {      // after finding which child is the earliest one, compare it to the root
          heap[rootIndex] = heap[earliestChildIndex];            // if the earliestChild is earlier than the root, switch their places
          heap[earliestChildIndex] = root;
          reheapForPoll(earliestChildIndex);                     // do this until the root has no children, i.e. left child does not exist
        }
      }
    }

    /** Returns the first item according to compareTo in the queue, without removing it.
     * Throws a NoSuchElementException if the queue is empty.
     */
    public Flashcard peek() {
      if (isEmpty()) {
        throw new NoSuchElementException();
      } else {
        Flashcard flashcard = heap[1];          // since the first item in the array is unused for the sake of calculations,
        return flashcard;                       // check the second place in the array, that is the first flashcard, and return it.
      }
    }

    /** Returns true if the queue is empty. */
    public boolean isEmpty() {
      return lastIndex == 0;                      // since the first element is unused, if the lastIndex is 0 the array has no flashcards in it.
    }

    /** Removes all items from the queue. */
    public void clear() {
      for (int i = 1; i <= lastIndex; i++) {
        heap[i] = null;                           // equal all the flashcards in the heap to null
      }
      lastIndex = 0;
    }

    /** If the array is full, it doubles the size of the array.*/
    private void ensureCapacity() {
      if (lastIndex + 1 == heap.length) {                    // if the lastIndex is also the lastIndex of flashcards of the array itself
        Flashcard[] oldHeap = heap;                          // then make a new array of double the size and copy all of the elements
        int oldSize = oldHeap.length;                        // into that array, and assign the old array name to it
        int newSize = 2 * oldSize;
        Flashcard[] tempHeap = new Flashcard[newSize];
        for (int i = 1; i <= lastIndex; i++) {
          tempHeap[i] = oldHeap[i];
        }
        heap = tempHeap;
      }
    }

    public static void main(String[] args) {

      FlashcardPriorityQueue flashcards = new FlashcardPriorityQueue();
      Flashcard f1 = new Flashcard("2021-11-29T12:11", "Belgrade", "Serbia");
      Flashcard f2 = new Flashcard("2021-11-29T12:10", "Sarajevo", "Bosnia and Herzegovina");
      Flashcard f3 = new Flashcard("2021-11-29T12:08", "Zagreb", "Croatia");
      Flashcard f4 = new Flashcard("2021-11-29T12:08", "Podgorica", "Montenegro");
      Flashcard f5 = new Flashcard("2021-11-29T12:12", "Ljubljana", "Slovenia");
      Flashcard f6 = new Flashcard("2021-11-29T12:07", "Tirana", "Albania");
      Flashcard f7 = new Flashcard("2021-11-29T12:09", "Pristine", "Kosovo");

      // test for add() and peek()
      flashcards.add(f1);
      System.err.println("This should print out Belgrade: " + flashcards.peek().getFrontText());
      flashcards.add(f2);
      System.err.println("This should print out Sarajevo: " + flashcards.peek().getFrontText());
      flashcards.add(f3);
      System.err.println("This should print out Zagreb: " + flashcards.peek().getFrontText());
      flashcards.add(f4);
      System.err.println("This should print out Zagreb: " + flashcards.peek().getFrontText());
      flashcards.add(f5);
      System.err.println("This should print out Zagreb: " + flashcards.peek().getFrontText());
      flashcards.add(f6);
      System.err.println("This should print out Tirana: " + flashcards.peek().getFrontText());
      flashcards.add(f7);
      System.err.println("This should print out Tirana: " + flashcards.peek().getFrontText());

      // test for poll()
      flashcards.poll();
      System.err.println("This should print out Zagreb: " + flashcards.peek().getFrontText());
      flashcards.poll();
      System.err.println("This should print out Podgorica: " + flashcards.peek().getFrontText());
      flashcards.poll();
      System.err.println("This should print out Pristine: " + flashcards.peek().getFrontText());
      flashcards.poll();
      System.err.println("This should print out Sarajevo: " + flashcards.peek().getFrontText());
      flashcards.poll();
      System.err.println("This should print out Belgrade: " + flashcards.peek().getFrontText());
      flashcards.poll();
      System.err.println("This should print out Ljubljana: " + flashcards.peek().getFrontText());


      // test for clear() and isEmpty()
      flashcards.add(f1);
      flashcards.add(f2);
      flashcards.add(f3);
      flashcards.add(f4);
      flashcards.add(f5);
      if (flashcards.isEmpty()) {
        System.err.println("The heap is not empty, but isEmpty() returns true.");
      }
      flashcards.clear();
      if (!flashcards.isEmpty()) {
        System.err.println("The heap is supposed to be empty after calling clear(), but isEmpty() returns false.");
      }
      flashcards.add(f1);
      if (flashcards.isEmpty()) {
        System.err.println("The heap is not empty, but isEmpty() returns true.");
      }
    }
 }
