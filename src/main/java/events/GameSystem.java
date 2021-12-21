package events;

import java.awt.Color;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import system.MySqlConnector;

public class GameSystem extends ListenerAdapter{

    private static TextChannel gameChannel = null;
    private boolean gameStarted = false;
    private static List<Member> players = new ArrayList<>();
    // private HashMap<Member, Integer> players = new HashMap<>();
    private QuestionsSystem questionsSystem = null;
    private CoinsSystem coinsSystem;

    public GameSystem(QuestionsSystem questionsSystem, CoinsSystem coinsSystem) {
        this.questionsSystem = questionsSystem;
        this.coinsSystem = coinsSystem;
    }


    public void addPlayer(Member member, MessageChannel channel, MessageReaction reaction){
        if(gameChannel.getMembers().size() >= 4){
            channel.sendMessage(member.getAsMention()+" game is already full").queue();
            reaction.removeReaction(member.getUser()).queue();
            return;
        }

        if(players.contains(member)){
            channel.sendMessage(member.getAsMention()+" are you already in the game").queue();
            reaction.removeReaction(member.getUser()).queue();
            return;
        }

        players.add(member);
        // players.put(member, 0);
        gameChannel.putPermissionOverride(member).setAllow(EnumSet.of(Permission.VIEW_CHANNEL)).queue();
        gameChannel.sendMessage(member.getAsMention()+" player has joined the game").queue();
        reaction.removeReaction(member.getUser()).queue();
    }

    
    public void addPlayer(Member member){
        if(gameChannel.getMembers().size() >= 10){
            return;
        }

        if(players.contains(member)){
            return;
        }

        players.add(member);
        gameChannel.putPermissionOverride(member).setAllow(EnumSet.of(Permission.VIEW_CHANNEL)).queue();
        gameChannel.sendMessage(member.getAsMention()+" player has joined the game").queue();
    }

    
    public TextChannel createChannel(Guild guild, Member member){
        ChannelAction<TextChannel> newChannel = guild.createTextChannel("Mind Game");
        newChannel.addPermissionOverride(guild.getPublicRole(), null,EnumSet.of(Permission.VIEW_CHANNEL,Permission.MESSAGE_WRITE));
        newChannel.queue(channel -> {
            gameChannel = channel;
            addPlayer(member);
        });

        return gameChannel;
    }


    public void startGame(){
        gameStarted = true;

        MessageEmbed eb = new EmbedBuilder()
            .setTitle("Welcome to MindGame", null)
            .setColor(new Color(180, 70, 0))
            .setDescription("Get ready to answer the 10 most difficult questions you've seen so far.\n" +
                "If you answer correctly, you go to the next question.\n" +
                "If you answer incorrectly, you die.\n" +
                "At the end of the game the prize will be distributed to everyone.\n" +
                "Good luck!")
            .setFooter("MindBot | "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), gameChannel.getJDA().getSelfUser().getAvatarUrl())
            .setThumbnail("https://c.tenor.com/FnMFDXJubNkAAAAd/dannychavinha-squidgame.gif")
            .build();


        MessageHistory history = gameChannel.getHistory();
        history.retrievePast(25).queue(messages -> {
            for(Message message : messages){
               message.delete().queue();
            }

            gameChannel.sendMessageEmbeds(eb).addFile(new File("src/main/resources/squidgame_sound.mp3")).queue(msg ->{
                try {
                    Thread.sleep(25000);
                    msg.delete().queue();
                    List<Member> wins = questionsSystem.showQuestions(players, gameChannel);
                    endGame(wins);
                } catch (InterruptedException e) {}
            });
        });         
    }

    public void endGame(List<Member> players) throws InterruptedException {
        if(players.size() > 0){
            gameChannel.sendMessage("https://c.tenor.com/PXHTdE-DWjQAAAAC/money-squid-game.gif").queue();
            gameChannel.sendMessage("Congratulations, the following players have won the game").queue();
            int premio = 100000;
            players.forEach(member -> {
                int premiofinal = coinsSystem.getMoney(member, gameChannel.getGuild()) + (premio / players.size());
                gameChannel.sendMessage(member.getAsMention() + " you win "+ (premio / players.size())+" Coins").queue();
                coinsSystem.setMoney(member, Math.round(premiofinal) , gameChannel.getGuild());
            });
            Thread.sleep(5000);
            gameChannel.delete().queue();
        }else{
            gameChannel.delete().queue();
        }

        gameChannel = null;
        gameStarted = false;
        GameSystem.players.clear();
    }

    public boolean isGameStarted(){
        return gameStarted;
    }


    public TextChannel getGameChannel() {
        return gameChannel;
    }
    
}
