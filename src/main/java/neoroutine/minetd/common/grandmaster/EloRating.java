package neoroutine.minetd.common.grandmaster;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.PacketDistributor;

//https://gitlab.com/LaDestitute/mana-mod-template/-/tree/main/src/main/java/ladestitute/manatemplate
public class EloRating
{
    private int maxPoints = 1000;
    private int points = 0;

    public int getMaxPoints() { return maxPoints;}

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int value)
    {
        points = value;

        if (points > maxPoints)
        {
            points = maxPoints;
        }

        if (points < 0)
        {
            points = 0;
        }
    }

    public void addPoints(Player player, int value)
    {
        points += value;

        if (points > maxPoints)
        {
            points = maxPoints;
        }
        update(player);
    }

    public void subtractPoints(Player player, int value)
    {
        points -= value;

        if (points < 0)
        {
            points = 0;
        }
        update(player);
    }

    public static void update(Player player)
    {
        if (!player.level.isClientSide())
        {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            player.getCapability(EloRatingProvider.PLAYER_ELO_POINTS).ifPresent(capability ->
            {
                SimpleNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                new ClientboundPlayerEloPointsUpdateMessage(capability.points));
            });
        }
    }

    public static Tag writeNBT(Capability<EloRating> capability, EloRating instance, Direction side) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("elopoints", instance.getPoints());
        return tag;
    }

    public static void readNBT(Capability<EloRating> capability, EloRating instance, Direction side, Tag nbt) {
        instance.setPoints(((CompoundTag) nbt).getInt("elopoints"));
    }

}
