package glorydark.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.level.particle.SpellParticle;
import cn.nukkit.level.particle.SplashParticle;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public class PotionSplashMain extends PluginBase {

    public List<PotionSplashData> potionSplashDataList = new ArrayList<>();

    public static String path;

    @Override
    public void onEnable() {
        path = this.getDataFolder().getPath();
        this.saveDefaultConfig();
        init();
        Config config = new Config(path + "/config.yml", Config.YAML);
        this.getServer().getCommandMap().register("", new Command(config.getString("command", "potionsplash"), "Add potion splash effect") {
            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                if (strings.length < 1) {
                    return false;
                }
                switch (strings[0]) {
                    case "add":
                        if (commandSender.isPlayer()) {
                            if (strings.length != 2) {
                                return false;
                            }
                            PotionSplashData data = new PotionSplashData(Integer.parseInt(strings[1]), ((Player) commandSender).getLocation());
                            potionSplashDataList.add(data);
                            saveAll();
                            commandSender.sendMessage(TextFormat.GREEN + "Add the potion spawn successfully");
                        }
                        break;
                    case "del":
                        if (commandSender.isPlayer()) {
                            Player player = (Player) commandSender;
                            potionSplashDataList.removeIf(data -> data.getLocation().floor().equals(player.getLocation().floor()));
                            commandSender.sendMessage(TextFormat.GREEN + "Remove the potion spawn successfully");
                        }
                        saveAll();
                        break;
                    case "reload":
                        init();
                        commandSender.sendMessage(TextFormat.GREEN + "Reload successfully");
                        break;
                }
                return false;
            }
        });
        this.getServer().getScheduler().scheduleRepeatingTask(this, () -> {
            for (PotionSplashData potionSplashData : potionSplashDataList) {
                Effect effect = Effect.getEffect(potionSplashData.getPotionId());
                Particle particle = new SpellParticle(potionSplashData.getLocation(), effect.getColor()[0],  effect.getColor()[1],  effect.getColor()[2]);
                potionSplashData.getLocation().getLevel().addParticle(particle);
            }
        }, config.getInt("interval_ticks", 20));
        this.getLogger().info(TextFormat.GREEN + "PotionSplashMain enabled!");
    }

    @Override
    public void onDisable() {
        potionSplashDataList.clear();
    }

    public void init() {
        Config config = new Config(path + "/config.yml", Config.YAML);
        List<Map<String, Object>> potionSplashSpawns = (List<Map<String, Object>>) config.get("potion_spawns");
        for (Map<String, Object> potionSplashSpawn : potionSplashSpawns) {
            String levelName = potionSplashSpawn.get("world").toString();
            Level level = Server.getInstance().getLevelByName(levelName);
            if (level == null) {
                if (Server.getInstance().loadLevel(levelName)) {
                    level = Server.getInstance().getLevelByName(levelName);
                }
            }
            if (level == null) {
                this.getLogger().info(TextFormat.RED + "World not found! World name: " + levelName);
                continue;
            }
            PotionSplashData data = new PotionSplashData((Integer) potionSplashSpawn.get("potion_id"),
                    new Location(Double.parseDouble(potionSplashSpawn.get("x").toString()),
                            Double.parseDouble(potionSplashSpawn.get("y").toString()),
                                    Double.parseDouble(potionSplashSpawn.get("z").toString()), level));
            potionSplashDataList.add(data);
            this.getLogger().info(TextFormat.GREEN + "Load success! Data: " + data);
        }
    }

    public void saveAll() {
        Config config = new Config(path + "/config.yml", Config.YAML);
        List<Map<String, Object>> potionSplashSpawns = (List<Map<String, Object>>) config.get("potion_spawns");
        for (PotionSplashData data : potionSplashDataList) {
            Map<String, Object> addMap = new LinkedHashMap<>();
            addMap.put("potion_id", data.getPotionId());
            addMap.put("x", data.getLocation().getFloorX() + 0.5);
            addMap.put("y", data.getLocation().getFloorY() + 0.5);
            addMap.put("z", data.getLocation().getFloorZ() + 0.5);
            addMap.put("world", data.getLocation().getLevel().getName());
            potionSplashSpawns.add(addMap);
            config.set("potion_spawns", potionSplashSpawns);
        }
        config.save();
    }
}