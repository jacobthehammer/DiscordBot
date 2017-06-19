package com.github.kaaz.emily.exeption;

import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;

/**
 * Made by nija123098 on 3/31/2017.
 */
public class BotException extends RuntimeException {
    public BotException() {
    }

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotException(Throwable cause) {
        super(cause);
    }

    public void makeMessage(Channel channel){
        new MessageMaker(channel).asExceptionMessage(this).send();
    }
}