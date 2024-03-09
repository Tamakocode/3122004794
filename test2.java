
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class test2 {

        public static double jaccardSimilarity(Set<String> set1, Set<String> set2) {
            Set<String> intersection = new HashSet<>(set1);
            intersection.retainAll(set2);

            Set<String> union = new HashSet<>(set1);
            union.addAll(set2);

            return (double) intersection.size() / union.size();
        }

        public static void main(String[] args) {
            Set<String> text1 = new HashSet<>(Arrays.asList("This is the first document.".split(" ")));
            Set<String> text2 = new HashSet<>(Arrays.asList("This document is the second document.".split(" ")));

            double similarity = jaccardSimilarity(text1, text2);
            System.out.println(similarity);
        }



}
