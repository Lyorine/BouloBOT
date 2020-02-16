package net.maxouxax.boulobot.commands.register.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.entities.UserImpl;
import net.maxouxax.boulobot.commands.Command;
import net.maxouxax.boulobot.commands.Command.ExecutorType;
import net.maxouxax.boulobot.commands.CommandMap;
import net.maxouxax.boulobot.commands.SimpleCommand;

public class HelpCommand {

    private final CommandMap commandMap;

    public HelpCommand(CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    @Command(name="help",type=ExecutorType.USER,description="Affiche l'entièreté des commandes disponibles", help = ".help", example = ".help")
    private void help(User user, MessageChannel channel, Guild guild){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Aide » Liste des commandes");
        builder.setColor(3447003);

        for(SimpleCommand command : commandMap.getDiscordCommands()){
            if(command.getExecutorType() == ExecutorType.CONSOLE) continue;

            if(guild != null && command.getPower() > commandMap.getPowerUser(guild, user)) continue;

            builder.addField(command.getName(), command.getDescription(), true);
        }

        if(!user.hasPrivateChannel()) user.openPrivateChannel().complete();
        ((UserImpl)user).getPrivateChannel().sendMessage(builder.build()).queue();

        channel.sendMessage(user.getAsMention()+", veuillez regarder vos message privés.").queue();

    }

}