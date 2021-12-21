package repository;

import entities.Coins;
import entities.Question;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import system.MySqlConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    private final MySqlConnector dbconnector;

    public Repository(MySqlConnector dbconnector) {
        this.dbconnector = dbconnector;
    }

    public List<Question> getQuestions(){
        try{
            dbconnector.startConn();
            ResultSet rsQuestion = dbconnector.executeQuery(
                    "SELECT questions.id,question,answer FROM questions "+
                    "JOIN response ON questions.id=response.id ORDER BY RAND() LIMIT 10");

            List<Question> questionList = new ArrayList<>();

            while(rsQuestion.next()) {
                Question question = Question.builder()
                        .id(rsQuestion.getInt("id"))
                        .question(rsQuestion.getString("question"))
                        .answer(rsQuestion.getString("answer"))
                        .build();

                questionList.add(question);
            }
            return questionList;

        } catch(java.sql.SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            dbconnector.closeConn();
        }
    }

    public List<Question> getFakeAnswers(int id){
        try{
            dbconnector.startConn();
            ResultSet rsAnswers = dbconnector.executeQuery("select id,fakeanwser from fakeresponse "+
            " JOIN response ON fakeresponse.idquestion=response.id where id = "+id+" ORDER BY RAND() ");


            List<Question> questionList = new ArrayList<>();

            while(rsAnswers.next()) {
                Question question = Question.builder()
                        //.id(rsAnswers.getInt("id"))
                        .answer(rsAnswers.getString("fakeanwser"))
                        .build();

                questionList.add(question);
            }

            return questionList;

        } catch(java.sql.SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            dbconnector.closeConn();
        }
    }

    public Coins getUserCoins(Member member, Guild guild) {
        int coins = 0;
        try {
            dbconnector.startConn();
            ResultSet rs = dbconnector.executeQuery("select * from coins where iduser='"+member.getId()
                    +"' and idguild='"+guild.getId()+"'");
            rs.next();
            coins = rs.getInt("coins");
        } catch (SQLException e) {
            dbconnector.executeUpdate("insert into coins (iduser,idguild,coins) values ('"+member.getId()+"','"+guild.getId()+"','0')");
        }finally {
            dbconnector.closeConn();
        }

        return new Coins(member.getId(), guild.getId(),coins);
    }

    public void setUserCoins(Member member, Guild guild, int coins) {
        getUserCoins(member, guild);
        dbconnector.startConn();
        dbconnector.executeUpdate("update coins set coins = '"+coins+"' where iduser='"+member.getId()+"' and idguild='"+guild.getId()+"'");
        dbconnector.closeConn();
    }
}
