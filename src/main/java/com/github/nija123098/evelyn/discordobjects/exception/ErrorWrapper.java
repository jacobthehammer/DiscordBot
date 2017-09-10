package com.github.nija123098.evelyn.discordobjects.exception;

import com.github.nija123098.evelyn.util.Care;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

/**
 * An class to assist in error wrapping and rate limiting.
 *
 * All Discord API methods that can throw errors should be
 * wrapped with this class' methods in order to wrap any
 * errors that those methods throw as well as handle rate
 * limiting.  In the case of rate limiting the methods will
 * block until the API throws an error or the API request
 * is completed.  This is to ensure that at the end of the
 * method call the action is completed so that a request
 * will not fail without the bot being notified programmatically.
 *
 * @author nija123098
 * @since 2.0.0
 * @see MissingPermException
 * @see DException
 */
public class ErrorWrapper {
    private static final String CLOUDFLAIR_MESSAGE =
            "<html>\n" +
                    "<head><title>400 Bad Request</title></head>\n" +
                    "<body bgcolor=\"white\">\n" +
                    "<center><h1>400 Bad Request</h1></center>\n" +
                    "<hr><center>cloudflare-nginx</center>\n" +
                    "</body>\n" +
                    "</html>";
    public static <E> E wrap(Request<E> request) {
        return innerWrap(request);
    }
    public static void wrap(VoidRequest request) {
        innerWrap(request);
    }
    private static <E> E innerWrap(Request<E> request) {
        try {
            return RequestBuffer.request((RequestBuffer.IRequest<E>) request::request).get();
        } catch (MissingPermissionsException e){
            throw new MissingPermException(e);
        } catch (DiscordException e){
            if (e.getMessage().startsWith(CLOUDFLAIR_MESSAGE)) {
                Care.lessSleep(250);
                return innerWrap(request);
            }
            throw new DException(e);
        }
    }
    @FunctionalInterface
    public interface Request<T> {
        T request();
    }
    @FunctionalInterface
    public interface VoidRequest extends Request<Void>{
        default Void request(){
            doRequest();
            return null;
        }
        void doRequest();
    }
}
