# Flashcard-Displayer

Flashcard Displayer is a program that takes in a text document that contains flashcards and their show times
and displays them on the screen. If the answer user thought of is correct, the flashcard will have its show time
moved by 24 hours, but if its incorrect it will be moved by only 1 minute.

Show time means that the program will show this flashcard if the current time is after the cards "show time", also
noted in the code as dueDate.

This allows user to see the cards they got wrong more often than those they get right.

To compile the program, compile all java files in respitory: javac *.java

To run, give the argument that is the text document containing the flashcards and times: java FlashcardDisplayer .txt

After running your quiz, you can then save the flashcards and their new show times in a new text document.

The way Flashcards were sorted was by sing a ternary tree, where the flashcard with the earliest show time
was always the root of the tree. After changing the show time of the flashcard, the tree is then sorted again.

In the respitory you can find SampleFlashcards which is the format used for the flashcards and their show times.
