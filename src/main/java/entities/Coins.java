package entities;

public class Coins {

    private String iduser;
    private String idguild;
    private int coins;

    public Coins(String iduser, String idguild, int coins) {
        this.iduser=iduser;
        this.idguild=idguild;
        this.coins=coins;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getIdguild() {
        return idguild;
    }

    public void setIdguild(String idguild) {
        this.idguild = idguild;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public static QuestionBuilder builder() {
        return new QuestionBuilder();
    }

    public static class QuestionBuilder {
        private String iduser;
        private String idguild;
        private int coins;

        public QuestionBuilder iduser(String iduser) {
            this.iduser = iduser;
            return this;
        }

        public QuestionBuilder idguild(String idguild) {
            this.idguild = idguild;
            return this;
        }

        public QuestionBuilder coins(int coins) {
            this.coins = coins;
            return this;
        }

        public Coins build() {
            return new Coins(this.iduser,this.idguild,this.coins);
        }
    }

}
