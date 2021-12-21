package events;


import entities.Coins;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import repository.Repository;
import system.MySqlConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class CoinsSystem extends ListenerAdapter {

    private final String prefix = "!";
    private Repository repository;

    public CoinsSystem(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        if(checkUser(event.getMember())){
            int length = event.getMessage().getContentRaw().length();
            if(length > 0){
                setMoney(event.getMember(),getMoney(event.getMember(), event.getGuild())+length,event.getGuild());
            }
        }
    }

    public int getMoney(Member member,Guild guild){
        Coins coins = repository.getUserCoins(member, guild);
        return coins.getCoins();
    }

    public void setMoney(Member member, int money,Guild guild){
        repository.setUserCoins(member,guild,money);
    }

    public boolean checkUser(Member member){
        return !member.getUser().isBot();
    }

}
