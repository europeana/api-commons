package eu.europeana.api.commons.definitions.utils;

import java.util.Locale;
import java.util.Set;

public class LanguageUtils {

  private static Set<String> ISO_LANGUAGES;

  public static boolean isIsoLanguage(String lang) {
    if(ISO_LANGUAGES==null) {
      ISO_LANGUAGES = Set.of(Locale.getISOLanguages());
    }
    return ISO_LANGUAGES.contains(lang);
  }
}
