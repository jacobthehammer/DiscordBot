package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.service.AbstractService;
import com.github.nija123098.evelyn.util.Log;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MusicDownloadService extends AbstractService {
    private static final ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(1, ConfigProvider.AUDIO_SETTINGS.musicDownloadThreads(), 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), r -> {
        Thread t = new Thread(r, "Music Download Thread");
        t.setDaemon(true);
        return t;
    }, (r, executor) -> Log.log("Music download queue exceeded"));
    private static final Consumer<DownloadableTrack> NOTHING = o -> {};
    private static final Set<DownloadableTrack> DOWNLOADING = ConcurrentHashMap.newKeySet();
    private static final List<List<DownloadableTrack>> Q = new CopyOnWriteArrayList<>();
    private static final Map<DownloadableTrack, Integer> PRIORITY_MAP = new ConcurrentHashMap<>();
    private static final Map<DownloadableTrack, Set<Consumer<DownloadableTrack>>> CONSUMER_MAP = new ConcurrentHashMap<>();
    private static DownloadableTrack getNext() {
        for (int i = Q.size() - 1; i > -1; --i) if (!Q.get(i).isEmpty()) return Q.get(i).remove(0);
        return null;
    }
    public MusicDownloadService() {
        super(-1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> DOWNLOADING.forEach(track -> track.file().delete())));
    }
    public static boolean isDownloaded(DownloadableTrack track) {
        return track.file().exists() && !DOWNLOADING.contains(track);
    }
    public static void queueDownload(int priority, DownloadableTrack track, Consumer<DownloadableTrack> consumer) {
        if (consumer == null) consumer = NOTHING;
        if (isDownloaded(track)) {
            consumer.accept(track);
            return;
        }
        if (DOWNLOADING.contains(track)) {
            CONSUMER_MAP.get(track).add(consumer);
            return;
        }
        CONSUMER_MAP.computeIfAbsent(track, t -> ConcurrentHashMap.newKeySet()).add(consumer);
        Integer originalPriority = PRIORITY_MAP.get(track);
        if (originalPriority != null) {
            if (originalPriority > priority) return;
            Q.get(originalPriority).remove(track);
        }
        PRIORITY_MAP.put(track, priority);
        ensureQCapacity(priority);
        Q.get(priority).add(track);
        POOL_EXECUTOR.execute(() -> {
            DownloadableTrack t = getNext();// getting the next track preserves priority
            if (t == null) return;// should never happen
            DOWNLOADING.add(t);
            try{t.download();
            } catch (Exception e) {Log.log("Exception downloading song", e);}
            DOWNLOADING.remove(t);
            CONSUMER_MAP.remove(t).forEach(c -> c.accept(track));
        });
    }
    @Override
    public boolean mayBlock() {
        return true;
    }
    private static void ensureQCapacity(int size) {
        while (size > Q.size() - 1) Q.add(new CopyOnWriteArrayList<>());
    }
    @Override
    public void run() {}
}
