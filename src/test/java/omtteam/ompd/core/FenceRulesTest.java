package omtteam.ompd.core;

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
    private static final PlayerInfo FRIEND = player("Friend");
    private static final PlayerInfo TEAMMATE = player("Teammate");
    private static final PlayerInfo SHARED = player("Shared");
    private static final PlayerInfo STRANGER = player("Stranger");

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
        trust.addTrustedPlayer(OWNER, FRIEND.identity());
    }

    private boolean hurtsPlayer(PlayerInfo player) {
        return FenceRules.hurts(trust, Optional.of(player));
    }

    @Test
    void theOwnerIsSpared() {
        assertFalse(hurtsPlayer(OWNER));
        assertFalse(hurtsPlayer(SHARED), "shared owner");
    }

    @Test
    void everyoneElseIsHurt() {
        assertTrue(hurtsPlayer(FRIEND), "trusted player");
        assertTrue(hurtsPlayer(TEAMMATE), "owner's team");
        assertTrue(hurtsPlayer(STRANGER));
    }

    @Test
    void mobsAndPetsAreHurt() {
        assertTrue(FenceRules.hurts(trust, Optional.empty()));
    }

    @Test
    void aFenceWithoutOwnerHurtsEveryone() {
        TrustManager noOwner = new TrustManager(environment, () -> {
        });
        assertTrue(FenceRules.hurts(noOwner, Optional.of(OWNER)));
    }
}
