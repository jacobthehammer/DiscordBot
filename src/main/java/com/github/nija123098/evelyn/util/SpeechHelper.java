package com.github.nija123098.evelyn.util;

import com.darkprograms.speech.synthesiser.Synthesiser;
import com.github.nija123098.evelyn.exception.DevelopmentException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.nija123098.evelyn.botconfiguration.ConfigProvider.LIBRARIES_FILES;
import static com.github.nija123098.evelyn.util.FileHelper.getTempFile;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.regex.Pattern.quote;
import static org.apache.commons.io.IOUtils.copy;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SpeechHelper {
    private static final LangString GIBERISH = new LangString(true, "You sent me gibberish");
    private static final Map<String, Map<String, File>> MAP = new ConcurrentHashMap<>();
    private static final Synthesiser SYNTHESISER = new Synthesiser();

    public static File getFile(LangString string, String lang) {
        String content = string.translate(lang);
        return MAP.computeIfAbsent(lang, s -> new HashMap<>()).computeIfAbsent(content, s -> {
            if (s.length() < 101) return getFile(string.translate(lang), lang);
            File file = null;
            String total = "";
            String[] split = s.split(quote(". "));
            if (split.length == 1) return getFile(GIBERISH.translate(lang), lang);
            for (int i = 0; i < split.length; i++) {
                if (split[i].length() + total.length() > 100) {
                    --i;
                    file = merge(file, getFile(total, lang));
                }
                if (i == split.length - 1) return merge(file, getFile(total, lang));
            }
            throw new DevelopmentException("nija123098 did something horribly wrong!");
        });// should never be called since "i == split.length - 1" should happen on the last iteration
    }

    private static File getFile(String content, String lang) {
        try {
            File file = getTempFile("speech", "mp3");
            SYNTHESISER.setLanguage(lang);
            copy(SYNTHESISER.getMP3Data(content), new FileOutputStream(file));
            MAP.get(lang).put(content, file);
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Error during audio synthesis", e);
        }
    }

    private static File merge(File first, File second) {
        if (first == null) return second;
        File to = getTempFile("speech", "mp3");
        List<String> arguments = new ArrayList<>();
        arguments.add(LIBRARIES_FILES.ffmPeg());
        arguments.add("-i");
        arguments.add("\"concat:" + first.getPath() + "|" + second.getPath() + "\"");
        arguments.add("-acodec");
        arguments.add("copy");
        arguments.add(to.getPath());
        try {
            Process process = new ProcessBuilder(arguments).start();
            new StreamGobler(process.getInputStream(), out).start();
            new StreamGobler(process.getErrorStream(), err).start();
            process.waitFor(10, SECONDS);
            return to;
        } catch (IOException | InterruptedException e) {
            throw new DevelopmentException("Exception while merging audio files", e);
        }
    }
}
