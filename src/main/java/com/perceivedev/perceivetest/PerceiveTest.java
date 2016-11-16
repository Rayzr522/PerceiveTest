package com.perceivedev.perceivetest;

import static com.perceivedev.perceivetest.command.CommandTest.msg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.perceivedev.perceivecore.command.CommandTree;
import com.perceivedev.perceivecore.command.DefaultCommandExecutor;
import com.perceivedev.perceivecore.command.DefaultTabCompleter;
import com.perceivedev.perceivecore.command.argumentmapping.ArgumentMapper;
import com.perceivedev.perceivecore.command.argumentmapping.ArgumentMappers;
import com.perceivedev.perceivecore.config.SerializationManager;
import com.perceivedev.perceivecore.config.SimpleSerializationProxy;
import com.perceivedev.perceivecore.config.util.DataFileManager;
import com.perceivedev.perceivecore.config.util.DataFolderManager;
import com.perceivedev.perceivecore.config.util.DataManager;
import com.perceivedev.perceivecore.language.I18N;
import com.perceivedev.perceivetest.command.CommandEntityInfo;
import com.perceivedev.perceivetest.command.CommandTest;

public class PerceiveTest extends JavaPlugin implements Listener {

    private DataManager<UUID, PlayerData>       playerManager;
    private DataManager<EntityType, EntityInfo> entityManager;
    private I18N                                language;

    @Override
    public void onEnable() {

        SerializationManager.addSerializationProxy(EntityType.class, new SimpleSerializationProxy<EntityType>() {
            public Object serializeSimple(EntityType object) {
                return object.name();
            }

            public EntityType deserializeSimple(Object data) {
                return EntityType.valueOf(data.toString());
            }
        });

        ArgumentMappers.addMapper(new ArgumentMapper<EntityType>() {
            public Class<EntityType> getTargetClass() {
                return EntityType.class;
            }

            public Optional<? extends EntityType> map(Queue<String> strings) {
                if (strings.isEmpty()) {
                    return Optional.empty();
                }
                String name = strings.poll().toUpperCase();

                try {
                    return Optional.ofNullable(EntityType.valueOf(name));
                } catch (IllegalArgumentException e) {
                    return Optional.empty();
                }
            }
        });

        ArgumentMappers.addMapper(new ArgumentMapper<Double>() {
            public Class<Double> getTargetClass() {
                return Double.class;
            }

            public Optional<? extends Double> map(Queue<String> strings) {
                if (strings.isEmpty()) {
                    return Optional.empty();
                }
                String str = strings.poll().trim();
                if (!str.matches("(\\+|\\-)?\\d+(\\.\\d+)?")) {
                    return Optional.empty();
                }
                return Optional.of(Double.valueOf(str));
            }
        });

        ArgumentMappers.addMapper(new ArgumentMapper<String>() {
            public Class<String> getTargetClass() {
                return String.class;
            }

            public Optional<? extends String> map(Queue<String> strings) {
                return Optional.ofNullable(strings.poll());
            }
        });

        playerManager = new DataFolderManager<>(this, "players", UUID.class, PlayerData.class);
        entityManager = new DataFileManager<>(this, "entities.yml", EntityType.class, EntityInfo.class);

        setupLanguage();

        setupCommands();

        getServer().getPluginManager().registerEvents(this, this);

        reload();
    }

    @Override
    public void onDisable() {
        playerManager.save();
        entityManager.save();
    }

    public void reload() {
        playerManager.load();
        entityManager.load();

        language.reload();
    }

    private void setupLanguage() {
        Path output = getDataFolder().toPath().resolve("language");

        if (Files.notExists(output)) {
            try {
                Files.createDirectories(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        I18N.copyDefaultFiles("language", output, false, getFile());

        language = new I18N(this, "language");
    }

    private void setupCommands() {

        CommandTree tree = new CommandTree();

        CommandExecutor executor = new DefaultCommandExecutor(tree, language);
        TabCompleter tabCompleter = new DefaultTabCompleter(tree);

        CommandTest commandTest = new CommandTest(this);
        CommandEntityInfo commandEntity = new CommandEntityInfo(this);

        tree.addTopLevelChildAndRegister(commandTest, executor, tabCompleter, this, "test", "ptest");
        tree.addTopLevelChildAndRegister(commandEntity, executor, tabCompleter, this, "entityinfo");

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        double money = playerManager.get(p.getUniqueId()).getMoney();

        msg(p, ">> Welcome {0}!", p.getName());
        msg(p, ">> You have {0,number,$#.##}", money);
    }

    /**
     * @return
     */
    public DataManager<UUID, PlayerData> getPlayerManager() {
        return playerManager;
    }

    public PlayerData getPlayerData(Player player) {
        return playerManager.getOrDefault(player.getUniqueId(), new PlayerData(player));
    }

    /**
     * @return
     */
    public DataManager<EntityType, EntityInfo> getEntityManager() {
        return entityManager;
    }

    /**
     * @return the language
     */
    public I18N getLanguage() {
        return language;
    }

    /**
     * @param key
     * @param formattingObjects
     * @return
     * @see com.perceivedev.perceivecore.language.MessageProvider#tr(java.lang.String,
     *      java.lang.Object[])
     */
    public String tr(String key, Object... formattingObjects) {
        return language.tr(key, formattingObjects);
    }

}
