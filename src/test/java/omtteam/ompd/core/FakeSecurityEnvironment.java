package omtteam.ompd.core;

import omtteam.omlib.core.security.PlayerIdentity;
import omtteam.omlib.core.security.SecurityEnvironment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/** Test double for {@link SecurityEnvironment}: teams by player name, shared owners by UUID. */
class FakeSecurityEnvironment implements SecurityEnvironment {
    final Map<String, String> teams = new HashMap<>();
    final Set<UUID> sharedOwners = new HashSet<>();

    @Override
    public Optional<String> getTeam(UUID uuid, String name) {
        return Optional.ofNullable(teams.get(name));
    }

    @Override
    public boolean operatorsCanAccessOwnedBlocks() {
        return false;
    }

    @Override
    public boolean turretsDamageTrustedPlayers() {
        return false;
    }

    @Override
    public boolean isSharedOwner(PlayerIdentity owner, PlayerIdentity player) {
        return sharedOwners.contains(player.uuid());
    }

    @Override
    public boolean offlineModeSupport() {
        return false;
    }
}
