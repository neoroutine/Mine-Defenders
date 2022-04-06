package neoroutine.minetd.common.grandmaster;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundPlayerEloPointsUpdateMessage implements INormalMessage {
    int points;

    public ClientboundPlayerEloPointsUpdateMessage(int mana) {
        this.points = mana;
    }

    public ClientboundPlayerEloPointsUpdateMessage(FriendlyByteBuf buf) {
        this.points = buf.readInt();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(points);
    }

    @Override
    public void process(Supplier<NetworkEvent.Context> context) {
        //This method is for when information is received by the intended end (i.e, client in this case)
        //We can ignore login to server/client for NetworkDirection, its used for internal forge stuff
        //Remember that client/server side rules apply here
        //Access client stuff only in client, otherwise you will crash MC
        if (context.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            context.get().enqueueWork(() -> Minecraft.getInstance().player.getCapability(EloRatingProvider.PLAYER_ELO_POINTS).
                    ifPresent(cap -> {
                        //do stuff with the info, such as mainly syncing info for the client-side gui
                        cap.setPoints(this.points);
                    }));
        }
    }
}



