package com.github.nija123098.evelyn.economy.role;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrencySymbolConfig;
import com.github.nija123098.evelyn.economy.configs.CurrentCurrencyConfig;
import com.github.nija123098.evelyn.economy.role.configs.RoleBuyConfig;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.FormatHelper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GetRoleCommand extends AbstractCommand {
    public GetRoleCommand() {
        super("getrole", ModuleLevel.ECONOMY, "buyrole, roleme, role me", null, "Allows the buying of roles with guild based money.");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Role role, User user, GuildUser guildUser, MessageMaker maker) {
        if (role == null) {
            String icon = ConfigHandler.getSetting(CurrencySymbolConfig.class, guildUser.getGuild());
            List<String> list = guildUser.getGuild().getRoles().stream().map(role1 -> {
                Integer price = ConfigHandler.getSetting(RoleBuyConfig.class, role1);
                return price == null ? null : role1.getName() + (price != 0 ? " for " + price + "" + icon : "");
            }).filter(Objects::nonNull).collect(Collectors.toList());
            if (list.isEmpty()) maker.append("There are no roles in this guild to get.");
            else {
                maker.append("You may get these roles:");
                maker.appendRaw(FormatHelper.makeTable(list));
            }
        } else {
            Integer f = ConfigHandler.getSetting(RoleBuyConfig.class, role);
            if (f == null) throw new ArgumentException("You can not buy that role");
            if (f == 0) {
                Integer c = ConfigHandler.getSetting(CurrentCurrencyConfig.class, guildUser);
                if (c < f) throw new ArgumentException("You must have " + f + " currency to buy that role.  Current: " + c);
                ConfigHandler.setSetting(CurrentCurrencyConfig.class, guildUser, c - f);
            }
            user.addRole(role);
        }
    }

    @Override
    public boolean isTemplateCommand() {
        return true;
    }
}
