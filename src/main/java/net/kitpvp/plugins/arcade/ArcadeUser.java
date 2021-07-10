package net.kitpvp.plugins.arcade;

import net.kitpvp.plugins.kitpvp.modules.session.SessionBlock;
import net.kitpvp.plugins.kitpvp.user.CommonUser;
import net.kitpvp.plugins.kitpvpcore.user.SimpleUserFactory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArcadeUser extends CommonUser {

    public static final SimpleUserFactory<ArcadeUser> USER_FACTORY = new SimpleUserFactory<>(ArcadeUser::new);

    public static @NotNull ArcadeUser getUser(Player player) {
        return USER_FACTORY.getUser(player);
    }

    public static ArcadeUser getUser(UUID playerId) {
        return USER_FACTORY.getUser(playerId);
    }

    private final Map<ArcadeCategory, SessionBlock> sessionBlockMap = new HashMap<>();

    public ArcadeUser(Player player) {
        super(player);
    }

    public SessionBlock getSession(ArcadeCategory category) {
        return this.sessionBlockMap.computeIfAbsent(category, (ArcadeCategory ignored) ->
                this.getSession().createBlock());
    }
}
