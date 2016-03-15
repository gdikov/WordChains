import java.util.List;

/**
 * Created by G. Dikov on 07/11/15.
 */
public class PrefixSuffixNode {
    String prefix;
    String suffix;
    String oneCompleteWord;

    List<String> prefixWordList;
    List<String> suffixWordList;

    PrefixSuffixNode parent;

    boolean isVisitedFromStartDirection;
    boolean isVisitedFromGoalDirection;

    public PrefixSuffixNode(String prefix, String suffix, String completeWord, List<String> prefixWordList, List<String> suffixWordList){
        this.prefix = prefix;
        this.suffix = suffix;
        this.oneCompleteWord = completeWord;
        this.prefixWordList = prefixWordList;
        this.suffixWordList = suffixWordList;
        this.isVisitedFromStartDirection = this.isVisitedFromGoalDirection = false;
        this.parent = null;
    }
}
