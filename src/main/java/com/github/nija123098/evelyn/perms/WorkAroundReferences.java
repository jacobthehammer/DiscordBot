package com.github.nija123098.evelyn.perms;

import java.util.concurrent.atomic.AtomicReference;

import static com.github.nija123098.evelyn.perms.BotRole.*;

/**
 * @author nija123098
 * @since 1.0.0
 */
class WorkAroundReferences {
    static final AtomicReference<BotRole> B_A = new AtomicReference<>();
    static final AtomicReference<BotRole> B_O = new AtomicReference<>();
    static final AtomicReference<BotRole> G_A = new AtomicReference<>();
    static void set() {
        B_A.set(BOT_ADMIN);
        B_O.set(BOT_OWNER);
        G_A.set(GUILD_ADMIN);
    }
}
