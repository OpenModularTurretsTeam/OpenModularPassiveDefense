package omtteam.ompd.util;

import omtteam.omlib.core.security.PlayerIdentity;
import omtteam.omlib.core.security.PlayerInfo;
import omtteam.omlib.core.security.TrustManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PassageRulesTest {
    private static final PlayerInfo OWNER = player("Owner");
    private static final PlayerIdentity FRIEND = player("Friend").identity();
    private static final PlayerIdentity TEAMMATE = player("Teammate").identity();
    private static final PlayerIdentity SHARED = player("Shared").identity();
    private static final PlayerIdentity STRANGER = player("Stranger").identity();

    private TrustManager trust;

    private static PlayerInfo player(String name) {
        return new PlayerInfo(UUID.nameUUIDFromBytes(name.getBytes()), name, false);
    }

    @BeforeEach
    void setUp() {
        FakeSecurityEnvironment environment = new FakeSecurityEnvironment();
        environment.teams.put("Owner", "red");
        environment.teams.put("Teammate", "red");
        environment.teams.put("Stranger", "blue");
        environment.sharedOwners.add(SHARED.uuid());
        trust = new TrustManager(environment, () -> {
        });
        trust.setOwner(OWNER.identity());
        trust.addTrustedPlayer(OWNER, FRIEND);
    }

    @Test
    void friendlyPlayersOpen() {
        assertTrue(PassageRules.canOpenByHand(trust, OWNER.identity()));
        assertTrue(PassageRules.canOpenByHand(trust, FRIEND), "trusted player");
        assertTrue(PassageRules.canOpenByHand(trust, TEAMMATE), "owner's team");
        assertTrue(PassageRules.canOpenByHand(trust, SHARED), "shared owner");
    }

    @Test
    void strangersDoNotOpen() {
        assertFalse(PassageRules.canOpenByHand(trust, STRANGER));
    }

    @Test
    void withoutOwnerNobodyOpensByHand() {
        TrustManager unowned = new TrustManager(new FakeSecurityEnvironment(), () -> {
        });
        assertFalse(PassageRules.canOpenByHand(unowned, STRANGER));
        assertFalse(PassageRules.canOpenByHand(unowned, OWNER.identity()));
    }
}
