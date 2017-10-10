package com.github.nija123098.evelyn.launcher;

import com.wezinkhof.configuration.ConfigurationOption;

import java.io.File;

/**
 * A class which utilizes reflection to get values from a file.
 *
 * Values indicated here are defaults.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class BotConfig {
    @ConfigurationOption
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
    @ConfigurationOption
    public static String BOT_TOKEN = "Evelyn's Token";
    @ConfigurationOption
    public static String CONTAINER_PATH = new File(BotConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + "\\";
    @ConfigurationOption
    public static String AUDIO_PATH = CONTAINER_PATH + "musicfiles\\";
    @ConfigurationOption
    public static String TEMP_PATH = CONTAINER_PATH + "tempfiles\\";
    @ConfigurationOption
    public static String NEURAL_NET_FOLDER = CONTAINER_PATH + "neuralnets\\";
    @ConfigurationOption
    public static String CONTRIBUTOR_SIGN_ROLE = "-1";
    @ConfigurationOption
    public static String SUPPORTER_SIGN_ROLE = "-1";
    @ConfigurationOption
    public static String GIPHY_TOKEN = null;
    @ConfigurationOption
    public static long IMAGE_LOAD_CHANNEL = 0L;
    @ConfigurationOption
    public static String SUPPORT_SERVER = "support-server";
    @ConfigurationOption
    public static String GOOGLE_API_KEY = "api-key";
    @ConfigurationOption
    public static String TWITCH_ID = null;
    @ConfigurationOption
    public static String YT_DL_PATH = CONTAINER_PATH + "youtube-dl.exe";
    @ConfigurationOption
    public static String FFM_PEG_PATH = CONTAINER_PATH + "ffmpeg.exe";
    @ConfigurationOption
    public static String FF_PROBE_PATH = CONTAINER_PATH + "ffprobe.exe";
    @ConfigurationOption
    public static String LANGUAGE_FILTERING_PATH = CONTAINER_PATH + "LanguageFiltering.txt";
    @ConfigurationOption
    public static String FAKE_DANGER_PATH = CONTAINER_PATH + "FakeDangerContents.txt";
    @ConfigurationOption
    public static String STATS_OVER_TIME = CONTAINER_PATH + "TimeStats.txt";
    @ConfigurationOption
    public static String VERIFIED_GAMES = CONTAINER_PATH + "VerifiedGames.txt";
    @ConfigurationOption
    public static String BADGE_PATH = CONTAINER_PATH + "badges\\";
    @ConfigurationOption
    public static int MUSIC_DOWNLOAD_THREAD_COUNT = 1;
    @ConfigurationOption
    public static String AUDIO_FORMAT = "mp3";
    @ConfigurationOption
    public static String AUDIO_FILE_TYPES = "opus, mp3";
    @ConfigurationOption
    public static String CAT_API_TOKEN = null;
    @ConfigurationOption
    public static final String RIOT_GAMES_TOKEN = "riot-token";
    @ConfigurationOption
    public static String DB_HOST = "localhost";
    @ConfigurationOption
    public static String DB_PORT = "3306";
    @ConfigurationOption
    public static String DB_NAME = "emilybot";
    @ConfigurationOption
    public static String DB_USER = "root";
    @ConfigurationOption
    public static String DB_PASS = "idlikenottobeblacklistedsostopputtingthingsabouthittlerhereoriwillforceyoutomakepullrequests";
    @ConfigurationOption
    public static String BITLY_TOKEN = "";
    @ConfigurationOption
    public static Boolean VOICE_COMMANDS_ENABLED = false;
    @ConfigurationOption
    public static String TWITTER_KEY = "twitter-key";
    @ConfigurationOption
    public static String TWITTER_SECRET = "twitter-secret";
    @ConfigurationOption
    public static Boolean GHOST_MODE = false;
    @ConfigurationOption
    public static Boolean TYPING_ENABLED = false;
    @ConfigurationOption
    public static Integer REQUIRED_PLAYS_TO_DOWNLOAD = 2;
    @ConfigurationOption
    public static Integer TOTAL_EMILYS = 1;
    @ConfigurationOption
    public static Integer EMILY_NUMBER = 0;
    @ConfigurationOption
    public static Integer MESSAGE_FILTERING_SERVER_SIZE = 150;
    @ConfigurationOption
    public static Long TRACK_EXPIRATION_TIME = 432000000L;
    @ConfigurationOption
    public static Boolean TESTING_MODE = false;
}
