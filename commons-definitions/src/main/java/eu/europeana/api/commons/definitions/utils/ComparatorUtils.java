package eu.europeana.api.commons.definitions.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class used for detection of near duplicate strings
 * @author GordeaS
 *
 */
public final class ComparatorUtils implements Comparator<String>, Serializable {
    private static final String PUNCTUATION_REGEX = "[\\p{Punct}]+";
    static final long serialVersionUID = 1L;

    private ComparatorUtils() {
      super();
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
      for (String value : list) {
        listWithoutPunctuation.add(stripPunctuation(value));
      }
      return listWithoutPunctuation;
    }

    /**
     * remove duplicates from the input list
     * @param listWithDuplicates the input list 
     */
    public static void removeDuplicates(List<String> listWithDuplicates) {
        Set<String> set = new TreeSet<>(new ComparatorUtils());
        set.addAll(listWithDuplicates);
        listWithDuplicates.clear();
        listWithDuplicates.addAll(set);
    }

    /**
     * verify if the two values differ only in empty spaces
     * @param value1 - input string 1
     * @param value2 - input string 2
     * @return true if the two values differ only in empty spaces
     */
    public static boolean sameValueWithoutSpace(String value1, String value2) {
        if(value1 == null || value2 == null) {
          return false;
        }
        return value1.replaceAll("\\s+", "").equalsIgnoreCase(value2.replaceAll("\\s+", ""));
    }

    /**
     * compare method
     */
    public int compare(String o1, String o2) {
        return stripPunctuation(o1).equalsIgnoreCase(stripPunctuation(o2)) ? 0 : 1;
    }
}
