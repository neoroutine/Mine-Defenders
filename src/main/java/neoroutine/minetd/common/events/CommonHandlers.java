package neoroutine.minetd.common.events;

import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.generators.GeneratorBlockEntity;
import neoroutine.minetd.common.blocks.kings.KingBlockEntity;
import neoroutine.minetd.common.blocks.towerbase.TowerBaseBE;
import neoroutine.minetd.common.blocks.towers.TowerBlockEntity;
import neoroutine.minetd.common.grandmaster.ClientboundPlayerEloPointsUpdateMessage;
import neoroutine.minetd.common.grandmaster.EloRatingProvider;
import neoroutine.minetd.common.grandmaster.SimpleNetworkHandler;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
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
        }

        else if (placedBe instanceof KingBlockEntity king)
        {
            king.updatePlacer((Player)event.getEntity());
            king.grandmaster.getPlayer().getCapability(EloRatingProvider.PLAYER_ELO_POINTS).ifPresent(capability ->
            {
                capability.subtractPoints(king.grandmaster.getPlayer(), 20);
                int points = capability.getPoints();
                String message = String.format("Your placed your king. Points -%d (%d)", 20, points);
                king.grandmaster.getPlayer().displayClientMessage(new TranslatableComponent(message), true);
            });

            Level level = king.getLevel();
            BlockPos pos = king.getBlockPos();

            buildKingFloor(king, Blocks.BLACK_WOOL, Blocks.WHITE_WOOL);
            buildKingJail(king, Registration.LABYRINTH_GLASS.get());

            buildKingHallwayFloor(king, Blocks.BLACK_WOOL, Blocks.WHITE_WOOL, 10, 50);
            buildKingHallwayWalls(king, Registration.LABYRINTH_GLASS.get(), 10, 50);

        }
    }

    @SubscribeEvent
    public static void blockDestruction(BlockEvent.BreakEvent event)
    {
        Level level = event.getPlayer().level;

        BlockEntity brokenBe = level.getBlockEntity(event.getPos());
        if (brokenBe instanceof KingBlockEntity king)
        {
            buildKingFloor(king, Blocks.GRASS_BLOCK, Blocks.GRASS_BLOCK);
            buildKingJail(king, Blocks.AIR);

            buildKingHallwayFloor(king, Blocks.GRASS_BLOCK, Blocks.GRASS_BLOCK, 10, 50);
            buildKingHallwayWalls(king, Blocks.AIR, 10, 50);
        }
    }

    public static boolean isModdedBE(BlockPos pos, Level level)
    {
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof GeneratorBlockEntity ||
            be instanceof TowerBaseBE ||
            be instanceof TowerBlockEntity)
        {
            return true;
        }

        return false;
    }

    public static void carefulPlace(BlockPos pos, Block buildingBlock, Level level)
    {
        if (!isModdedBE(pos, level))
        {
            level.setBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), buildingBlock.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    public static void buildKingFloor(KingBlockEntity king, Block buildingBlockA, Block buildingBlockB)
    {
        BlockPos pos = king.getBlockPos();
        Level level = king.getLevel();

        //Upper left origin (Ground)
        BlockPos ulo = new BlockPos(pos.getX() + 3, pos.getY() - 1, pos.getZ() + 3);

        BlockPos currentPos = ulo;
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                if ((i + j) % 2 == 0)
                {
                    level.setBlock(currentPos, buildingBlockA.defaultBlockState(), Block.UPDATE_ALL);
                }
                else
                {
                    level.setBlock(currentPos, buildingBlockB.defaultBlockState(), Block.UPDATE_ALL);
                }
                currentPos = new BlockPos(currentPos.getX() - 1, currentPos.getY(), currentPos.getZ());
            }

            currentPos = new BlockPos(ulo.getX(), currentPos.getY(), currentPos.getZ() - 1);
        }
    }

    public static void buildKingHallwayFloor(KingBlockEntity king, Block buildingBlockA, Block buildingBlockB, int width, int length)
    {
        //Lower left origin (Ground)
        BlockPos pos = king.getBlockPos();
        Level level = king.getLevel();

        BlockPos center = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() + 4);

        BlockPos currentPos = new BlockPos(center.getX() + width/2, center.getY(), center.getZ());
        for (int i = 0; i < length; i++)
        {
            for (int j = 0; j < width; j++)
            {
                carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ()), Blocks.AIR, level);
                carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY()+1, currentPos.getZ()), Blocks.AIR, level);
                carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY()+2, currentPos.getZ()), Blocks.AIR, level);
                carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY()+3, currentPos.getZ()), Blocks.AIR, level);


                if ((i + j) % 2 == 0)
                {
                    carefulPlace(currentPos, buildingBlockA, level);
                }
                else
                {
                    carefulPlace(currentPos, buildingBlockB, level);
                }

                currentPos = new BlockPos(currentPos.getX() - 1, currentPos.getY(), currentPos.getZ());
            }
            currentPos = new BlockPos(center.getX() + width/2, currentPos.getY(), currentPos.getZ() + 1);
        }
    }

    public static void buildKingHallwayWalls(KingBlockEntity king, Block buildingBlock, int width, int length)
    {
        BlockPos pos = king.getBlockPos();
        Level level = king.getLevel();

        BlockPos center = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 4);

        BlockPos currentPos = new BlockPos(center.getX() + width/2, center.getY(), center.getZ());
        for (int i = 0; i < (width/2) - 3; i++)
        {
            carefulPlace(new BlockPos(center.getX() + width/2 - i, currentPos.getY(), currentPos.getZ()), buildingBlock, level);
            carefulPlace(new BlockPos(center.getX() + width/2 - i, currentPos.getY()+1, currentPos.getZ()), buildingBlock, level);
            carefulPlace(new BlockPos(center.getX() + width/2 - i, currentPos.getY()+2, currentPos.getZ()), buildingBlock, level);

            carefulPlace(new BlockPos(center.getX() - width/2 + i, currentPos.getY(), currentPos.getZ()), buildingBlock, level);
            carefulPlace(new BlockPos(center.getX() - width/2 + i, currentPos.getY()+1, currentPos.getZ()), buildingBlock, level);
            carefulPlace(new BlockPos(center.getX() - width/2 + i, currentPos.getY()+2, currentPos.getZ()), buildingBlock, level);

        }
        for (int i = 0; i < length+1; i++)
        {
            carefulPlace(currentPos, buildingBlock, level);
            carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY()+1, currentPos.getZ()), buildingBlock, level);
            carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY()+2, currentPos.getZ()), buildingBlock, level);

            carefulPlace(new BlockPos(center.getX() - width/2, currentPos.getY(), currentPos.getZ()), buildingBlock, level);
            carefulPlace(new BlockPos(center.getX() - width/2, currentPos.getY()+1, currentPos.getZ()), buildingBlock, level);
            carefulPlace(new BlockPos(center.getX() - width/2, currentPos.getY()+2, currentPos.getZ()), buildingBlock, level);

            currentPos = new BlockPos(center.getX() + width/2, currentPos.getY(), currentPos.getZ()+1);
        }
    }
    public static void buildKingJail(KingBlockEntity king, Block buildingBlock)
    {
        BlockPos pos = king.getBlockPos();
        Level level = king.getLevel();

        //Upper left origin (Ground)
        BlockPos ulo = new BlockPos(pos.getX() + 3, pos.getY(), pos.getZ() + 3);

        // X-
        BlockPos currentPos = ulo;
        for (int i = 0; i < 7; i++)
        {
            if (i < 2 || i > 4)
            {
                carefulPlace(currentPos, buildingBlock, level);
                carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ()), buildingBlock, level);
                carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY() + 2, currentPos.getZ()), buildingBlock, level);
            }
            else
            {
                carefulPlace(currentPos, Blocks.AIR, level);
                carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ()), Blocks.AIR, level);
                carefulPlace(new BlockPos(currentPos.getX(), currentPos.getY() + 2, currentPos.getZ()), Blocks.AIR, level);

            }

            currentPos = new BlockPos(currentPos.getX() - 1, currentPos.getY(), currentPos.getZ());
        }

        // Z-

        currentPos = new BlockPos(ulo.getX(), ulo.getY(), ulo.getZ() - 1);
        for (int i = 0; i < 6; i++)
        {
            level.setBlock(currentPos, buildingBlock.defaultBlockState(), Block.UPDATE_ALL);
            level.setBlock(new BlockPos(currentPos.getX(), currentPos.getY()+1, currentPos.getZ()), buildingBlock.defaultBlockState(), Block.UPDATE_ALL);
            level.setBlock(new BlockPos(currentPos.getX(), currentPos.getY()+2, currentPos.getZ()), buildingBlock.defaultBlockState(), Block.UPDATE_ALL);


            currentPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ() - 1);
        }

        //Lower right origin
        BlockPos lro = new BlockPos(pos.getX() - 3, pos.getY(), pos.getZ() - 3);
        currentPos = lro;

        // Z+
        for (int i = 0; i < 6; i++)
        {
            level.setBlock(currentPos, buildingBlock.defaultBlockState(), Block.UPDATE_ALL);
            level.setBlock(new BlockPos(currentPos.getX(), currentPos.getY()+1, currentPos.getZ()), buildingBlock.defaultBlockState(), Block.UPDATE_ALL);
            level.setBlock(new BlockPos(currentPos.getX(), currentPos.getY()+2, currentPos.getZ()), buildingBlock.defaultBlockState(), Block.UPDATE_ALL);

            currentPos = new BlockPos(currentPos.getX(), currentPos.getY(), currentPos.getZ() + 1);
        }

        // X+
        currentPos = new BlockPos(lro.getX() + 1, lro.getY(), lro.getZ());
        for (int i = 0; i < 5; i++)
        {

            level.setBlock(currentPos, buildingBlock.defaultBlockState(), Block.UPDATE_ALL);
            level.setBlock(new BlockPos(currentPos.getX(), currentPos.getY()+1, currentPos.getZ()), buildingBlock.defaultBlockState(), Block.UPDATE_ALL);
            level.setBlock(new BlockPos(currentPos.getX(), currentPos.getY()+2, currentPos.getZ()), buildingBlock.defaultBlockState(), Block.UPDATE_ALL);

            currentPos = new BlockPos(currentPos.getX() + 1, currentPos.getY(), currentPos.getZ());
        }
    }


}
