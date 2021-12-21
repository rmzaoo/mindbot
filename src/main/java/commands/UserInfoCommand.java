package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserInfoCommand extends ListenerAdapter {
    private final String prefix = "!";

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if(msg.getAuthor().isBot()){
            return;
        }

        if(msg.getContentRaw().matches(prefix+"userinfo.*")){
            MessageChannel channel = event.getChannel();
            EmbedBuilder eb = new EmbedBuilder();
            Member member;

            try{
                member = event.getMessage().getMentionedMembers().get(0);

            }catch (IndexOutOfBoundsException ex){
                member = event.getMessage().getMember();

            }

            eb.setTitle(member.getEffectiveName()+"#"+member.getUser().getDiscriminator(), null);
            eb.setColor(new Color(180, 70, 0));
            eb.setDescription("Create account at "+member.getTimeCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            eb.addField("Profile Photo", "[Link]("+member.getEffectiveAvatarUrl()+")", false);
            eb.addField("Discord Dev Id", "```"+member.getId()+"```", false);
            eb.addField("Roles", "```"+String.valueOf(member.getRoles())+"```", false);
            eb.addField("Time Joined Server", member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), false);

            eb.addBlankField(false);
            eb.setFooter("MindBot | "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), event.getJDA().getSelfUser().getAvatarUrl());
            eb.setThumbnail(member.getEffectiveAvatarUrl());


            channel.sendMessage(eb.build()).queue();
        }
    }
}
