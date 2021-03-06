package com.github.nija123098.evelyn.fun.slot;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.exception.InsufficientFundsException;
import com.github.nija123098.evelyn.tag.Tag;
import com.github.nija123098.evelyn.tag.Tags;
import com.github.nija123098.evelyn.util.Rand;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Dxeo
 * @since 1.0.0
 */
@Tags(value = {Tag.ARCADE})
public class SlotCommand extends AbstractCommand {// todo optimization and cleanup

    public SlotCommand() {
        super("slot", ModuleLevel.FUN, null, null, "Challenge the RNG and test your fate.");
    }

    //emotes: Diamond, Lollipop, Candy, Cherries, Melon, Lemon, Grapes
    //DO NOT CHANGE THE ORDER WITHOUT CHANGING THE calculateWin() METHOD
    private String[] emotes = {"\uD83D\uDC8E", "\uD83C\uDF6D", "\uD83C\uDF6C", "\uD83C\uDF52", "\uD83C\uDF48", "\uD83C\uDF4B", "\uD83C\uDF47"};

    //win multiplier
    private int winM;

    //minimum jackpot bet
    private int mBet = 20;

    @Command
    public void command(Guild guild, User user, MessageMaker maker, @Argument(info = "bet") Integer bet) throws InterruptedException {

        //save guild money symbol
        String currency_symbol = ConfigHandler.getSetting(CurrencySymbolConfig.class, guild);

        //bet not zero
        if (bet < 1) {
            throw new ArgumentException("You cannot bet less than `\u200B " + currency_symbol + " 1 \u200B` currency.");
        } else if (bet > 200) {
            throw new ArgumentException("You cannot bet more than `\u200b " + currency_symbol + " 200 \u200b` currency.");
        }

        //save guild jackpot
        int guildJackpot = ConfigHandler.getSetting(SlotJackpotConfig.class, guild);

        //subtract bet
        int userBalance = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);
        if (userBalance < bet) {

            //not enough funds
            throw new ArgumentException("You need `\u200B " + currency_symbol + " " + (bet - userBalance) + " \u200B` more to perform this transaction.");
        }
        ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, userBalance - bet);
        userBalance -= bet;

        //configure message maker
        maker.withAutoSend(false).withColor(new Color(54, 57, 62));

        //print the first frame
        maker.appendRaw("```\uD83C\uDFB0 @" + user.getDisplayName(guild) + " \uD83C\uDFB0\n");
        maker.appendRaw("════════════════════════════════════════\n");
        maker.appendRaw(" \uD83C\uDFB2|\uD83C\uDFB2|\uD83C\uDFB2     Bet: " + currency_symbol + " " + bet.toString() + "\n");
        maker.appendRaw(">\uD83C\uDFB2|\uD83C\uDFB2|\uD83C\uDFB2<    Won: " + currency_symbol + " -\n");
        maker.appendRaw(" \uD83C\uDFB2|\uD83C\uDFB2|\uD83C\uDFB2   Funds: " + currency_symbol + " " + userBalance + "\n");
        maker.appendRaw("════════════════════════════════════════\n");
        maker.appendRaw(" Jackpot: " + currency_symbol + " " + guildJackpot + "  MJB: " + currency_symbol + " " + mBet + "```");
        maker.send();

        //check for minimum bet
        if (bet >= mBet) {

            //check for jackpot
            int jackpotRoll = Rand.getRand(guild.getUserSize()*3);
            if (jackpotRoll == 0 && guildJackpot != 0) {

                //refund bet and add jackpot
                ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, userBalance + bet + guildJackpot);

                //reset jackpot
                ConfigHandler.setSetting(SlotJackpotConfig.class, guild, 0);

                //refresh user balance
                userBalance = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);

                //reset the message maker
                maker.getHeader().clear();

                //display jackpot frame with delay
                TimeUnit.SECONDS.sleep(2);
                maker.appendRaw("```\uD83C\uDFB0 @" + user.getDisplayName(guild) + " \uD83C\uDFB0\n");
                maker.appendRaw("════════════════════════════════════════\n\n");
                maker.appendRaw("       Congratulations you won!\n\n");
                maker.appendRaw("         \uD83C\uDF89 " + currency_symbol + " " + String.format("%010d", guildJackpot) + " \uD83C\uDF89\n\n");
                maker.appendRaw("════════════════════════════════════════\n");
                maker.appendRaw(" Funds: " + currency_symbol + " " + userBalance + "  Jackpot: " + currency_symbol + " " + guildJackpot + "```");
                maker.send();
                return;
            }
        }

        //reset the message maker
        maker.getHeader().clear();

        //generate slots
        String[] gSlots = generateSlots();

        //add win
        int win;
        if (winM == -1) {
            win = 0;
            ConfigHandler.setSetting(SlotJackpotConfig.class, guild, ConfigHandler.getSetting(SlotJackpotConfig.class, guild) + bet/2);
        } else {
            win = bet * winM;
            userBalance = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user) + win;
            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, userBalance);
        }

        //refresh guild jackpot
        guildJackpot = ConfigHandler.getSetting(SlotJackpotConfig.class, guild);

        //print the second frame after delay
        TimeUnit.SECONDS.sleep(2);
        maker.appendRaw("```\uD83C\uDFB0 @" + user.getDisplayName(guild) + " \uD83C\uDFB0\n");
        maker.appendRaw("════════════════════════════════════════\n");
        maker.appendRaw(" " + gSlots[0] + "|" + gSlots[1] + "|" + gSlots[2] + "     Bet: " + currency_symbol + " " + bet.toString() + "\n");
        maker.appendRaw(">" + gSlots[3] + "|" + gSlots[4] + "|" + gSlots[5] + "<    Won: " + currency_symbol + " " + win + "\n");
        maker.appendRaw(" " + gSlots[6] + "|" + gSlots[7] + "|" + gSlots[8] + "   Funds: " + currency_symbol + " " + userBalance + "\n");
        maker.appendRaw("════════════════════════════════════════\n");
        maker.appendRaw(" Jackpot: " + currency_symbol + " " + guildJackpot  + "  MJB: " + currency_symbol + " " + mBet + "```");

        //add reaction for repeating the command
        maker.withReactionBehavior("slot_machine", ((add, reaction, u) -> {

            //save guild jackpot
            int r_guildJackpot = ConfigHandler.getSetting(SlotJackpotConfig.class, guild);

            //subtract bet
            int r_userBalance = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);
            if (r_userBalance < bet) throw new InsufficientFundsException(bet, r_userBalance, currency_symbol);
            ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, r_userBalance - bet);
            r_userBalance -= bet;

            //configure message maker
            maker.withAutoSend(false);
            maker.getHeader().clear();

            //print the first frame
            maker.appendRaw("```\uD83C\uDFB0 @" + user.getDisplayName(guild) + " \uD83C\uDFB0\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(" \uD83C\uDFB2|\uD83C\uDFB2|\uD83C\uDFB2     Bet: " + currency_symbol + " " + bet.toString() + "\n");
            maker.appendRaw(">\uD83C\uDFB2|\uD83C\uDFB2|\uD83C\uDFB2<    Won: " + currency_symbol + " -\n");
            maker.appendRaw(" \uD83C\uDFB2|\uD83C\uDFB2|\uD83C\uDFB2   Funds: " + currency_symbol + " " + r_userBalance + "\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(" Jackpot: " + currency_symbol + " " + r_guildJackpot + "  MJB: " + currency_symbol + " " + mBet + "```");
            maker.send();

            //check for minimum bet
            if (bet >= mBet) {

                //check for jackpot
                int jackpotRoll = Rand.getRand(guild.getUserSize()*3);
                if (jackpotRoll == 0 && r_guildJackpot != 0) {

                    //refund bet and add jackpot
                    ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, r_userBalance + bet + r_guildJackpot);

                    //reset jackpot
                    ConfigHandler.setSetting(SlotJackpotConfig.class, guild, 0);

                    //refresh user balance
                    r_userBalance = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user);

                    //reset the message maker
                    maker.getHeader().clear();

                    //display jackpot frame with delay
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    maker.appendRaw("```\uD83C\uDFB0 @" + user.getDisplayName(guild) + " \uD83C\uDFB0\n");
                    maker.appendRaw("════════════════════════════════════════\n\n");
                    maker.appendRaw("       Congratulations you won!\n\n");
                    maker.appendRaw("         \uD83C\uDF89 " + currency_symbol + " " + String.format("%010d", r_guildJackpot) + " \uD83C\uDF89\n\n");
                    maker.appendRaw("════════════════════════════════════════\n");
                    maker.appendRaw(" Funds: " + currency_symbol + " " + r_userBalance + "  Jackpot: " + currency_symbol + " " + r_guildJackpot + "```");
                    maker.send();
                    return;
                }
            }

            //reset the message maker
            maker.getHeader().clear();

            //generate slots
            String[] r_gSlots = generateSlots();

            //add win
            int r_win;
            if (winM == -1) {
                r_win = 0;
                ConfigHandler.setSetting(SlotJackpotConfig.class, guild, ConfigHandler.getSetting(SlotJackpotConfig.class, guild) + bet/2);
            } else {
                r_win = bet * winM;
                r_userBalance = ConfigHandler.getSetting(CurrentCurrencyConfig.class, user) + r_win;
                ConfigHandler.setSetting(CurrentCurrencyConfig.class, user, r_userBalance);
            }

            //refresh guild jackpot
            r_guildJackpot = ConfigHandler.getSetting(SlotJackpotConfig.class, guild);

            //print the second frame after delay
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            maker.appendRaw("```\uD83C\uDFB0 @" + user.getDisplayName(guild) + " \uD83C\uDFB0\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(" " + r_gSlots[0] + "|" + r_gSlots[1] + "|" + r_gSlots[2] + "     Bet: " + currency_symbol + " " + bet.toString() + "\n");
            maker.appendRaw(">" + r_gSlots[3] + "|" + r_gSlots[4] + "|" + r_gSlots[5] + "<    Won: " + currency_symbol + " " + r_win + "\n");
            maker.appendRaw(" " + r_gSlots[6] + "|" + r_gSlots[7] + "|" + r_gSlots[8] + "   Funds: " + currency_symbol + " " + r_userBalance + "\n");
            maker.appendRaw("════════════════════════════════════════\n");
            maker.appendRaw(" Jackpot: " + currency_symbol + " " + r_guildJackpot  + "  MJB: " + currency_symbol + " " + mBet + "```");
            maker.send();

        }));
        maker.send();


    }

    @Override
    public String getHelp() {

        //command description:
        return
        "#  Jackpot\n// Per server. Every time you lose, a portion of your bet goes to the jackpot\n" +
        "#  MJB\n// Minimum Jackpot Bet. The minimum amount you have to bet to roll for the jackpot";
    }

    private String[] generateSlots() {

        //create slots empty structure
        int[] slots = new int[9];

        //save emotes length
        int emoteLength = emotes.length -1;

        //initialize middle row
        slots[3] = Rand.getRand(emoteLength);
        slots[4] = Rand.getRand(emoteLength);
        slots[5] = Rand.getRand(emoteLength);

        //calculate win
        winM = calculateWin(slots[3], slots[4], slots[5]);

        //initialize first row
        if (slots[3] == 0) {slots[0] = emoteLength;} else {slots[0] = slots[3] - 1;}
        if (slots[4] == 0) {slots[1] = emoteLength;} else {slots[1] = slots[4] - 1;}
        if (slots[5] == 0) {slots[2] = emoteLength;} else {slots[2] = slots[5] - 1;}

        //initialize third row
        if (slots[3] == emoteLength) {slots[6] = 0;} else {slots[6] = slots[3] + 1;}
        if (slots[4] == emoteLength) {slots[7] = 0;} else {slots[7] = slots[4] + 1;}
        if (slots[5] == emoteLength) {slots[8] = 0;} else {slots[8] = slots[5] + 1;}

        //translate to emotes
        String[] translatedSlots = new String[9];
        for (int i = 0; i < 9; i++) {
            translatedSlots[i] = emotes[slots[i]];
        }

        //return translated slots
        return translatedSlots;
    }

    private int calculateWin(int first, int second, int third) {

        //make the calculating array
        int[] array = {first, second, third};

        //calculate number of element 0 in emote array ("win condition")
        int count = 0;
        for (int i = 0; i < 3; i++) {
            if (array[i] == 0) count++;
        }
        if (count > 0) {
            switch (count) {

                //if -1 return no win
                case 1:
                    return -1;

                //if 2 set winM to 2
                case 2:
                    return 2;

                //if 3 set winM to 10
                case 3:
                    return 10;
            }
        }

        if (array[0] == array[1] && array[0] == array[2]) {
            switch (array[0]) {

                //return based on emotes array order, use this to adjust win multiplier
                case 1:
                    return 7;
                case 2:
                    return 7;
                default:
                    return 5;
            }
        }
        return -1;
    }
}