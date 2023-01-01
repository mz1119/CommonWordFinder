/**
 * Finds the n most common words in a .txt file
 * @author Max Zhang (mz2956)
 */

import java.io.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Arrays;


public class CommonWordFinder {
    public static void main(String[] args){
        //check if incorrect number of arguments provided
        if (args.length < 2 || args.length > 3){
            System.err.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
            System.exit(1);
        }

        //check if file can be opened + creating buffered reader for file
        //read through Java API to figure out buffered reader
        //https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(args[0]));
        } catch (FileNotFoundException e) {
            System.err.println("Error: Cannot open file '" + args[0] + "' for input.");
            System.exit(1);
        }

        //assign map based off user input + check if valid
        //figuring out how enhanced switches work from below link
        //https://www.vojtechruzicka.com/java-enhanced-switch/
        MyMap<String, Integer> map = switch (args[1]){
            case "bst" ->  new BSTMap<>();
            case "avl" -> new AVLTreeMap<>();
            case "hash" -> new MyHashMap<>();
            default -> {
                System.err.println("Error: Invalid data structure '" + args[1] + "' received.");
                System.exit(1);
                yield null;
            }
        };

        //set default limit + change if user inputs custom limit
        int limit = 10;
        if (args.length == 3){
            try{
                limit = Integer.parseInt(args[2]);
                if (limit <= 0){
                    System.err.println("Error: Invalid limit '" + args[2] + "' received.");
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Invalid limit '" + args[2] + "' received.");
                System.exit(1);
            }
        }

        //going through text, adding each word to map
        try {
            StringBuilder builder = new StringBuilder();
            String word;
            while (reader.ready()){
                char c = (char) reader.read();
                //a letter or single quote
                if (c == '\'' || Character.isLowerCase(c)) {
                    builder.append(c);
                    continue;
                } else if (Character.isUpperCase(c)){
                    builder.append(Character.toLowerCase(c));
                    continue;
                } else if (c == '-'){
                    if (builder.length() == 0){ //don't add hyphens at start of word
                        continue;
                    } else {
                        builder.append(c);
                        continue;
                    }
                } else if (c == ' ' || Character.toString(c).equals(System.lineSeparator())) {
                    if (builder.length() == 0){ //don't let spaces get counted as words
                        continue;
                    }
                    //otherwise don't continue, let pass onto rest of code
                } else {
                    continue;
                }

                word = builder.toString().toLowerCase();
                builder.setLength(0); //clear builder

                if (map.get(word) != null){ //if key already in map, incremenent value by 1
                    map.put(word, map.get(word) + 1);
                } else { //if key not in map, set value to 1
                    map.put(word, 1);
                }
            }
            word = builder.toString().toLowerCase();
            if (map.get(word) != null){ //if key already in map, incremenent value by 1
                map.put(word, map.get(word) + 1);
            } else { //if key not in map, set value to 1
                map.put(word, 1);
            }
        } catch (IOException e) {
            System.err.println("Error: An I/O error occurred reading '" + args[0] + "'.");
            System.exit(1);
        }

        //display unique word count
        int mapSize = map.size();
        System.out.println("Total unique words: " + mapSize);

        int displayCount = Math.min(limit, mapSize);

        //turning entries from map into array
        Iterator<Entry<String, Integer>> iterator = map.iterator();
        Entry<String, Integer>[] list = new Entry[mapSize];
        int counter = 0;
        while(iterator.hasNext()){ //adding each entry of iterator into list
            list[counter++] = iterator.next();
        }

        //sorting list using custom comparator, figured out by reading Java API
        //https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html#sort(T[],%20java.util.Comparator)
        //see Comparer class below for more details
        Arrays.sort(list, new Comparer());

        //printing out words and counts
        StringBuilder builder = new StringBuilder();
        int displayCountLength = String.valueOf(displayCount).length();
        int longestWordLength = 0;
        for (int i = 1; i <= displayCount; i++){
            if (list[mapSize - i].key.length() > longestWordLength){
                longestWordLength = list[mapSize - i].key.length();
            }
        }
        for (int i = 1; i <= displayCount; i++){
            int numberWhiteSpaceLength = displayCountLength - String.valueOf(i).length();
            int index = mapSize - i;
            int wordWhiteSpaceLength = longestWordLength - list[index].key.length();
            builder.append(" ".repeat(numberWhiteSpaceLength));
            builder.append(i);
            builder.append(". ");
            builder.append(list[index].key);
            builder.append(" ".repeat(wordWhiteSpaceLength + 1));
            builder.append(list[index].value);
            System.out.println(builder);
            builder.setLength(0);
        }
    }
}

//Read Java API to figure out how comparators work
//https://docs.oracle.com/javase/7/docs/api/java/util/Comparator.html
class Comparer implements Comparator<Entry<String, Integer>> {
    @Override
    public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
        if (o1.value < o2.value) return -1; //o1 count less than o2 count
        else if (o1.value > o2.value) return 1; //o1 count more than o2 count
        else {//counts equal, use compareTo to sort alphabetically
            int comparison =  o1.key.compareTo(o2.key);
            if (comparison < 0) return 1; //want lower alphabetically to be thought of as "larger"
            else if (comparison == 0) return 0;
            else return -1; ///want higher alphabetically to be thought of as "smaller"
        }
    }
}