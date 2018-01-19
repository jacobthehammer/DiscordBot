package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import org.apache.commons.validator.UrlValidator;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class NetworkHelper {
    private static final UrlValidator VALIDATOR = new UrlValidator(new String[]{"http", "https"});
    private static final Map<String, Boolean> CACHE = new ConcurrentHashMap<>();
    public static String stripProtocol(String s){
        if (s.startsWith("http")) {
            s = s.substring(4);
            if (s.startsWith("s")) s = s.substring(1);
            s = s.substring(3);
        }
        return s;
    }
    public static boolean isValid(String url){
        return CACHE.computeIfAbsent(url, s -> {
            try{Jsoup.connect((url.startsWith("http") ? "" : "https://") + url).userAgent(ConfigProvider.BOT_SETTINGS.userAgent()).get();
                return VALIDATOR.isValid((url.startsWith("http") ? "" : "https://") + url);
            } catch (IllegalArgumentException | IOException e) {return false;}
        });
    }
}
