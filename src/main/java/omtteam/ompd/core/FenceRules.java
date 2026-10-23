package omtteam.ompd.core;

import omtteam.omlib.core.security.PlayerIdentity;
import omtteam.omlib.core.security.TrustManager;

import java.util.Optional;

/**
 * Who a fence hurts on contact. Friendly are the owner, shared owners, trusted players and the owner's team
 * ({@link TrustManager#isOwnerOrTrusted}), and pets of any of them; everything else living is hurt. Dropped items are
 * left alone by the block.
 */
public final class FenceRules {
    private FenceRules() {
    }

    /**
     * @param player   the touching entity as a player, if it is one
     * @param petOwner the owner of the touching entity, if it is a pet
     */
    public static boolean hurts(TrustManager trust, Optional<PlayerIdentity> player, Optional<PlayerIdentity> petOwner) {
        if (player.isPresent()) {
            return !trust.isOwnerOrTrusted(player.get());
        }
        return !petOwner.isPresent() || !trust.isOwnerOrTrusted(petOwner.get());
    }
}
