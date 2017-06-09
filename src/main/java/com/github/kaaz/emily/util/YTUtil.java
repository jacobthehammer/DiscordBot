package com.github.kaaz.emily.util;

import org.apache.commons.validator.UrlValidator;

/**
 * Made by nija123098 on 6/6/2017.
 */
public class YTUtil {
    private static final String BASE_VIDEO_URL = "https://www.youtube.com/watch?v=";
    private static final String BASE_PLAYLIST_URL = "https://www.youtube.com/playlist?list=";
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(new String[]{"http", "https"});
    private static boolean isYoutubeVideoCode(String s){
        if (s.length() != 11) return false;
        for (int i = 0; i < 11; i++) if (!Character.isLetterOrDigit(s.charAt(i)) || s.charAt(i) == '-' || s.charAt(i) == '_') return false;
        URL_VALIDATOR.isValid(BASE_VIDEO_URL + s);
        return true;
    }
    public static String extractVideoCode(String s){
        s = NetworkHelper.stripProtocol(s);
        if (s.startsWith("www.youtube.com/watch?v=")) s = s.substring(24);
        else if (s.startsWith("youtu.be/")) s = s.substring(9);
        else if (isYoutubeVideoCode(s.substring(0, 11))) return s;
        else return null;
        if (isYoutubeVideoCode(s.substring(0, 11))) return s;
        return null;
    }
    private static boolean isYoutubePlaylistCode(String s){
        if (s.length() != 34) return false;
        for (int i = 0; i < 34; i++) if (!Character.isLetterOrDigit(s.charAt(i)) || s.charAt(i) == '-' || s.charAt(i) == '_') return false;
        URL_VALIDATOR.isValid(BASE_PLAYLIST_URL + s);
        return true;
    }
    public static String extractPlaylistCode(String s){
        s = NetworkHelper.stripProtocol(s);
        if (s.startsWith("www.youtube.com/playlist?list=")) s = s.substring(30);
        if (isYoutubePlaylistCode(s.substring(0, 34))) return s;
        return null;
    }
}
