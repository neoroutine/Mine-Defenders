package neoroutine.minetd.common.grandmaster;

import neoroutine.minetd.MineTD;
import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EloRatingProvider implements ICapabilitySerializable<Tag>
{
    public static Capability<EloRating> PLAYER_ELO_POINTS = CapabilityManager.get(new CapabilityToken<>(){});


    @Nonnull
    private final EloRating instance;

    private final LazyOptional<EloRating> instanceHandler;

    public EloRatingProvider()
    {
        instance = new EloRating();
        instanceHandler = LazyOptional.of(this::getInstance);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side)
    {
        return PLAYER_ELO_POINTS.orEmpty(capability, instanceHandler);
    }

    public void invalidate()
    {
        instanceHandler.invalidate();
    }

    public EloRating getInstance()
    {
        return instance;
    }

    @Override
    public Tag serializeNBT()
    {
        return EloRating.writeNBT(PLAYER_ELO_POINTS, instance, null);
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        EloRating.readNBT(PLAYER_ELO_POINTS, instance, null, nbt);
    }
}
