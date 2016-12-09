package qmaks.askme;

import java.util.List;

/**
 *
 * @author Qmaks
 */
public class Question {

    private final String question;
    private final String rewardcmd;
    private final String noreplymsg;
    private final String wronganswer;
    private final String correctanswer;
    private final List<String> answers;
    private final String massnotification;

    public Question(String question, List<String> answers, String rewardcmd, String wronganswer, String correctanswer, String noreplymsg, String massnotification) {
        this.question = question;
        this.answers = answers;
        this.noreplymsg = noreplymsg;
        this.rewardcmd = rewardcmd;
        this.wronganswer = wronganswer;
        this.correctanswer = correctanswer;
        this.massnotification = massnotification;
    }

    public String getQuestion() {
        return question;
    }

    public String getRewardCmd() {
        return rewardcmd;
    }
    
    public String getNoReplyMsg() {
        return noreplymsg;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getWrongAnswer() {
        return wronganswer;
    }

    public String getCorrentAnswer() {
        return correctanswer;
    }

    public String getMassNotification() {
        return massnotification;
    }
}
