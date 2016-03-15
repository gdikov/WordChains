import java.util.*;

/**
 * Created by G. Dikov on 07/11/15.
 */
public class SearchAlgorithm extends Thread{

    private static Map<String, List<PrefixSuffixNode>> suffixNeighbouringPool = new Hashtable<String, List<PrefixSuffixNode>>();
    private static Map<String, List<PrefixSuffixNode>> prefixNeighbouringPool = new Hashtable<String, List<PrefixSuffixNode>>();

    String threadID;

    Map<String, PrefixSuffixNode> searchSpace;

    String startingNode;
    String goalNode;

    Queue<PrefixSuffixNode> bfsQueue;

    List<String> path;

    public SearchAlgorithm(Map<String, PrefixSuffixNode> searchSpace, String threadID, String startingNode, String goalNode){
        this.searchSpace = searchSpace;
        this.threadID = threadID;

        if (this.threadID.equals("start")){
            this.startingNode = startingNode;
            this.goalNode = goalNode;
        }else {
            this.startingNode = goalNode;
            this.goalNode = startingNode;
        }
        this.path = new ArrayList<String>();

        this.bfsQueue = new LinkedList<PrefixSuffixNode>();
        String truncatedStartingNode = this.startingNode.substring(0, 2) +
                this.startingNode.substring(this.startingNode.length() - 2, this.startingNode.length());
        String truncatedGoalNode = this.goalNode.substring(0, 2) +
                this.goalNode.substring(this.goalNode.length() - 2, this.goalNode.length());
        if (searchSpace.containsKey(truncatedStartingNode)){
            bfsQueue.offer(searchSpace.get(truncatedStartingNode));
            if (this.threadID.equals("start")){
                searchSpace.get(truncatedStartingNode).isVisitedFromStartDirection = true;
                searchSpace.get(truncatedGoalNode).isVisitedFromGoalDirection = true;
            }else {
                searchSpace.get(truncatedStartingNode).isVisitedFromGoalDirection = true;
                searchSpace.get(truncatedGoalNode).isVisitedFromStartDirection = true;
            }
        }else {
            System.out.println("Starting Word in unknown! It should have been added in main()!");
            return;
        }
    }

    @Override
    public void run() {
        while (!this.bfsQueue.isEmpty()){
            PrefixSuffixNode currentNode = this.bfsQueue.poll();
            if (currentNode.isVisitedFromGoalDirection && currentNode.isVisitedFromStartDirection){
                System.out.println("Path found.");
                printPath(currentNode);
                return;
            }

            List<PrefixSuffixNode> neighbours = findPrefixNeighbours(currentNode.prefix);
            neighbours.addAll(findSuffixNeighbours(currentNode.suffix));

            for (PrefixSuffixNode neighbour : neighbours){
                if (this.threadID.equals("start")){
                    // stop searching immediately
                    if (neighbour.isVisitedFromGoalDirection){
                        System.out.println("Path found.");
                        neighbour.parent = currentNode;
                        printPath(neighbour);
                        return;
                    }
                    if (neighbour.isVisitedFromStartDirection){
                        continue;
                    }
                    neighbour.isVisitedFromStartDirection = true;
                }else {
                    // stop searching too
                    if (neighbour.isVisitedFromStartDirection){
                        System.out.println("Path found.");
                        neighbour.parent = currentNode;
                        printPath(neighbour);
                        return;
                    }
                    if (neighbour.isVisitedFromGoalDirection){
                        continue;
                    }
                    neighbour.isVisitedFromGoalDirection = true;
                }
                neighbour.parent = currentNode;
                this.bfsQueue.offer(neighbour);
            }
        }
        System.out.println("No more words to explore. No chain found.");
    }

    private List<PrefixSuffixNode> findSuffixNeighbours(String suffix){
        List<PrefixSuffixNode> neighbours = new ArrayList<PrefixSuffixNode>();
        if (suffixNeighbouringPool.containsKey(suffix)){
            return suffixNeighbouringPool.get(suffix);
        }
        for (char firstLetter = 'a'; firstLetter <= 'z'; firstLetter++){
            for (char secondLetter = 'a'; secondLetter <= 'z'; secondLetter++){
                if (this.searchSpace.containsKey(suffix + firstLetter + secondLetter)){
                    neighbours.add(this.searchSpace.get(suffix + firstLetter + secondLetter));
                }
            }
        }
        suffixNeighbouringPool.put(suffix, neighbours);
        return neighbours;
    }

    private List<PrefixSuffixNode> findPrefixNeighbours(String prefix){
        List<PrefixSuffixNode> neighbours = new ArrayList<PrefixSuffixNode>();
        if (prefixNeighbouringPool.containsKey(prefix)){
            return prefixNeighbouringPool.get(prefix);
        }
        for (char firstLetter = 'a'; firstLetter <= 'z'; firstLetter++){
            for (char secondLetter = 'a'; secondLetter <= 'z'; secondLetter++){
                if (this.searchSpace.containsKey("" + firstLetter + secondLetter + prefix)){
                    neighbours.add(this.searchSpace.get("" + firstLetter + secondLetter + prefix));
                }
            }
        }
        prefixNeighbouringPool.put(prefix, neighbours);
        return neighbours;
    }

    private void printPath(PrefixSuffixNode currentNode){
        int index;
        if (WordChain.MULTITHREADING){
            index = currentNode.prefixWordList.indexOf(this.goalNode);
            if (index == -1){
                if ((index = currentNode.suffixWordList.indexOf(this.goalNode)) == -1){
                    this.path.add(currentNode.oneCompleteWord);
                }else{
                    this.path.add(currentNode.suffixWordList.get(index));
                }
            }else {
                this.path.add(currentNode.prefixWordList.get(index) + " - ");
            }
        }else{
            index = currentNode.prefixWordList.indexOf(this.goalNode);
            if (index == -1){
                this.path.add(currentNode.suffixWordList.get(currentNode.suffixWordList.indexOf(this.goalNode)));
            }else {
                this.path.add(currentNode.prefixWordList.get(index));
            }
        }

        currentNode = currentNode.parent;
        while (!currentNode.prefix.equals(this.startingNode.substring(0, 2)) ||
                !currentNode.suffix.equals(this.startingNode.substring(this.startingNode.length() - 2, this.startingNode.length()))){
            this.path.add(currentNode.oneCompleteWord);
            currentNode = currentNode.parent;
        }

        if (WordChain.MULTITHREADING){
            index = currentNode.prefixWordList.indexOf(this.startingNode);
            if (index == -1){
                if ((index = currentNode.suffixWordList.indexOf(this.startingNode)) == -1){
                    this.path.add(currentNode.oneCompleteWord + " - ");
                } else {
                    this.path.add(currentNode.suffixWordList.get(index));
                }
            }else {
                this.path.add(currentNode.prefixWordList.get(index) + " - ");
            }
        }else {
            index = currentNode.prefixWordList.indexOf(this.startingNode);
            if (index == -1) {
                this.path.add(currentNode.suffixWordList.get(currentNode.suffixWordList.indexOf(this.startingNode)));
            } else {
                this.path.add(currentNode.prefixWordList.get(index));
            }
        }
    }

}
