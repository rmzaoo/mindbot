import commands.*;
import events.CoinsSystem;
import events.GameSystem;
import events.QuestionsSystem;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import repository.Repository;
import system.MySqlConnector;

import javax.security.auth.login.LoginException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws LoginException {
        MySqlConnector mySqlConnector = new MySqlConnector();
        Repository repository = new Repository(mySqlConnector);

        CoinsSystem coinsSystem = new CoinsSystem(repository);
        QuestionsSystem questionsSystem = new QuestionsSystem(repository);
        GameSystem gameSystem = new GameSystem(questionsSystem, coinsSystem);
        ListenerAdapter[] listeners = {
                new PingCommand(),
                new UserInfoCommand(),
                new CatCodeCommand(),
                coinsSystem,
                new CoinCommand(coinsSystem),
                new StartGameCommand(gameSystem),
                gameSystem,
                questionsSystem};


        Bot bot = new Bot(listeners);
        bot.setToken("token");
        bot.start();


        mySqlConnector.startConn();
        mySqlConnector.closeConn();
    }
}
