package omtteam.ompd.core;

import omtteam.omlib.core.security.PlayerIdentity;
import omtteam.omlib.core.security.PlayerInfo;
import omtteam.omlib.core.security.TrustManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FenceRulesTest {
    private static final PlayerInfo OWNER = player("Owner");
    private static final PlayerIdentity FRIEND = player("Friend").identity();
    private static final PlayerIdentity TEAMMATE = player("Teammate").identity();
    private static final PlayerIdentity SHARED = player("Shared").identity();
    private static final PlayerIdentity STRANGER = player("Stranger").identity();

    private FakeSecurityEnvironment environment;
    private TrustManager trust;

    private static PlayerInfo player(String name) {
        return new PlayerInfo(UUID.nameUUIDFromBytes(name.getBytes()), name, false);
    }

    @BeforeEach
    void setUp() {
        environment = new FakeSecurityEnvironment();
        environment.teams.put("Owner", "red");
        environment.teams.put("Teammate", "red");
        environment.teams.put("Stranger", "blue");
        environment.sharedOwners.add(SHARED.uuid());
        trust = new TrustManager(environment, () -> {
        });
        trust.setOwner(OWNER.identity());
        trust.addTrustedPlayer(OWNER, FRIEND);
    }

    private boolean hurtsPlayer(PlayerIdentity player) {
        return FenceRules.hurts(trust, Optional.of(player), Optional.empty());
    }

    private boolean hurtsPetOf(PlayerIdentity owner) {
        return FenceRules.hurts(trust, Optional.empty(), Optional.of(owner));
    }

    @Test
    void friendlyPlayersAreSpared() {
        assertFalse(hurtsPlayer(OWNER.identity()));
        assertFalse(hurtsPlayer(FRIEND), "trusted player");
        assertFalse(hurtsPlayer(TEAMMATE), "owner's team");
        assertFalse(hurtsPlayer(SHARED), "shared owner");
    }

    @Test
    void strangersAreHurt() {
        assertTrue(hurtsPlayer(STRANGER));
    }

    @Test
    void petsOfFriendlyPlayersAreSpared() {
        assertFalse(hurtsPetOf(OWNER.identity()));
        assertFalse(hurtsPetOf(FRIEND));
        assertFalse(hurtsPetOf(TEAMMATE));
        assertTrue(hurtsPetOf(STRANGER));
    }

    @Test
    void mobsAreHurt() {
        assertTrue(FenceRules.hurts(trust, Optional.empty(), Optional.empty()));
    }

    @Test
    void aFenceWithoutOwnerHurtsEveryone() {
        TrustManager noOwner = new TrustManager(environment, () -> {
        });
        assertTrue(FenceRules.hurts(noOwner, Optional.of(OWNER.identity()), Optional.empty()));
        assertTrue(FenceRules.hurts(noOwner, Optional.empty(), Optional.of(OWNER.identity())));
    }
}
