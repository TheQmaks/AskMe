package qmaks.askme;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import qmaks.askme.threads.GameThread;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Qmaks
 */
public class Main extends JavaPlugin implements Listener {

    public Thread gameThread;
    private static Main plugin;
    public boolean isActive = false;
    public Question currentQuestion;
    public ArrayList<Question> questions = new ArrayList<Question>();

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);
        ConfigurationSection cs = getConfig().getConfigurationSection("questions");
        for (String s : cs.getKeys(false)) {
            questions.add(new Question(cs.getString(s + ".question"), cs.getStringList(s + ".answers"), cs.getString(s + ".reward-command"), cs.getString(s + ".wrong-answer"), cs.getString(s + ".correct-answer"), cs.getString(s + ".no-reply-msg"), cs.getString(s + ".massnotification")));
        }
        new GameThread().start();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (isActive) {
            String msg = event.getMessage();
            if (!msg.isEmpty()) {
                Player p = event.getPlayer();
                if (getConfig().getString("answerPrefix").isEmpty()) {
                    if (answerValid(currentQuestion.getAnswers(), msg)) {
                        cmdExec(currentQuestion.getRewardCmd(), p);
                        msg(p, currentQuestion.getCorrentAnswer());
                        massMsg(currentQuestion.getMassNotification().replace("<sender>", p.getName()));
                        isActive = false;
                        gameThread.stop();
                        new GameThread().start();
                        event.setCancelled(true);
                    } else {
                        msg(p, currentQuestion.getWrongAnswer());
                    }
                } else if (msg.startsWith(getConfig().getString("answerPrefix"))) {
                    if (answerValid(currentQuestion.getAnswers(), msg.substring(getConfig().getString("answerPrefix").length(), msg.length()))) {
                        cmdExec(currentQuestion.getRewardCmd(), p);
                        msg(p, currentQuestion.getCorrentAnswer());
                        massMsg(currentQuestion.getMassNotification());
                        isActive = false;
                        gameThread.stop();
                        new GameThread().start();
                        event.setCancelled(true);
                    } else {
                        msg(p, currentQuestion.getWrongAnswer());
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private void cmdExec(String s, Player p) {
        getServer().dispatchCommand(getServer().getConsoleSender(), s.replace("<sender>", p.getName()));
    }

    public void massMsg(String s) {
        for (Player p : getServer().getOnlinePlayers()) {
            msg(p, s);
        }
    }

    private void msg(Player p, String s) {
        p.sendMessage(s.replace("<sender>", p.getName()).replace("<pluginPrefix>", getConfig().getString("pluginPrefix")).replace("&", "\u00a7"));
    }

    private boolean answerValid(List<String> answers, String answer) {
        for (String s : answers) {
            if (answer.toLowerCase().equals(s.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static Main getPlugin() {
        return plugin;
    }
}
