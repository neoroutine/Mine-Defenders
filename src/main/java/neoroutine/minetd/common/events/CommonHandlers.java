package neoroutine.minetd.common.events;

import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.towers.TowerBlockEntity;
import neoroutine.minetd.common.grandmaster.ClientboundPlayerEloPointsUpdateMessage;
import neoroutine.minetd.common.grandmaster.EloRatingProvider;
import neoroutine.minetd.common.grandmaster.SimpleNetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = MineTD.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonHandlers
{
    public static final ResourceLocation ELO_POINTS_CAP = new ResourceLocation(MineTD.MODID, "elopoints");

    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event)
    {
        event.register(EloRatingProvider.class);
    }


    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer && !(event.getEntity() instanceof FakePlayer))
        {
            event.getEntity().getCapability(EloRatingProvider.PLAYER_ELO_POINTS).ifPresent(t ->
                    SimpleNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                            new ClientboundPlayerEloPointsUpdateMessage(t.getPoints())));
        }
    }


    //As explained below, this event is responsible for attaching and also removing the capability if the entity/item/etc no longer exists
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        //Do nothing if the entity is not the player
        if (event.getObject() instanceof Player && event.getObject().isAlive()) {
            //We attach our capability here to the player, another entity, a chunk, blocks, items, etc
            EloRatingProvider provider = new EloRatingProvider();
            event.addCapability(ELO_POINTS_CAP, new EloRatingProvider());
            event.addListener(provider::invalidate);
            System.out.println("CAPABILITY ATTACHED TO PLAYER");
            //The invalidate listener is a listener that removes the capability when the entity/itemstack/etc is destroyed, so we can clear our lazyoptional
            //This is important, if something else gets our cap and stores the value, it becomes invalid and is not usable for safety reasons
            //Otherwise, this creates a dangling reference, which is a reference to an object that no longer exists
            //As a result, make sure not to have the invalidate line missing when attaching a capability
            //You are responsible for handling your own mod's proper lazy-val cleanup
        }
    }

    @SubscribeEvent
    public static void onDeathEvent(LivingDeathEvent event) {
        //Also look into onplayercloned events if you wish to keep cap-data persisted between death or dimension transfers
        if (event.getEntityLiving() instanceof Player) {
            Entity player = event.getEntityLiving();
            //START NOTE
            //This is the proper way to check/modify/edit/etc capability values
            //Verify if it's present then act on via the varible-name we provide, such as "h"
            player.getCapability(EloRatingProvider.PLAYER_ELO_POINTS).ifPresent(cap -> {
                {
                    cap.setPoints(20);
                }
            });
            //END NOTE
        }
    }

    @SubscribeEvent
    public static void blockPlacement(BlockEvent.EntityPlaceEvent event)
    {
        if (event.getEntity() == null) { return;}
        BlockEntity placedBe = event.getEntity().level.getBlockEntity(event.getPos());
        if (placedBe instanceof TowerBlockEntity tower)
        {
            tower.updatePlacer((Player)event.getEntity());
            System.out.println(String.format("Placer = %s", tower.grandmaster.getGrandmasterName()));
        }
    }
}
