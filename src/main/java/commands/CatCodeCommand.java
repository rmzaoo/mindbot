package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CatCodeCommand extends ListenerAdapter {

    private final String prefix = "!";

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if(msg.getAuthor().isBot()){
            return;
        }
        if(msg.getContentRaw().matches(prefix+"catcode.*")){
            MessageChannel channel = event.getChannel();
            String[] splitMessage = event.getMessage().getContentRaw().split("[\\s&&[^\\n]]++");
            try{
                if(splitMessage[1].matches("-?\\d+(\\.\\d+)?")){
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("HTTP Cat Code", null);
                    eb.setColor(new Color(180, 70, 0));
                    eb.addBlankField(false);
                    eb.setFooter("MindBot | "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), event.getJDA().getSelfUser().getAvatarUrl());
                    eb.setImage("https://http.cat/"+splitMessage[1]);
                    channel.sendMessage(eb.build()).queue();

                }else{
                    channel.sendMessage("!catcode (httpcode)").queue();
                }
            }catch (IndexOutOfBoundsException ex){
                channel.sendMessage("!catcode (httpcode)").queue();
            }
        }
    }
}
