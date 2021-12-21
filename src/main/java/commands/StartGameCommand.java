package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;

import events.CoinsSystem;
import events.GameSystem;




public class StartGameCommand extends ListenerAdapter {
    private final String prefix = "!";
    private String idMessageJoin = null;
    private GameSystem gameSystem = null;
    private Message msgstart = null;
    private boolean jointime = false;



    public StartGameCommand(GameSystem gameSystem) {
        this.gameSystem = gameSystem;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if(msg.getAuthor().isBot()){
            return;
        }
        if(msg.getContentRaw().matches(prefix+"startgame.*")){
            Member member = event.getMember();
            MessageChannel channel = event.getChannel();
//            if(!member.isOwner()){
//                channel.sendMessage("sem permissoes").queue();
//                return;
//            }
            
            if(gameSystem.isGameStarted() || jointime){
                channel.sendMessage("jogo já criado").queue();
                return;
            }

            event.getMessage().delete().queue();

            gameSystem.createChannel(event.getGuild(), event.getMember());    

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("MindGame will start in 30 seconds", null);
            eb.setColor(new Color(180, 70, 0));
            eb.setDescription("React below to join in the game");
            eb.setFooter("MindBot | "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), event.getJDA().getSelfUser().getAvatarUrl());
            eb.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());

            channel.sendMessage(eb.build()).queue((message) -> {
                message.addReaction("U+2705").queue();
                idMessageJoin = message.getIdLong()+"";
                msgstart = msg;
                jointime = true;
                Thread thread = new Thread(){
                    public void run(){
                        message.editMessage(eb.build()).queue(msg ->{
                            try {
                                 Thread.sleep(10000);
                                 eb.setTitle("MindGame will start in 20 seconds ");
                                 msg.editMessage(eb.build()).queue();
                                 Thread.sleep(10000);
                                 eb.setTitle("MindGame will start in 10 seconds");
                                 msg.editMessage(eb.build()).queue();
                                 Thread.sleep(5000);
                                eb.setTitle("MindGame will start in 5 seconds ");
                                msg.editMessage(eb.build()).queue();
                                Thread.sleep(3000);
                                eb.setTitle("MindGame will start in 4 seconds");
                                msg.editMessage(eb.build()).queue();
                                Thread.sleep(1000);
                                eb.setTitle("MindGame will start in 3 seconds ");
                                msg.editMessage(eb.build()).queue();
                                Thread.sleep(1000);
                                eb.setTitle("MindGame will start in 2 seconds ");
                                msg.editMessage(eb.build()).queue();
                                Thread.sleep(1000);
                                eb.setTitle("MindGame will start in 1 seconds ");
                                msg.editMessage(eb.build()).queue();
                                Thread.sleep(1000);
                                eb.setTitle("Mind Game will start soon. Good luck ");
                                msg.editMessage(eb.build()).queue();
                                gameSystem.startGame();
                                jointime = false;
                                Thread.sleep(5000);
                                msg.delete().queue();
                            } catch (InterruptedException e) {}
                        });
                    }
                };
                thread.start();
            });    
        }


    }

   @Override
   public void onMessageReactionAdd(MessageReactionAddEvent event){
        if(event.getUser().isBot()){
            return;
        }
        if(idMessageJoin.equals(event.getMessageId())){
            if(gameSystem.isGameStarted()){
                event.getChannel().sendMessage(event.getUser().getAsMention()+" jogo já iniciado").queue();
                return;
            }

            if(event.getReaction().getReactionEmote().isEmote()){
                return;
            }

            if(event.getReaction().getReactionEmote().getAsCodepoints().equals("U+2705")){
                Member member = event.getMember();
                gameSystem.addPlayer(member,event.getChannel(),event.getReaction());
            }
        }
   }


}
