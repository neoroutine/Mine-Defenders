package neoroutine.minetd.common.grandmaster;

import neoroutine.minetd.MineTD;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public final class SimpleNetworkHandler
{
    //This should be 1.0
    public static final String NETWORK_VERSION = "1.0";

    //The channel the packets are sent in
    //It takes the name of a modid, the channel name, a protocol version supplier (in short, sides),
    //server and client accepted versions are predicates, basically the protocol versions they will accept
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(
            new ResourceLocation(MineTD.MODID, "main")).networkProtocolVersion(() ->
            NETWORK_VERSION).serverAcceptedVersions(NETWORK_VERSION::equals).clientAcceptedVersions(
            NETWORK_VERSION::equals).simpleChannel();

    //An init where we register our packets
    public static void init() {
        int index = 0;
        //This type of packet is server to client
        registerMessage(index++, ClientboundPlayerEloPointsUpdateMessage.class, ClientboundPlayerEloPointsUpdateMessage::new);
    }

    private static <T extends INormalMessage> void registerMessage(int index, Class<T> messageType, Function<FriendlyByteBuf, T> decoder) {
        //Encoding is saving information to the byte buffer, decoding is the opposite of that (reading info)
        CHANNEL.registerMessage(index, messageType, INormalMessage::toBytes, decoder, (message, context) -> {
            message.process(context);
            context.get().setPacketHandled(true);
        });
    }
}
