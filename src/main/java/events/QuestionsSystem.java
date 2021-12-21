package events;

import entities.Question;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import repository.Repository;
import system.MySqlConnector;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class QuestionsSystem extends ListenerAdapter {

    private static Message msgQuestion;
    private List<Member> GamePlayers = new ArrayList<>();;
    private List<Member> ReactionPlayers = new ArrayList<>();
    private int correctanwser;
    private MySqlConnector mysql;
    private MessageChannel gameChannel;
    private Repository repository;

    public QuestionsSystem(Repository repository) {
        this.repository=repository;
    }


    public List<Member> showQuestions(List<Member> gamePlayers, MessageChannel gameChannel) {
        this.GamePlayers = gamePlayers;
        this.gameChannel=gameChannel;
        try {
            List<Question> questionList = repository.getQuestions();

            for (int i = 0; i < questionList.size(); i++) {
                int id = questionList.get(i).getId();
                String question = questionList.get(i).getQuestion();
                String answer = questionList.get(i).getAnswer();

                List<Question> fakeanwsers = repository.getFakeAnswers(id);

                MessageEmbed embed = createQuestion(question,answer,fakeanwsers);
                gameChannel.sendMessage(embed).queue(message -> {
                    msgQuestion = message;
                    message.addReaction("1️⃣").queue();
                    message.addReaction("2️⃣").queue();
                    message.addReaction("3️⃣").queue();
                    message.addReaction("4️⃣").queue();
                });

                Thread.sleep(8000);
                checkAnwsers();
                msgQuestion.delete().queue();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return GamePlayers;
    }


    private MessageEmbed createQuestion(String question,String answer, List<Question> fakeanwsers) {

        EmbedBuilder eb = new EmbedBuilder()
            .setTitle("MindGame | Questions", null)
            .setColor(new Color(180, 70, 0))
            .setDescription("**" + question + "**\nChoose your answer with the reactions below")
            .setFooter("MindBot | " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), gameChannel.getJDA().getSelfUser().getAvatarUrl())
            .setThumbnail(gameChannel.getJDA().getSelfUser().getAvatarUrl());


        int num = 1 + (int) (Math.random() * ((4 - 1) + 1));
        for (int i = 1; i <= 4; i++) {
            if (i == num) {
                eb.addField("Option: " + i, answer, false);
                correctanwser = i;
            } else {
                eb.addField("Option: " + i, fakeanwsers.get(0).getAnswer(), false);
                fakeanwsers.remove(0);
            }
        }

        return eb.build();
    }


    private void checkAnwsers(){

        for(int i=0; i < GamePlayers.size(); i++){
            if(!ReactionPlayers.contains(GamePlayers.get(i))){
                Member member = GamePlayers.get(i);
                gameChannel.getJDA().getTextChannelById(gameChannel.getIdLong()).putPermissionOverride(member).setDeny(EnumSet.of(Permission.VIEW_CHANNEL)).queue();
                GamePlayers.remove(i);
            }
        }

        ReactionPlayers.clear();
    }


    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        if(event.getUser().isBot()){
            return;
        }
        if(msgQuestion == null){
            return;
        }
        if(msgQuestion.getId().equals(event.getMessageId())){
            if(event.getReaction().getReactionEmote().isEmote()){
                return;
            }

            if(ReactionPlayers.contains(event.getMember())){
                return;
            }

            if(event.getReaction().getReactionEmote().getName().equals("1️⃣")){
                if(correctanwser == 1){
                    ReactionPlayers.add(event.getMember());
                }
            }else if(event.getReaction().getReactionEmote().getName().equals("2️⃣")){
                if(correctanwser == 2){
                    ReactionPlayers.add(event.getMember());
                }
            }else if(event.getReaction().getReactionEmote().getName().equals("3️⃣")){
                if(correctanwser == 3){
                    ReactionPlayers.add(event.getMember());
                }
            }else if(event.getReaction().getReactionEmote().getName().equals("4️⃣")){
                if(correctanwser == 4){
                    ReactionPlayers.add(event.getMember());
                }
            }

        }
   }
}
