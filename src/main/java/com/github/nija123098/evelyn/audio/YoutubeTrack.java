package com.github.nija123098.evelyn.audio;

import com.github.nija123098.evelyn.util.CallBuffer;
import com.github.nija123098.evelyn.util.YTUtil;

/**
 * The track defining the audio portion of the video.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class YoutubeTrack extends DownloadableTrack {
    private static final CallBuffer CALL_BUFFER = new CallBuffer("Youtube-Name-Resolver", 250);
    static {
        registerTrackType(YoutubeTrack.class, YoutubeTrack::new, YoutubeTrack::new);
    }
    private transient String name;
    private transient Boolean available;
    public YoutubeTrack(String id) {
        super(id);
        CALL_BUFFER.call(this::isAvailable);
    }
    protected YoutubeTrack() {}
    private void loadName() {
        if (this.name == null) {
            this.name = YTUtil.getTrackName(this.getCode());
            if (this.name == null) {
                this.available = false;
                this.name = "Not Available";
            }
        }
    }
    @Override
    public String getName() {
        loadName();
        return this.name != null ? this.name : "I'm loading the song name as fast as I can";
    }
    public String getSource() {
        return "https://www.youtube.com/watch?v=" + this.getCode();
    }
    @Override
    public String getInfo() {
        return null;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean isAvailable() {
        loadName();
        if (this.available == null || this.available) {// recheck to make sure it does not become unavailable
            this.available = YTUtil.isAvailable(this.getCode());
        }
        return available;
    }

    @Override
    public String getPreviewURL() {
        return "https://i3.ytimg.com/vi/" + this.getCode() + "/0.jpg";
    }
    @Override
    public boolean download() {
        return this.isDownloaded() && (this.getLength() != Long.MAX_VALUE || this.getLength() == 0) && this.actualDownload();
    }
    public void setName(String name) {
        this.name = name;
        this.available = true;
    }
}
