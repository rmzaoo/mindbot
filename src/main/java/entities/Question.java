package entities;

public class Question {

    private int id;
    private String question;
    private String answer;

    public Question(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public static QuestionBuilder builder() {
        return new QuestionBuilder();
    }

    public static class QuestionBuilder {
        private int id;
        private String question;
        private String answer;

        public QuestionBuilder id(int id) {
            this.id = id;
            return this;
        }

        public QuestionBuilder question(String question) {
            this.question = question;
            return this;
        }

        public QuestionBuilder answer(String answer) {
            this.answer = answer;
            return this;
        }

        public Question build() {
            return new Question(this.id, this.question, this.answer);
        }
    }

}
