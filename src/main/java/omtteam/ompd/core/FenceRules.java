package omtteam.ompd.core;

import omtteam.omlib.core.security.PlayerInfo;
import omtteam.omlib.core.security.TrustManager;

import java.util.Optional;

/**
 * Who a fence hurts on contact: everything living except the owner, as on 1.20.1+. The owner is the fence's owner or a
 * player the owner shares ownership with ({@link TrustManager#isOwner(PlayerInfo)}, the 1.12 owner concept); trusted
 * players, the owner's team and pets are hurt. Dropped items are left alone by the block.
 */
public final class FenceRules {
    private FenceRules() {
    }

    /**
     * @param player the touching entity as a player, if it is one
     */
    public static boolean hurts(TrustManager trust, Optional<PlayerInfo> player) {
        return !player.isPresent() || !trust.isOwner(player.get());
    }
}
