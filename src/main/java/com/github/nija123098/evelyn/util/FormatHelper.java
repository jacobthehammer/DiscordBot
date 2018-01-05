package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static com.github.nija123098.evelyn.botconfiguration.ConfigProvider.URLS;
import static com.github.nija123098.evelyn.util.EmoticonHelper.getChars;
import static java.lang.Math.max;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.fill;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class FormatHelper {
    public static String repeat(char c, int i) {
        char[] chars = new char[i];
        fill(chars, c);
        return new String(chars);
    }

    public static String reduceRepeats(String s, char c) {// use index of to optimize
        final StringBuilder builder = new StringBuilder();
        boolean repeat = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                if (!repeat) {
                    builder.append(c);
                }
                repeat = true;
            } else {
                repeat = false;
                builder.append(s.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String removeChars(String s, char toRemove) {
        return filtering(s, c -> c != toRemove);
    }

    public static String trimFront(String s) {
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) != ' ') return s.substring(i);
        return "";
    }

    public static String makePleural(String s) {
        return s + "'" + (s.endsWith("s") ? s + "" : "s");
    }

    public static int lengthOf(String[] args, int count) {
        int l = 0;
        for (int i = 0; i < count; i++) {
            l += args[i].length();
        }
        return l;
    }

    public static String embedLink(String text, String link) {
        if (link.isEmpty()) return "[" + text + "](" + URLS.rickrollVid() + ")";
        return "[" + text + "](" + link + ")";
    }

    /**
     * @param headers array containing the headers
     * @param table   array[n size] of array's[header size], containing the rows of the controllers
     * @param footer
     * @return a formatted controllers
     */
    public static String makeAsciiTable(List<String> headers, List<List<String>> table, List<String> footer) {
        StringBuilder sb = new StringBuilder();
        int padding = 1;
        int[] widths = new int[headers.size()];
        fill(widths, 0);
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).length() > widths[i]) {
                widths[i] = headers.get(i).length();
                if (footer != null) {
                    widths[i] = max(widths[i], footer.get(i).length());
                }
            }
        }
        for (List<String> row : table) {
            for (int i = 0; i < row.size(); i++) {
                String cell = row.get(i);
                if (cell.length() > widths[i]) {
                    widths[i] = cell.length();
                }
            }
        }
        sb.append("```").append("\n");
        String formatLine = "|";
        for (int width : widths) {
            formatLine += " %-" + width + "s |";
        }
        formatLine += "\n";
        sb.append(appendSeparatorLine("+", "+", "+", padding, widths));
        sb.append(format(formatLine, headers.toArray()));
        sb.append(appendSeparatorLine("+", "+", "+", padding, widths));
        for (List<String> row : table) {
            sb.append(format(formatLine, row.toArray()));
        }
        if (footer != null) {
            sb.append(appendSeparatorLine("+", "+", "+", padding, widths));
            sb.append(format(formatLine, footer.toArray()));
        }
        sb.append(appendSeparatorLine("+", "+", "+", padding, widths));
        sb.append("```");
        return sb.toString();
    }

    /**
     * helper function for makeAsciiTable
     *
     * @param left    character on the left
     * @param middle  character in the middle
     * @param right   character on the right
     * @param padding controllers cell padding
     * @param sizes   width of each cell
     * @return a filler row for the controllers
     */
    private static String appendSeparatorLine(String left, String middle, String right, int padding, int... sizes) {
        boolean first = true;
        StringBuilder ret = new StringBuilder();
        for (int size : sizes) {
            if (first) {
                first = false;
                ret.append(left).append(Strings.repeat("-", size + padding * 2));
            } else {
                ret.append(middle).append(Strings.repeat("-", size + padding * 2));
            }
        }
        return ret.append(right).append("\n").toString();
    }

    /**
     * @param items items in the controllers
     * @return formatted controllers
     */
    public static String makeTable(List<String> items) {
        return makeTable(items, 16, 4);
    }

    /**
     * @param items items in the controllers
     * @return formatted controllers
     */
    public static String makeTable(List<String> items, boolean codeBlock) {
        return makeTable(items, 20, 3, codeBlock);
    }

    /**
     * Makes a controllers-like display of list of items
     *
     * @param items        items in the controllers
     * @param columnLength length of a column(filled up with whitespace)
     * @param columns      amount of columns
     * @return formatted controllers
     */
    public static String makeTable(List<String> items, int columnLength, int columns) {
        StringBuilder ret = new StringBuilder("```\n");
        int counter = 0;
        for (String item : items) {
            counter++;
            ret.append(format("%-" + columnLength + "s", item));
            if (counter % columns == 0) {
                ret.append("\n");
            }
        }
        if (counter % columns != 0) {
            ret.append("\n");
        }
        return ret + "```\n";
    }

    /**
     * Makes a formatted table of user permissions
     *
     * @param user        User
     * @param guild      Guild
     * @param simple     simple
     * @return formatted table
     */
    public static String makeUserPermissionsTable(User user, Guild guild, boolean simple) {
        List<String> permissions = new ArrayList();
        user.getPermissionsForGuild(guild).forEach(permission -> {
            if (permission.name().contains("MANAGE")) {
                permissions.add("'" + permission.name().toLowerCase() + "'");
            } else if (permission.name().contains("KICK") || permission.name().contains("BAN") || permission.name().contains("MOVE") || permission.name().contains("DEAFEN") || permission.name().contains("MUTE") || permission.name().contains("LOG")) {
                permissions.add('"' + permission.name().toLowerCase() + '"');
            } else if (permission.name().contains("ADMINISTRATOR")){
                permissions.add(" Administrator ");
            } else if (!simple) {
                permissions.add(" " + permission.name().toLowerCase() + " ");
            }
        });
        Collections.sort(permissions);

        StringBuilder ret = new StringBuilder("```ml\n");
        int counter = 0;
        for (String item : permissions) {
            counter++;
            ret.append(format("%-" + 23 + "s", item));
            if (counter % 2 == 0) {
                ret.append("\n");
            }
        }
        if (counter % 2 != 0) {
            ret.append("\n");
        }
        return ret + "```\n";
    }

    /**
     * Makes a formatted table of role permissions
     *
     * @param role        Role
     * @return formatted table
     */
    public static String makeRolePermissionsTable(Role role) {
        List<String> permissions = new ArrayList();
        role.getPermissions().forEach(permission -> {
            if (permission.name().contains("MANAGE") || permission.name().contains("LOG")) {
                permissions.add("'" + permission.name().toLowerCase() + "'");
            } else if (permission.name().contains("KICK") || permission.name().contains("BAN") || permission.name().contains("MOVE") || permission.name().contains("DEAFEN") || permission.name().contains("MUTE")) {
                permissions.add('"' + permission.name().toLowerCase() + '"');
            } else if (permission.name().contains("ADMINISTRATOR")){
                permissions.add(" Administrator ");
            } else {
                permissions.add(" " + permission.name().toLowerCase() + " ");
            }
        });
        Collections.sort(permissions);

        StringBuilder ret = new StringBuilder("```ml\n");
        int counter = 0;
        for (String item : permissions) {
            counter++;
            ret.append(format("%-" + 23 + "s", item));
            if (counter % 2 == 0) {
                ret.append("\n");
            }
        }
        if (counter % 2 != 0) {
            ret.append("\n");
        }
        return ret + "```\n";
    }

    /**
     * Makes a controllers-like display of list of items
     *
     * @param items        items in the controllers
     * @param columnLength length of a column(filled up with whitespace)
     * @param columns      amount of columns
     * @return formatted controllers
     */
    public static String makeUserTable(List<User> items, int columnLength, int columns) {
        StringBuilder ret = new StringBuilder("```\n");
        int counter = 0;
        for (User item : items) {
            counter++;
            ret.append(format("%-" + columnLength + "s", item.getName()));
            if (counter % columns == 0) {
                ret.append("\n");
            }
        }
        if (counter % columns != 0) {
            ret.append("\n");
        }
        return ret + "```\n";
    }


    /**
     * Makes a controllers-like display of list of items
     *
     * @param items        items in the controllers
     * @param columnLength length of a column(filled up with whitespace)
     * @param columns      amount of columns
     * @param codeBlock    whether to make it a code block
     * @return formatted controllers
     */
    public static String makeTable(List<String> items, int columnLength, int columns, boolean codeBlock) {
        StringBuilder ret = new StringBuilder();
        if (codeBlock) ret.insert(0, "```");
        int counter = 0;
        for (String item : items) {
            counter++;
            ret.append(format(("%-" + columnLength + "s"), item));
            if (counter % columns == 0) {
                ret.append("\n");
            }
        }
        if (counter % columns != 0) {
            ret.append("\n");
        }
        if (codeBlock) return ret + "```";
        return ret.toString();
    }

    private static final String DASH = getChars("wavy_dash", true);

    public static String makeStackedBar(int max, int bar, String barChar) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bar; i++) {
            sb.append(barChar);
        }
        for (int i = bar; i < max; i++) {
            sb.append(DASH);
        }
        return sb.toString();
    }

    public static String getList(List<String> strings) {
        switch (strings.size()) {
            case 0:
                throw new DevelopmentException("List provided is empty");
            case 1:
                return strings.get(0);
            case 2:
                return strings.get(0) + " and " + strings.get(1);
            default:
                String builder = "";
                for (int i = 0; i < strings.size() - 1; i++) {
                    builder += strings.get(i) + ", ";
                }
                return builder + "and " + strings.get(strings.size() - 1);
        }
    }

    public static String cleanOfXML(String s) {
        StringBuilder builder = new StringBuilder();
        AtomicBoolean in = new AtomicBoolean();
        new StringIterator(s).forEachRemaining(character -> {
            if (character == '<') {
                in.set(true);
                return;
            }
            if (character == '>') {
                in.set(false);
                return;
            }
            if (!in.get()) builder.append(character);
        });
        return builder.toString();
    }

    public static List<String> cleanOfXML(List<String> strings) {
        return strings.stream().map(FormatHelper::cleanOfXML).collect(toList());
    }

    public static String filtering(String s, Function<Character, Boolean> filter) {
        StringBuilder builder = new StringBuilder();
        new StringIterator(s).forEachRemaining(character -> {
            if (filter.apply(character)) builder.append(character);
        });
        return builder.toString();
    }

    public static String reformat(String s, Function<Character, Character> function) {
        StringBuilder builder = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            Character character = function.apply(s.charAt(i));
            if (character != null) builder.append(character);
        }
        return builder.toString();
    }

    public static String reduce(String s) {
        return filtering(s, Character::isLetter);
    }

    public static Set<String> reduce(Set<String> strings) {
        return strings.stream().map(FormatHelper::reduce).collect(toSet());
    }

    public static String addComas(double l) {
        String str = valueOf(l);
        if (str.endsWith(".0")) str = str.substring(0, str.length() - 2);
        int eIndex = str.indexOf("E");
        if (eIndex != -1) return str.substring(0, 4) + str.substring(eIndex);
        if (l < 9999) return str;
        StringBuilder builder = new StringBuilder();
        int bound = l % 1 == 0 ? str.length() : str.indexOf(".");
        for (int i = bound - 1; i > -1; --i) {
            builder.append(str.charAt(bound - i - 1));
            if (i % 3 == bound % 3 && i != 0) builder.append(",");
        }
        if (bound != str.length()) {
            for (int i = bound; i < str.length(); i++) {
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String addComas(String bigInteger) {
        if (bigInteger.length() < 5) return bigInteger;
        int decimal = bigInteger.indexOf(".");
        if (decimal != -1) bigInteger = bigInteger.substring(0, decimal);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bigInteger.length(); i++) {
            if (i % 3 == bigInteger.length() % 3 && i != 0) builder.append(",");
            builder.append(bigInteger.charAt(i));
        }
        return builder.toString();
    }

    public static String limitLength(String s,int length){
        return s.length() > length ? s.substring(0, length) : s;
    }
}