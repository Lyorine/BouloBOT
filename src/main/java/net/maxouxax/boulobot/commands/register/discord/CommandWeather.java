package net.maxouxax.boulobot.commands.register.discord;

import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.maxouxax.boulobot.BOT;
import net.maxouxax.boulobot.commands.Command;
import net.maxouxax.boulobot.commands.CommandMap;
import net.maxouxax.boulobot.util.TextFormatter;

public class CommandWeather {

    private final BOT bot;
    private final CommandMap commandMap;

    public CommandWeather(CommandMap commandMap) {
        this.commandMap = commandMap;
        this.bot = BOT.getInstance();
    }

    @Command(name = "météo", type = Command.ExecutorType.USER, help = ".météo <ville>", example = ".météo Paris", description = "Permet d'obtenir la météo sur la ville spécifiée")
    private void météo(User user, TextChannel textChannel, Guild guild, String[] args) {
        try {
            EmbedBuilder em = commandMap.getHelpEmbed("météo");
            if (args.length == 0) {
                textChannel.sendMessage(em.build()).queue();
            } else {

                textChannel.sendTyping().queue();
                StringBuilder str = new StringBuilder();

                for (String arg : args) {
                    str.append(arg).append(" ");
                }

                String city = str.toString();
                OWM owm = new OWM(bot.getConfigurationManager().getStringValue("owmApiKey"));
                owm.setLanguage(OWM.Language.FRENCH);
                owm.setUnit(OWM.Unit.METRIC);
                CurrentWeather cwd = owm.currentWeatherByCityName(city);

                EmbedBuilder weatherEmbed = new EmbedBuilder();

                weatherEmbed.setTitle("Météo pour " + cwd.getCityName(), "https://openweathermap.org");
                weatherEmbed.setDescription("Voici la météo actuelle pour la ville: " + cwd.getCityName());
                weatherEmbed.setFooter(TextFormatter.asDate(bot.getConfigurationManager().getStringValue("embedFooter")), bot.getConfigurationManager().getStringValue("embedIconUrl"));
                weatherEmbed.setColor(3066993);


                if (cwd.getMainData() != null && cwd.getMainData().hasTemp()) {
                    weatherEmbed.addField("Température", cwd.getMainData().getTemp() + "°C", true);
                } else {
                    weatherEmbed.addField("Température", "Aucune donnée", true);
                }
                weatherEmbed.addField("Température min", cwd.getMainData().getTempMin() + "°C", true);
                weatherEmbed.addField("Température max", cwd.getMainData().getTempMax() + "°C", true);
                if (cwd.getWindData() != null && cwd.getWindData().hasSpeed()) {
                    weatherEmbed.addField("Vent", cwd.getWindData().getSpeed() + " m/s", true);
                } else {
                    weatherEmbed.addField("Vent", "Aucune donnée", true);
                }
                textChannel.sendMessage(weatherEmbed.build()).queue();
            }
        } catch (Exception e) {
            EmbedBuilder errorEmbed = new EmbedBuilder();

            errorEmbed.setTitle("Une erreur est survenue", "https://openweathermap.org");
            errorEmbed.addField("Message d'erreur", "» "+e.getMessage(), true);
            errorEmbed.setColor(15158332);
            errorEmbed.setFooter(TextFormatter.asDate(bot.getConfigurationManager().getStringValue("embedFooter")), bot.getConfigurationManager().getStringValue("embedIconUrl"));
            textChannel.sendMessage(errorEmbed.build()).queue();
            bot.getErrorHandler().handleException(e);
        }

    }

}
