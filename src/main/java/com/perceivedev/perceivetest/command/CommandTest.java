/**
 * 
 */
package com.perceivedev.perceivetest.command;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.perceivedev.perceivecore.command.CommandResult;
import com.perceivedev.perceivecore.command.CommandSenderType;
import com.perceivedev.perceivecore.command.TranslatedCommandNode;
import com.perceivedev.perceivetest.PerceiveTest;
import com.perceivedev.perceivetest.PlayerData;

/**
 * @author Rayzr
 *
 */
public class CommandTest extends TranslatedCommandNode {

    private PerceiveTest plugin;

    /**
     * @param perceiveTest
     */
    public CommandTest(PerceiveTest plugin) {
        super(new Permission("PerceiveTest.admin"), "command.test", plugin.getLanguage(), CommandSenderType.PLAYER);
        this.plugin = plugin;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> wholeChat, int relativeIndex) {
        return Collections.emptyList();
    }

    @Override
    protected CommandResult executePlayer(Player player, String... args) {

        PlayerData data = plugin.getPlayerManager().getOrDefault(player.getUniqueId(), new PlayerData(player));
        data.addMoney(6.325910);

        msg(player, ">> Your total money now: &e{0,number,#.##}", data.getMoney());

        return CommandResult.SUCCESSFULLY_INVOKED;
    }

    /*
     * 
     * @Override
     * public boolean onCommand(CommandSender sender, Command command, String
     * label, String[] args) {
     * if (!(sender instanceof Player)) {
     * sender.sendMessage("Only players can do that!");
     * return true;
     * }
     * 
     * Player p = (Player) sender;
     * 
     * PlayerData data = plugin.getDataManager().getOrDefault(p.getUniqueId(),
     * new PlayerData(p));
     * data.addMoney(6.325910);
     * 
     * msg(p, ">> Your total money now: &e{0,number,#.##}", data.getMoney());
     * 
     * return true;
     * }
     * 
     *
     * 
     * public void apply(Player p) {
     * new Helix(Orientation.VERTICAL, 0.1, Particle.END_ROD, 2.0, 3.0,
     * 3.0).display(p.getLocation().add(0.0, 0.0, 0.0));
     * // new Rectangle(Orientation.HORIZONTAL, 3.0, 0.1,
     * // Particle.END_ROD).display(p.getLocation().add(0.0, 1.0, 0.0));
     * }
     */

    public static void msg(Player p, String msg, Object... args) {
        String output = msg;
        output = output.replace(">>", "&8\u00bb&6");
        output = output.replaceAll("&([0-9a-fklmnor])", "\u00a7$1");
        output = new MessageFormat(output).format(args);
        p.sendMessage(output);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perceivedev.perceivecore.command.CommandNode#tabComplete(org.bukkit.
     * command.CommandSender, java.util.List, int)
     */

}
