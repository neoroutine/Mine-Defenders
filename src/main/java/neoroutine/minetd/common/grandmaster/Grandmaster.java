package neoroutine.minetd.common.grandmaster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

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

    public void setPlayer(Player value)
    {
        if (value == null) {return;}
        this.player = value;
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

        Player player = level.getServer().getPlayerList().getPlayer(UUID.fromString(uuid));
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
