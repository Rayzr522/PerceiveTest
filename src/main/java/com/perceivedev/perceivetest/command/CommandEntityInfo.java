package com.perceivedev.perceivetest.command;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.permissions.Permission;

import com.perceivedev.perceivecore.command.CommandResult;
import com.perceivedev.perceivecore.command.CommandSenderType;
import com.perceivedev.perceivecore.command.TranslatedCommandNode;
import com.perceivedev.perceivecore.command.argumentmapping.ConvertedParams;
import com.perceivedev.perceivetest.EntityInfo;
import com.perceivedev.perceivetest.PerceiveTest;

/**
 * @author Rayzr
 *
 */
public class CommandEntityInfo extends TranslatedCommandNode {

    private PerceiveTest plugin;

    public CommandEntityInfo(PerceiveTest plugin) {
        super(new Permission("PerceiveTest.entity"), "command.entity", plugin.getLanguage(), CommandSenderType.ALL);
        this.plugin = plugin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perceivedev.perceivecore.command.CommandNode#tabComplete(org.bukkit.
     * command.CommandSender, java.util.List, int)
     */
    @Override
    public List<String> tabComplete(CommandSender sender, List<String> wholeChat, int relativeIndex) {
        System.out.println(wholeChat.stream().collect(Collectors.joining("   ")));
        return Collections.emptyList();
    }

    @ConvertedParams(targetClasses = { EntityType.class, Double.class, String.class })
    public CommandResult onCommand(CommandSender sender, EntityType type, Double value, String alias, String[] args, String[] wholeChat) {

        if (type == null) {
            return CommandResult.SEND_USAGE;
        }

        sender.sendMessage(tr("command.entity.type", (Object) type.name()));

        EntityInfo info = plugin.getEntityManager().getOrDefault(type, new EntityInfo());

        info.setValue(value);
        info.setAlias(alias);

        sender.sendMessage(tr("command.entity.info", (Object) type.name(), info.getValue(), info.getAlias()));

        return CommandResult.SUCCESSFULLY_INVOKED;
    }

}
