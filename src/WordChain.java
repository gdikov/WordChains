import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by G. Dikov on 07/11/15.
 */
public class WordChain {

    // multithreading still not working
    public static final boolean MULTITHREADING = false;

    public static void main(String[] args) {

        //take starting and ending word from input stream
        Scanner in = new Scanner(System.in);
        System.out.println("Starting Word: ");
        String startingWord = in.next();
        System.out.println("Ending Word: ");
        String goalWord = in.next();
        if (startingWord.equals(goalWord)){
            System.out.println("Chain: " + startingWord + "\n" +
                    "Chain Length: 0");
            return;
        }

        //measure total running time
        long begin = System.nanoTime();
        //this will represent the graph of all nodes containing the prefixes and suffixes of the words
        Map<String, PrefixSuffixNode> searchSpace = new Hashtable<String, PrefixSuffixNode>();

        Map<String, List<String>> allWordsWithSamePrefix = new Hashtable<String, List<String>>();
        Map<String, List<String>> allWordsWithSameSuffix = new Hashtable<String, List<String>>();
        //parse dictionary and create objects according to the prefixes and suffixes of the words
        try {
            BufferedReader br = new BufferedReader(new FileReader("wordsEn.txt"));
            String wordInDic;

            while ((wordInDic = br.readLine()) != null) {
                if (wordInDic.length() < 2){
                    continue;
                }
                String prefix = wordInDic.substring(0,2);
                String suffix = wordInDic.substring(wordInDic.length() - 2, wordInDic.length());

                if (allWordsWithSamePrefix.containsKey(prefix)){
                    allWordsWithSamePrefix.get(prefix).add(wordInDic);
                }else {
                    List<String> wordsWithPref = new ArrayList<String>();
                    wordsWithPref.add(wordInDic);
                    allWordsWithSamePrefix.put(prefix, wordsWithPref);
                }
                if (allWordsWithSameSuffix.containsKey(suffix)){
                    allWordsWithSameSuffix.get(suffix).add(wordInDic);
                }else {
                    List<String> wordsWithSuffix = new ArrayList<String>();
                    wordsWithSuffix.add(wordInDic);
                    allWordsWithSameSuffix.put(suffix, wordsWithSuffix);
                }

                PrefixSuffixNode node = new PrefixSuffixNode(prefix, suffix, wordInDic,
                        allWordsWithSamePrefix.get(prefix), allWordsWithSameSuffix.get(suffix));
                searchSpace.put(prefix+suffix, node);
            }
//            System.out.println(allWordsWithSamePrefix.toString());
//            System.out.println(allWordsWithSameSuffix.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //create starting and ending nodes
        String prefixOfStartingWord = startingWord.substring(0, 2);
        String suffixOfStartingWord = startingWord.substring(startingWord.length()-2, startingWord.length());
        String prefixOfGoalWord = goalWord.substring(0, 2);
        String suffixOfGoalWord = goalWord.substring(goalWord.length()-2, goalWord.length());

        if (searchSpace.containsKey(prefixOfStartingWord+suffixOfStartingWord)){
            if(!searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).prefixWordList.contains(startingWord)){
                searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).prefixWordList.add(startingWord);
            }
            if(!searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).suffixWordList.contains(startingWord)){
                searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).suffixWordList.add(startingWord);
            }
        }else {
            System.out.println("The word " + startingWord + " is not contained in the dictionary! It will be added.");
            searchSpace.put(prefixOfStartingWord+suffixOfStartingWord,
                    new PrefixSuffixNode(prefixOfStartingWord, suffixOfStartingWord, startingWord,
                            allWordsWithSamePrefix.get(prefixOfStartingWord),
                            allWordsWithSameSuffix.get(suffixOfStartingWord)));
            if (searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).prefixWordList == null){
                searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).prefixWordList = new ArrayList<String>();
                searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).prefixWordList.add(startingWord);
            }else {
                searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).prefixWordList.add(startingWord);
            }
            if (searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).suffixWordList == null){
                searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).suffixWordList = new ArrayList<String>();
                searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).suffixWordList.add(startingWord);
            }else {
                searchSpace.get(prefixOfStartingWord+suffixOfStartingWord).suffixWordList.add(startingWord);
            }
        }

        if (searchSpace.containsKey(prefixOfGoalWord+suffixOfGoalWord)){
            if(!searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).prefixWordList.contains(goalWord)){
                searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).prefixWordList.add(goalWord);
            }
            if(!searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).suffixWordList.contains(goalWord)){
                searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).suffixWordList.add(goalWord);
            }
        }else {
            System.out.println("The word " + goalWord + " is not contained in the dictionary! It will be added.");
            searchSpace.put(prefixOfGoalWord+suffixOfGoalWord,
                    new PrefixSuffixNode(prefixOfGoalWord, suffixOfGoalWord, goalWord,
                            allWordsWithSamePrefix.get(prefixOfGoalWord),
                            allWordsWithSameSuffix.get(suffixOfGoalWord)));

            if (searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).prefixWordList == null){
                searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).prefixWordList = new ArrayList<String>();
                searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).prefixWordList.add(goalWord);
            }else {
                searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).prefixWordList.add(goalWord);
            }
            if (searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).suffixWordList == null){
                searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).suffixWordList = new ArrayList<String>();
                searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).suffixWordList.add(goalWord);
            }else {
                searchSpace.get(prefixOfGoalWord+suffixOfGoalWord).suffixWordList.add(goalWord);
            }
        }

//        SearchAlgorithm exploreFromStart = new SearchAlgorithm(searchSpace, "start", startingWord, goalWord);
        SearchAlgorithm exploreFromGoal = new SearchAlgorithm(searchSpace, "goal", goalWord, startingWord);

//        exploreFromStart.start();
        exploreFromGoal.start();
        try {
//            exploreFromStart.join();
            exploreFromGoal.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(exploreFromStart.path);
        Collections.reverse(exploreFromGoal.path);
        System.out.println(exploreFromGoal.path.toString());
        long end = System.nanoTime();
        System.out.println("Elapsed running time: " + (end - begin)/1000000 + " ms");
    }
}
