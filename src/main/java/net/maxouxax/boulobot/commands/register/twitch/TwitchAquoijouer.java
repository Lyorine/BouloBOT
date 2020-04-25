package net.maxouxax.boulobot.commands.register.twitch;

import com.github.twitch4j.helix.domain.GameTopList;
import com.github.twitch4j.helix.domain.User;
import net.maxouxax.boulobot.BOT;
import net.maxouxax.boulobot.commands.CommandMap;
import net.maxouxax.boulobot.commands.TwitchCommand;


public class TwitchAquoijouer {

    private final BOT bot;
    private final CommandMap commandMap;

    public TwitchAquoijouer(BOT bot, CommandMap commandMap){
        this.bot = bot;
        this.commandMap = commandMap;
    }

    @TwitchCommand(name = "aquoijouer", example = "&aquoijouer", help = "&aquoijouer", description = "Récupérer les jeux les plus regardés en ce moment", rank = TwitchCommand.ExecutorRank.EVERYONE)
    private void aquoijouer(User user, String broadcaster, String[] args){
        StringBuilder str = new StringBuilder();
        GameTopList resultList = bot.getTwitchClient().getHelix().getTopGames(null, null, null, "10").execute();
        resultList.getGames().forEach(game -> {
            str.append(game.getName()).append(", ");
        });
        bot.getTwitchClient().getChat().sendMessage(broadcaster, "Voici quelques idées de jeux: ");
        bot.getTwitchClient().getChat().sendMessage(broadcaster, str.toString());
    }

}
