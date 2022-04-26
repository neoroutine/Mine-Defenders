package neoroutine.minetd.common.grandmaster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

//Store all player relevant information
public class Grandmaster implements INBTSerializable<Tag>
{
    private Player player = null;
    private String uuid = "Unknown";

    public Grandmaster()
    {
        super();
    }


    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        if (player == null) {return;}
        this.player = player;
        this.uuid   = this.player.getStringUUID();
        onUpdate();
    }


    public boolean updatePlayer(Level level)
    {
        if (level == null) { return false;}

        if (this.player != null)
        {
            //setPlayer(this.player); ?
            this.uuid = this.player.getStringUUID();
            return true;
        }
        else
        {
            if (this.uuid.equals("Unknown")) { return false;}
        }

        Player player = level.getPlayerByUUID(UUID.fromString(uuid));
        if (player == null) { return false;}
        setPlayer(player);
        updatePlayer(level);

        return true;
    }

    public String getGrandmasterName()
    {
        if (this.player != null)
        {
            return this.player.getScoreboardName();
        }

        return "Unknown";
    }

    public String getGrandMasterUUID()
    {
        return this.uuid;
    }

    public int getGrandmasterEloPoints()
    {
        if (player == null) { return 0;}
        AtomicInteger points = new AtomicInteger();
        this.player.getCapability(EloRatingProvider.PLAYER_ELO_POINTS).ifPresent(capability ->
        {
            points.set(capability.getPoints());
        });

        return points.get();
    }

    protected void onUpdate() {}

    @Override
    public Tag serializeNBT()
    {
        String uuid = "Unknown";
        if (player != null)
        {
            uuid = player.getStringUUID();
        }

        CompoundTag player = new CompoundTag();
        player.putString("grandmasteruuid", uuid);

        return player;
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        CompoundTag cnbt = (CompoundTag)nbt;
        String uuid = cnbt.getString("grandmasteruuid");

        this.uuid = uuid;
        onUpdate();
    }
}
