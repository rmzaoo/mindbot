package commands;

import events.CoinsSystem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoinCommand extends ListenerAdapter {
    private final String prefix = "!";
    private CoinsSystem coinsSystem;

    public CoinCommand(CoinsSystem coinsSystem) {
        this.coinsSystem = coinsSystem;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if(msg.getAuthor().isBot()){
            return;
        }
        if(msg.getContentRaw().matches(prefix+"coins.*")){
            MessageChannel channel = event.getChannel();
            EmbedBuilder eb = new EmbedBuilder();
            Member member;
            try{
                member = event.getMessage().getMentionedMembers().get(0);

            }catch (IndexOutOfBoundsException ex){
                member = event.getMember();
            }

            eb.setTitle(member.getEffectiveName() + " Coins", null);
            eb.setColor(new Color(180, 70, 0));
            eb.setDescription(coinsSystem.getMoney(member, msg.getGuild())+" Coins");
            eb.addBlankField(false);
            eb.setFooter("MindBot | "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), event.getJDA().getSelfUser().getAvatarUrl());
            eb.setThumbnail(member.getEffectiveAvatarUrl());

            channel.sendMessage(eb.build()).queue();
        }

    }
}
