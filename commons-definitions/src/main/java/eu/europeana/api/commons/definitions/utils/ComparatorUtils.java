package eu.europeana.api.commons.definitions.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class used for detection of near duplicate strings
 * @author GordeaS
 *
 */
public class ComparatorUtils implements Comparator<String>, Serializable {
    private static final String PUNCTUATION_REGEX = "[\\p{Punct}]+";

    public ComparatorUtils() {
    }

    
    /**
     * remove punctuation
     * @param value input
     * @return processed string
     */
    public static String stripPunctuation(String value) {
        return value.replaceAll(PUNCTUATION_REGEX, "");
    }

    /**
     * remove punctuation
     * @param list input
     * @return processed list
     */
    public static List<String> stripPunctuations(List<String> list) {
      if(list == null) {
        return Collections.emptyList();
      }
      
      List<String> listWithoutPunctuation = new ArrayList<>(list.size());
      Iterator<String> var2 = list.iterator();

        while(var2.hasNext()) {
            listWithoutPunctuation.add(stripPunctuation(var2.next()));
        }

        return listWithoutPunctuation;
    }

    /**
     * remove duplicates
     * @param listWithDuplicates
     * @return processed list
     */
    public static List<String> removeDuplicates(List<String> listWithDuplicates) {
        Set<String> set = new TreeSet<>(new ComparatorUtils());
        set.addAll(listWithDuplicates);
        listWithDuplicates.clear();
        listWithDuplicates.addAll(set);
        return listWithDuplicates;
    }

    /**
     * verify if the two values differ only in empty spaces
     * @param value1 - input string 1
     * @param value2 - input string 2
     * @return
     */
    public static boolean sameValueWithoutSpace(String value1, String value2) {
        return value1.replaceAll("\\s+", "").equalsIgnoreCase(value2.replaceAll("\\s+", ""));
    }

    /**
     * compare method
     */
    public int compare(String o1, String o2) {
        return stripPunctuation(o1).equalsIgnoreCase(stripPunctuation(o2)) ? 0 : 1;
    }
}
