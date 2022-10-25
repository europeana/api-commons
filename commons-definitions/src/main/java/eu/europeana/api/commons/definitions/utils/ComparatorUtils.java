package eu.europeana.api.commons.definitions.utils;

import java.util.*;

public class ComparatorUtils implements Comparator<String> {
    private static final String PUNCTUATION_REGEX = "[\\p{Punct}]+";

    public ComparatorUtils() {
    }

    public static String stripPunctuation(String value) {
        return value.replaceAll("[\\p{Punct}]+", "");
    }

    public static List<String> stripPunctuations(List<String> list) {
        List<String> listWithoutPunctuation = new ArrayList();
        Iterator var2 = list.iterator();

        while(var2.hasNext()) {
            String value = (String)var2.next();
            listWithoutPunctuation.add(stripPunctuation(value));
        }

        return listWithoutPunctuation;
    }

    public static List<String> removeDuplicates(List<String> listWithDuplicates) {
        Set<String> set = new TreeSet(new ComparatorUtils());
        set.addAll(listWithDuplicates);
        listWithDuplicates.clear();
        listWithDuplicates.addAll(set);
        return listWithDuplicates;
    }

    public static boolean sameValueWithoutSpace(String value1, String value2) {
        return value1.replaceAll("\\s+", "").equalsIgnoreCase(value2.replaceAll("\\s+", ""));
    }

    public int compare(String o1, String o2) {
        return stripPunctuation(o1).equalsIgnoreCase(stripPunctuation(o2)) ? 0 : 1;
    }
}
