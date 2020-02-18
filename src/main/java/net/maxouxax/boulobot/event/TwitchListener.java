package net.maxouxax.boulobot.event;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.UserBanEvent;
import com.github.twitch4j.chat.events.channel.UserTimeoutEvent;
import com.github.twitch4j.common.events.channel.ChannelChangeGameEvent;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.helix.domain.User;
import net.maxouxax.boulobot.BOT;
import net.maxouxax.boulobot.commands.CommandMap;
import net.maxouxax.boulobot.commands.TwitchCommand;

import java.util.Collections;

public class TwitchListener {

    private final CommandMap commandMap;
    private final BOT botDiscord;

    public TwitchListener(CommandMap commandMap, BOT botDiscord){
        this.commandMap = commandMap;
        this.botDiscord = botDiscord;

        botDiscord.getTwitchClient().getEventManager().onEvent(ChannelMessageEvent.class).subscribe(this::onMessageEvent);
        botDiscord.getTwitchClient().getEventManager().onEvent(ChannelChangeGameEvent.class).subscribe(this::onGameUpdate);
        botDiscord.getTwitchClient().getEventManager().onEvent(UserTimeoutEvent.class).subscribe(this::onTimeOut);
        botDiscord.getTwitchClient().getEventManager().onEvent(UserBanEvent.class).subscribe(this::onBan);
    }

    private void onBan(UserBanEvent userBanEvent) {
    }

    private void onTimeOut(UserTimeoutEvent userTimeoutEvent) {
    }

    private void onGameUpdate(ChannelChangeGameEvent channelChangeGameEvent) {
        botDiscord.getSessionManager().updateGame(channelChangeGameEvent.getGameId());
    }

    private void onMessageEvent(ChannelMessageEvent channelMessageEvent) {
        String message = channelMessageEvent.getMessage();
        if (message.startsWith(commandMap.getTwitchTag())) {
            EventUser user = channelMessageEvent.getUser();
            User userUser = botDiscord.getTwitchClient().getHelix().getUsers(null, Collections.singletonList(user.getId()), null).execute().getUsers().get(0);
            String broadcaster = channelMessageEvent.getChannel().getName();
            TwitchCommand.ExecutorRank executorRank = commandMap.getRank(channelMessageEvent.getPermissions());
            message = message.replaceFirst(commandMap.getTwitchTag(), "");
            commandMap.twitchCommandUser(userUser, broadcaster, executorRank, message, channelMessageEvent.getPermissions());
        }
    }


}
