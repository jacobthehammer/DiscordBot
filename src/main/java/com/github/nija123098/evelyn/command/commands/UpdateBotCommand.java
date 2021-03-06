package com.github.nija123098.evelyn.command.commands;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.configs.LastBotUpdaterUseConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GlobalConfigurable;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.util.ExecuteShellCommand;
import com.github.nija123098.evelyn.util.HasteBin;
import com.github.nija123098.evelyn.util.PlatformDetector;

import java.awt.*;

/**
 * @author Celestialdeath99
 * @since 1.0.0
 */
public class UpdateBotCommand extends AbstractCommand {
    public UpdateBotCommand() {
        super("updatebot", ModuleLevel.DEVELOPMENT, "update, upgrade", null, "Updates bot to latest git version.");
    }
    @Command
    public void command(MessageMaker maker) {
        if (ConfigProvider.BOT_SETTINGS.isRunningInContainer()) {
            maker.withColor(new Color(175, 30,5));
            maker.getTitle().clear().appendRaw("\uD83D\uDEE0 Bot Updater \uD83D\uDEE0");
            maker.appendRaw("This command has been disabled because the bot is running in a Docker container.\nPlease use the Management Bot to preform this action.");
        } else {
            maker.withColor(new Color(175, 30,5));
            maker.getTitle().clear().appendRaw("\uD83D\uDEE0 Bot Updater \uD83D\uDEE0");
            maker.getNote().clear().appendRaw("Last Update");
            maker.withTimestamp(ConfigHandler.getSetting(LastBotUpdaterUseConfig.class, GlobalConfigurable.GLOBAL));
            String out = ExecuteShellCommand.commandToExecute("git pull", ConfigProvider.UPDATE_SETTINGS.updateFolder());
            if (out.contains("Already up-to-date")) {
                maker.appendRaw("The bot is already up to date.\nAborting the update process.");
            } else {
                out = ExecuteShellCommand.commandToExecute("mvn " + ConfigProvider.UPDATE_SETTINGS.mvnArgs(), ConfigProvider.UPDATE_SETTINGS.updateFolder());
                if (out.contains("BUILD FAILURE")) {
                    maker.appendRaw("**The update was unsuccessful. Please view the build results here:**\n" + HasteBin.post(out));
                } else {
                    if (PlatformDetector.isUnix() || PlatformDetector.isWindows()) {
                        ExecuteShellCommand.commandToExecute("cp " + ConfigProvider.UPDATE_SETTINGS.updateFolder() + "target/original-Evelyn.jar " + ConfigProvider.BOT_SETTINGS.botFolder() + "Evelyn.jar", ConfigProvider.BOT_SETTINGS.botFolder());
                        maker.append("The bot has been updated. Please allow 1-2 minutes for changes to take effect.");
                        ConfigHandler.setSetting(LastBotUpdaterUseConfig.class, GlobalConfigurable.GLOBAL, System.currentTimeMillis());

                        MessageMaker maker2 = new MessageMaker(maker);
                        maker2.withColor(new Color(175, 30,5)).withAutoSend(false);
                        maker2.getNote().clear().appendRaw("Update Time");
                        maker2.withTimestamp(System.currentTimeMillis());
                        maker2.withChannel(Channel.getChannel(ConfigProvider.BOT_SETTINGS.loggingChannel()));
                        maker2.getHeader().clear().appendRaw("The Bot has been updated.");
                        maker2.send();

                        ExecuteShellCommand.commandToExecute(ConfigProvider.BOT_SETTINGS.startCommand(), ConfigProvider.BOT_SETTINGS.botFolder());
                    } else if (PlatformDetector.isMac()) {
                        maker.append("I am sorry, I do not know the commands needed to make this work for macOS computers. Please manually update the bot.");
                    }
                }
            }
        }
    }
}