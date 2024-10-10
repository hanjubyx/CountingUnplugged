import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class WordCount {
  String word;
  int count;

  public WordCount(String word, int count) {
    this.word = word;
    this.count = count;
  }

  public String getWord() {
    return word;
  }

  public void addCount() {
    count++;
  }

  @Override
  public String toString() {
    return word + ": " + count;
  }
}

public class Main {
  static ArrayList<WordCount> wordList = new ArrayList<>();
  private static Set<String> commonWords = new HashSet<>();

  static {
    String[] words = {
            "the", "of", "to", "and", "a", "in", "is", "it", "you", "that",
            "he", "was", "for", "on", "are", "with", "as", "I", "his", "they",
            "be", "at", "one", "have", "this", "from", "or", "had", "by", "not",
            "but", "what", "we", "can", "out", "other", "were", "all", "there",
            "when", "up", "your", "how", "said", "an", "each", "she", "which",
            "do", "their", "if", "will", "way", "about", "many", "then", "them",
            "would", "like", "so", "these", "her", "thing", "him", "has", "more",
            "could", "go", "come", "did", "no", "most", "my", "know", "than",
            "who", "may", "been", "now"
    };
    Collections.addAll(commonWords, words);
  }

  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Choose a file to analyze (press 1 for textOne or 2 for textTwo): ");
    String choice = scanner.nextLine();

    String filePath;
    if (choice.equals("1")) {
      filePath = "src/textOne.txt";  // Updated path to the src directory
    } else if (choice.equals("2")) {
      filePath = "src/textTwo.txt";  // Updated path to the src directory
    } else {
      System.out.println("Invalid choice. Exiting.");
      return;
    }

    System.out.println("Find the top 5 most common words in the file (press 1), or find the most common word (press 2): ");
    int option = scanner.nextInt();

    try {
      if (option == 1) {
        System.out.println(findTop5Occurrences(filePath));
      } else if (option == 2) {
        System.out.println("The most common word is " + findMostCommonWord(filePath) + ".");
      } else {
        System.out.println("Invalid option. Exiting.");
      }
    } catch (FileNotFoundException e) {
      System.out.println("Error: The file at " + filePath + " was not found.");
    } catch (IOException e) {
      System.out.println("Error reading the file: " + e.getMessage());
    }
  }

  private static String findTop5Occurrences(String filePath) throws IOException {
    ArrayList<String> words = cleanString(filePath);
    countWordOccurrences(words);
    Collections.sort(wordList, Comparator.comparingInt(wc -> -wc.count));

    if (wordList.size() < 5) {
      return "Not enough words in the file.";
    }

    StringBuilder result = new StringBuilder("The top five most common words in the text file " + filePath + " are:\n");
    for (int i = 0; i < 5; i++) {
      result.append(i + 1).append(". ").append(wordList.get(i)).append("\n");
    }
    return result.toString();
  }

  private static String findMostCommonWord(String filePath) throws IOException {
    ArrayList<String> words = cleanString(filePath);
    countWordOccurrences(words);
    Collections.sort(wordList, Comparator.comparingInt(wc -> -wc.count));
    return wordList.isEmpty() ? "No words found." : wordList.get(0).toString();
  }

  private static void countWordOccurrences(ArrayList<String> words) {
    for (String word : words) {
      if (!isInList(word)) {
        wordList.add(new WordCount(word, 1));
      } else {
        for (WordCount wordCount : wordList) {
          if (wordCount.getWord().equals(word)) {
            wordCount.addCount();
          }
        }
      }
    }
  }

  public static boolean isInList(String word) {
    return wordList.stream().anyMatch(wc -> wc.getWord().equals(word));
  }

  public static ArrayList<String> cleanString(String input) throws IOException {
    String output = readFile(input);
    ArrayList<String> out = new ArrayList<>();
    StringBuilder curWord = new StringBuilder();

    for (char c : output.toCharArray()) {
      if (Character.isLetterOrDigit(c)) {
        curWord.append(c);
      } else {
        if (curWord.length() > 0) {
          String word = curWord.toString().toLowerCase();
          if (!commonWords.contains(word) && word.length() >= 2) {
            out.add(word);
          }
          curWord.setLength(0);
        }
      }
    }
    if (curWord.length() > 0) {
      String word = curWord.toString().toLowerCase();
      if (!commonWords.contains(word) && word.length() >= 2) {
        out.add(word);
      }
    }
    return out;
  }

  private static String readFile(String file) throws IOException {
    File inputFile = new File(file);
    if (!inputFile.exists()) {
      throw new FileNotFoundException("File not found: " + file);
    }

    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    StringBuilder stringBuilder = new StringBuilder();
    String line;

    while ((line = reader.readLine()) != null) {
      stringBuilder.append(line).append(System.lineSeparator());
    }
    reader.close();
    return stringBuilder.toString();
  }
}