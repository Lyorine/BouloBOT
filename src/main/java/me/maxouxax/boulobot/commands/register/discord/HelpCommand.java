package me.maxouxax.boulobot.commands.register.discord;

import me.maxouxax.boulobot.commands.Command;
import me.maxouxax.boulobot.commands.Command.ExecutorType;
import me.maxouxax.boulobot.commands.CommandMap;
import me.maxouxax.boulobot.commands.SimpleCommand;
import me.maxouxax.boulobot.util.EmbedCrafter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.entities.UserImpl;

public class HelpCommand {

    private final CommandMap commandMap;

    public HelpCommand(CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    @Command(name="help",type=ExecutorType.USER,description="Affiche l'entièreté des commandes disponibles", help = ".help", example = ".help")
    private void help(User user, MessageChannel channel, Guild guild){
        EmbedCrafter embedCrafter = new EmbedCrafter();
        embedCrafter.setTitle("Aide » Liste des commandes")
            .setColor(3447003);

        for(SimpleCommand command : commandMap.getDiscordCommands()){
            if(command.getExecutorType() == ExecutorType.CONSOLE) continue;

            if(guild != null && command.getPower() > commandMap.getPowerUser(guild, user)) continue;

            embedCrafter.addField(command.getName(), command.getDescription(), true);
        }

        if(!user.hasPrivateChannel()) user.openPrivateChannel().complete();
        ((UserImpl)user).getPrivateChannel().sendMessage(embedCrafter.build()).queue();

        channel.sendMessage(user.getAsMention()+", veuillez regarder vos message privés.").queue();

    }

}