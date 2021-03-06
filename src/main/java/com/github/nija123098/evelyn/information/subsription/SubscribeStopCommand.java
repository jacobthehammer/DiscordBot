package com.github.nija123098.evelyn.information.subsription;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class SubscribeStopCommand extends AbstractCommand {
    public SubscribeStopCommand() {
        super(SubscribeCommand.class, "stop", null, null, "end", "Ends a subscription for the channel");
    }
    @Command
    public static void command(@Argument(optional = true, info = "The thing subscribed to") SubscriptionLevel level, Channel channel) {
        ConfigHandler.alterSetting(SubscriptionsConfig.class, channel, levels -> levels.remove(level));
    }
}
