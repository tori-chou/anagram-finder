import java.io.*;
import java.util.Iterator;
/**
 * Class that finds anagrams of a word
 * @author Tori Chou, vcc2126
 * @version 1.0 December 18, 2023
 */
public class AnagramFinder{
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java AnagramFinder <word> <dictionary file> <bst|avl|hash>");
            System.exit(1);
        }
        File dictfile = new File(args[1]);
        if (!dictfile.exists()) {
            System.err.println("Error: Cannot open file '" + dictfile + "' for input.");
            System.exit(1);
        }

        // Initialize map using data structure according to command-line argument
        MyMap<String, MyList<String>> map = null;
        switch (args[2]) {
            case "bst" -> map = new BSTMap<>();
            case "avl" -> map = new AVLTreeMap<>();
            case "hash" -> map = new MyHashMap<>();
            default -> {
                System.err.println("Error: Invalid data structure '" + args[2] + "' received.");
                System.exit(1);
            }
        }

        // Insert dictionary into map, source: https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
        try {
            BufferedReader br = new BufferedReader(new FileReader(dictfile));
            String st;
            while ((st = br.readLine()) != null) {
                String key = insertionSort(st);
                MyList<String> value = map.get(key);
                MyList<String> temp = new MyLinkedList<>();
                temp.add(st);
                if (value == null) {
                    map.put(key, temp);
                } else {
                    value.add(st);
                    map.put(key, value);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: An I/O error occurred reading '" + dictfile + "'.");
            System.exit(1);
        }
        findAnagrams(args[0], map);
    }

    /**
     * Processes the word to find its anagrams and prints in alphabetical order
     * @param word the user's input word
     * @param map the map with key-value pairs from the dictionary
     *
     */
    private static void findAnagrams(String word, MyMap<String, MyList<String>> map) {
        MyList<String> anagrams = map.get(insertionSort(word));
        if (anagrams == null || anagrams.size() <= 1) {
            System.out.println("No anagrams found.");
        } else {
            String[] output = new String[anagrams.size()];
            int i = 0;
            Iterator<String> iter = anagrams.iterator();
            while (iter.hasNext()) {
                String curr = iter.next();
                if (!curr.equals(word)) {
                    output[i] = curr;
                    i++;
                }
            }
            // cut off end of array if original word was present in anagrams
            if (output[output.length-1] == null) {
                String[] copy = new String[anagrams.size()-1];
                System.arraycopy(output, 0, copy, 0, copy.length);
                insertionSort(copy);
                for (String item : copy) {
                    System.out.println(item);
                }
            } else {
                insertionSort(output);
                for (String item : output) {
                    System.out.println(item);
                }
            }
        }
    }

    /**
     * Returns the lower-case, sorted key using Insertion Sort
     * @param val the word to be sorted as to obtain the key
     * @return the key
     */
    private static String insertionSort(String val) {
        char[] word = val.toLowerCase().toCharArray();
        for (int i = 1; i < word.length; ++i) {
            int k;
            char current = word[i];
            for (k = i - 1; k >= 0 && word[k] > current; --k) {
                word[k + 1] = word[k];
            }
            word[k + 1] = current;
        }
        return String.valueOf(word);
    }

    /**
     * Sorts array of anagrams lexicographically using Insertion Sort
     * @param arr the array of anagrams to be sorted
     */
    private static void insertionSort(String[] arr) {
        for (int i = 1; i < arr.length; ++i) {
            int k;
            String current = arr[i];
            for (k = i - 1; k >= 0 && arr[k].compareTo(current) > 0; --k) {
                arr[k + 1] = arr[k];
            }
            arr[k + 1] = current;
        }
    }
}
