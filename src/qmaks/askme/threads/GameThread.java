package qmaks.askme.threads;

import java.util.Random;
import qmaks.askme.Main;
import qmaks.askme.Question;

/**
 *
 * @author Qmaks
 */
public class GameThread extends Thread {

    private int prevRnd = -1;
    private int prevQuestion = -1;

    public GameThread() {
        Main.getPlugin().gameThread = this;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(Main.getPlugin().getConfig().getInt("gameDelay") * 1000);

            Main.getPlugin().isActive = true;
            Question question = null;
            if (Main.getPlugin().getConfig().getInt("mode") == 1) {
                question = Main.getPlugin().questions.get(trueRandom(0, Main.getPlugin().questions.size() - 1));
            } else {
                prevQuestion = prevQuestion + 1;
                question = Main.getPlugin().questions.get(prevQuestion);
                
                if (prevQuestion >= Main.getPlugin().questions.size() - 1) {
                    prevQuestion = -1;
                }
            }
            Main.getPlugin().currentQuestion = question;

            for (String s : Main.getPlugin().getConfig().getStringList("question-msg")) {
                Main.getPlugin().massMsg(s.replace("<question>", question.getQuestion()));
            }

            Thread.sleep(Main.getPlugin().getConfig().getInt("gamePeriod") * 1000);

            Main.getPlugin().isActive = false;
            Main.getPlugin().massMsg(Main.getPlugin().currentQuestion.getNoReplyMsg());

            new GameThread().start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static int trueRandom(int min, int max) {
        int out = randInt(min, max);
        if (prevRnd != -1) {
            int average = ((Long)Math.round((double)(min + max) / 2)).intValue();
            if (prevRnd > average) {
                out = randInt(min, average);
            } else {
                out = randInt(average, max);
            }
        }
        return prevRnd = out;
    }

    private static int randInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}
