package neoroutine.minetd.common.grandmaster;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

//A helper class to make packets easier
public interface INormalMessage {

    //FriendlyByteBuffer is nbt, fancy nbt basically
    void toBytes(FriendlyByteBuf buf);

    void process(Supplier<NetworkEvent.Context> context);

}