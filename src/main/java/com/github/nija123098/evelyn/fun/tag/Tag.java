package com.github.nija123098.evelyn.fun.tag;

import com.github.nija123098.evelyn.discordobjects.wrappers.User;

import static java.lang.System.currentTimeMillis;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class Tag {
    private long makeTime;
    private String userID, name, content;

    public Tag(String userID, String name, String content) {
        this.makeTime = currentTimeMillis();
        this.userID = userID;
        this.name = name;
        this.content = content;
    }

    private Tag() {
    }

    public long getMakeTime() {
        return this.makeTime;
    }

    public String getUserID() {
        return this.userID;
    }

    public User getUser() {
        return User.getUser(this.userID);
    }

    public String getName() {
        return this.name;
    }

    public String getContent() {
        return this.content;
    }
}
