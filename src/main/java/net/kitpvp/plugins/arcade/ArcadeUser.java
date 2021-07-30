package net.kitpvp.plugins.arcade;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.kitpvp.plugins.kitpvp.modules.session.PlayerSession;
import net.kitpvp.plugins.kitpvp.modules.session.Session;
import net.kitpvp.plugins.kitpvp.modules.session.SessionBlock;
import net.kitpvp.plugins.kitpvpcore.user.SimpleUserFactory;
import net.kitpvp.plugins.kitpvpcore.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ArcadeUser extends User {

    public static final SimpleUserFactory<ArcadeUser> USER_FACTORY = new SimpleUserFactory<>(ArcadeUser::new);
    private final Session session;
    private final Map<ArcadeCategory, SessionBlock> sessionBlockMap = new HashMap<>();

    public ArcadeUser(Player player) {
        super(player);
        this.session = new PlayerSession(ArcadePlugin.getPlugin().getEventRegister(), player, this);
    }

    public static @NotNull ArcadeUser getUser(Player player) {
        return USER_FACTORY.getUser(player);
    }

    public static ArcadeUser getUser(UUID playerId) {
        return USER_FACTORY.getUser(playerId);
    }

    public SessionBlock getSession(ArcadeCategory category) {
        return this.sessionBlockMap.computeIfAbsent(category, (ArcadeCategory ignored) ->
            this.getSession().createBlock());
    }

    public Session getSession() {
        return session;
    }
}
