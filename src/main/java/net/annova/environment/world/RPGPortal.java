package net.annova.environment.world;

import net.annova.command.cmdRPG.RPGCode;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static net.annova.environment.world.GlobalWorld.LOBBY;
import static net.annova.environment.world.sector0.SuruWahare.world0;

public enum RPGPortal {
    RPG_ENTER(LOBBY, 50, 110, 50, new TeleportTransition(world0, new Vec3(0.5, 100, 0.5), Vec3.ZERO, 0, 0, TeleportTransition.PLAY_PORTAL_SOUND),
            portal -> RPGCode.ALLOW_TO_JOIN)
    ;
    /** Событие перехода в {@link net.annova.thread.PersonStatusTimer}
     * Эффект портала в {@link net.annova.thread.FiveSecondsTimer}
     */

    private static final Map<ServerLevel, Map<BlockPos, RPGPortal>> PORTALS = new HashMap<>();
    static {
        for (RPGPortal portal : RPGPortal.values()) {
            PORTALS.putIfAbsent(portal.level, new HashMap<>());
            PORTALS.get(portal.level).put(new BlockPos(portal.x, portal.y, portal.z), portal);
        }
    }
    private final ServerLevel level;
    private final int x,y,z;
    private final TeleportTransition transition;
    private final Function<RPGPortal, Boolean> function;

    RPGPortal(ServerLevel level, int x, int y, int z, TeleportTransition transition, Function<RPGPortal, Boolean> function) {
        this.level = level;
        this.x = x; this.y = y; this.z = z;
        this.transition = transition;
        this.function = function;
    }
    public static RPGPortal getPortal(ServerLevel level, BlockPos pos) {
        return PORTALS.get(level).get(pos);
    }

    public ServerLevel getLevel() {
        return level;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }

    public TeleportTransition getTransition() {
        return transition;
    }

    public Function<RPGPortal, Boolean> getFunction() {
        return function;
    }
}
