package com.github.nija123098.evelyn.economy.bank;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencyNameConfig;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.exception.ArgumentException;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class BankSendCommand extends AbstractCommand {
    public BankSendCommand() {
        super(BankCommand.class, "send", null, null, null, "Sends currency to another user, guild, or guild user");
    }
    @Command
    public void command(@Argument User receiver, @Argument Integer currencyTransfer, User user, @Context(softFail = true) Guild guild, MessageMaker maker) {
        String name = ConfigHandler.getSetting(CurrencyNameConfig.class, guild), symbol = ConfigHandler.getSetting(CurrencySymbolConfig.class, guild);
        int senderCurrency = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);
        int receiverCurrency = ConfigHandler.getSetting(CurrentCurrencyConfig.class, receiver);
        if (currencyTransfer < 0) throw new ArgumentException("You can't take " + name + " from other users without their consent.");
        if (currencyTransfer == 0) throw new ArgumentException("You can't send them nothing.");
        if (senderCurrency < currencyTransfer) throw new ArgumentException("You need `\u200b " + symbol + " " + (currencyTransfer - senderCurrency) + " \u200b` more to perform this transaction.");
        if (receiver.equals(user)) throw new ArgumentException("You can't send yourself " + name + ", silly.");
        ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, (senderCurrency - currencyTransfer));
        ConfigHandler.setSetting(CurrentCurrencyConfig.class, receiver, (receiverCurrency + currencyTransfer));
        maker.appendEmbedLink(user.getDisplayName(guild), "").append(" successfully sent `\u200b " + symbol + " " + currencyTransfer + " \u200b`  to ").appendEmbedLink(receiver.getDisplayName(guild), "");
    }
}
