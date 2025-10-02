package uk.co.nikodem.dFSmpPlugin.Content.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.nikodem.dFSmpPlugin.Content.Utils.AntiSpamManager;
import uk.co.nikodem.dFSmpPlugin.Content.Utils.CombatLoggingManager;

import java.util.concurrent.TimeUnit;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player plr) {

            long time = AntiSpamManager.getPlayerCooldownTimestamp(plr, "spawn");

            if (time > (System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5))) {
                return denySpawn(plr, "you teleported to spawn less than 5 minutes ago!");
            }

            if (CombatLoggingManager.playerInCombat(plr)) {
                return denySpawn(plr, "you are in combat!");
            }

            Location spawn = plr.getWorld().getSpawnLocation();

            AntiSpamManager.playerUpdateCooldownLog(plr, "spawn");
            plr.teleport(spawn);
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Successfully teleported to spawn!"));
        } else {
            sender.sendMessage("You are not player!");
        }
        return false;
    }

    private boolean denySpawn(Player plr, String reason) {
        plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You cannot teleport to spawn because "+reason));
        return false;
    }
}
