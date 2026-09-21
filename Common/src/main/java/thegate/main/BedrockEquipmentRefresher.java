package thegate.main;

import org.bukkit.scheduler.BukkitRunnable;
import thegate.gate.GateManager;
import thegate.gate.GateObject;

/*
 * Bedrock (via Geyser) has a known, currently-unresolved flakiness bug (GeyserMC/Geyser#5795) where a
 * custom-textured skull SUPPLIED BY A PLUGIN (as opposed to one a player equips themselves via a real
 * client interaction) can render as a blank/default face, and sometimes only starts showing correctly
 * after the item gets re-equipped. Since a fake armor stand has no real inventory for a player to
 * interact with, we can't trigger that ourselves - but periodically re-sending the same equipment packet
 * is a low-cost way to give Geyser repeated chances to pick up the correct texture, which the linked
 * report suggests can help. This is NOT a guaranteed fix, just a mitigation.
 *
 * This deliberately reuses GatePackages.Display(p) rather than adding a new Bedrock-specific traversal:
 * every ArmorStand.showTo(p) call it makes already re-sends that player's equipment as a side effect (see
 * ArmorStand.java), so calling Display(p) again for a player already in range is functionally just "resend
 * everything for this player". It's harmless to do this for Java viewers too (same equipment they already
 * have), so this doesn't check BedrockCompat.isBedrock(p) at all - keeping this class simple and avoiding
 * needing to widen BedrockCompat's package-private access just for this.
 */
public class BedrockEquipmentRefresher extends BukkitRunnable {

    @Override
    public void run() {
        for (GateObject gate : GateManager.getGatesAsSet()) {
            for (org.bukkit.entity.Player p : gate.getPlayerInRange()) {
                gate.getPackages().Display(p);
            }
        }
    }
}
