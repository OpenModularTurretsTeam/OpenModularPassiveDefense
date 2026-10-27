package omtteam.ompd.core;

import omtteam.omlib.core.security.PlayerIdentity;
import omtteam.omlib.core.security.TrustManager;

/**
 * Who opens hardened doors, gates and the heavy door by hand: the owner, shared owners, trusted players and the
 * owner's team ({@link TrustManager#isOwnerOrTrusted}). Redstone opens them for everyone; blocks without an owner
 * open by redstone only.
 */
public final class PassageRules {
    private PassageRules() {
    }

    public static boolean canOpenByHand(TrustManager trust, PlayerIdentity player) {
        return trust.isOwnerOrTrusted(player);
    }
}
