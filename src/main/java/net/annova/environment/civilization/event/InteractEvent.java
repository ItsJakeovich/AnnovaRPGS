package net.annova.environment.civilization.event;

import net.annova.event.custom.LeftClickCE;
import net.annova.event.custom.RightClickCE;
import net.annova.object.Person;
import net.annova.object.Preview;
import net.minecraft.core.Vec3i;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.item.base.ItemType.CAMP;
import static net.annova.util.GlobalTasks.cancelPreviewBuild;
import static net.annova.util.GlobalTasks.previewBuild;

public class InteractEvent implements Listener {
    @EventHandler
    public void interact(RightClickCE event) {
        if (!CAMP.equals(event.getItemInHand())) return;

        Person person = event.getPerson();
        person.send(getInstance(), "camp-set-action");
        person.getConfirm().setConfirmType("set-camp");
    }

    @EventHandler
    public void movePreview(LeftClickCE event) {
        Person person = event.getPerson();
        Preview preview = event.getPerson().getPreview();
        if (preview == null) return;
        cancelPreviewBuild(person);
        Vec3i newPos = preview.position();
        float pitch = person.getServerPlayer().getXRot();
        if (pitch < -50) newPos = newPos.offset(0, 1, 0);
        else if (pitch > 50) newPos = newPos.offset(0, -1, 0);
        else {
            switch (Person.getCardinalDirection(person.getServerPlayer())) {
                case SOUTH -> newPos = newPos.offset(0, 0, 1);
                case WEST -> newPos = newPos.offset(-1, 0, 0);
                case EAST -> newPos = newPos.offset(1, 0, 0);
                default /*NORTH*/ -> newPos = newPos.offset(0, 0, -1);
            }
        }
        previewBuild(person, preview.structure(), newPos);
    }
}
