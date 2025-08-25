package net.annova.thread;

import net.annova.environment.world.RPGPortal;
import net.annova.event.custom.PostPortalTeleportCE;
import net.annova.object.person.status.Status;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.StatusType;
import net.annova.thread.event.SecondTickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.portal.TeleportTransition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.EnumSet;
import java.util.Set;

import static net.annova.environment.dungeon.GlobalDungeon.getPortalTransition;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.STOPPED;

public class PersonStatusTimer implements Listener {
    @EventHandler
    public void onTick(SecondTickEvent event) {
        Set<StatusType> toRemove = EnumSet.noneOf(StatusType.class);
        for (Player player : Bukkit.getOnlinePlayers()) {
            RPGPerson person = getRPGPerson(player);
            if (player.getY() > 350) person.damage(2);
            StatusManager manager = person.getStatusManager();
            ServerPlayer serverPlayer = person.getServerPlayer();

            // Обновление позиции и выносливости
            ServerLevel level = serverPlayer.serverLevel();
            BlockPos position = serverPlayer.blockPosition();
            TeleportTransition transition = getPortalTransition(level, position);
            if (transition != null) serverPlayer.teleport(transition);
            person.updateCurrentArea();
            person.updateStatusBar();
            if (manager.getStatus(STOPPED) == null) {
                person.getPerson().increaseStamina(0.05f);
                person.updateSpeed(150);
            }

            long time = System.currentTimeMillis();
            ItemStack inHand = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
            for (Status status : manager.getStatuses()) {
                if (status.toRemove(serverPlayer, time, inHand)) {
                    toRemove.add(status.getType());
                }
            }
            for (StatusType status : toRemove) {
                manager.removeStatus(status);
            } toRemove.clear();

            RPGPortal portal = RPGPortal.getPortal(level, position);
            if (portal != null) {
                serverPlayer.teleport(portal.getTransition());
                new PostPortalTeleportCE(portal, person, serverPlayer).callEvent();
            }
        }
    }
}