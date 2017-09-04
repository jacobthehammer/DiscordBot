package com.github.kaaz.emily.information.subsription;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.information.configs.SubscriptionsConfig;

/**
 * Made by nija123098 on 5/24/2017.
 */
public class SubscribeStopCommand extends AbstractCommand {
    public SubscribeStopCommand() {
        super(SubscribeCommand.class, "stop", null, null, "end", "Ends a subscription for the channel");
    }
    @Command
    public static void command(@Argument(optional = true, info = "The thing subscribed to") SubscriptionLevel level, Channel channel){
        ConfigHandler.alterSetting(SubscriptionsConfig.class, channel, levels -> levels.remove(level));
    }
}
