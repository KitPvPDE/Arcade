package net.kitpvp.plugins.arcade.game.status;

import net.kitpvp.plugins.arcade.ArcadeCategory;
import net.kitpvp.plugins.arcade.ArcadeUser;
import net.kitpvp.plugins.arcade.game.ArcadeGame;
import net.kitpvp.plugins.arcade.game.InGameStatus;
import net.kitpvp.plugins.arcade.session.ArcadeAttributes;
import net.kitpvp.plugins.kitpvp.modules.session.SessionBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class JnrStatus extends InGameStatus {

    public JnrStatus(ArcadeGame game) {
        super(game);
    }

    @Override
    public ArcadeCategory getArcadeCategory() {
        return ArcadeCategory.JNR;
    }

    @Override
    protected void onEnter() {
        super.onEnter();

        Block startBlock = getGame().getConfiguration().getJnrStart().getBlock();
        for(Player player : getGame().getParticipants()) {
            player.teleport(getGame().getConfiguration().getJnrSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);

            ArcadeUser user = ArcadeUser.getUser(player);
            SessionBlock block = user.getSession(ArcadeCategory.JNR);
            block.setAttr(ArcadeAttributes.JNR_ACTIVE_BLOCK, startBlock);
        }
    }
}
