import commands.*;
import events.CoinsSystem;
import events.GameSystem;
import events.QuestionsSystem;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import system.MySqlConnector;
import utils.TerminalColors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.security.auth.login.LoginException;
import java.util.List;

public class Bot {

    private static JDABuilder builder;
    public  static  JDA bot;
    private static final org.apache.log4j.Logger log = Logger.getLogger(Bot.class);

    public Bot(ListenerAdapter[] listeners){
        PropertyConfigurator.configure("log4j.properties");
        builder = JDABuilder.createDefault(null);
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);
        builder.setActivity(Activity.watching("Mindera School"));
        builder.addEventListeners(listeners);
    }

    public void setToken(String token){
        log.warn(TerminalColors.YELLOW+"[Login] The token has been set"+TerminalColors.RESET);
        builder.setToken(token);
    }

    public void start() {
        try {
            builder.build();
            log.info(TerminalColors.GREEN+"[Login] The bot has been started."+TerminalColors.RESET);
        } catch (LoginException e) {
            log.fatal(TerminalColors.RED+"[Login] Invalid Token"+TerminalColors.RESET);
        }
    }
}
