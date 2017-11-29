package com.github.nija123098.evelyn.botConfiguration.configInterfaces.BotConfig;

/**
 * @Author: Celestialdeath99
 * Made on 11/28/2017
 */


public interface BotSettings {
    String bot_token();
    Boolean test_mode_enabled();
    Integer number_of_shards();
    Integer evelyn_shard_number();
    String support_server_id();
    Boolean ghost_mode_enabled();
    Integer message_filtering_server_size();
    Boolean typing_enabled();
    Boolean voice_commands_enabled();
    String contributor_sign_role();
    String supporter_sign_role();
}
