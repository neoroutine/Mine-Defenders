package neoroutine.minetd.common.setup;

import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.blockentity.king.KingBlock;
import neoroutine.minetd.common.blocks.blockentity.king.KingBlockEntity;
import neoroutine.minetd.common.blocks.blockentity.king.KingContainer;
import neoroutine.minetd.common.blocks.generators.minigenerator.MiniGenerator;
import neoroutine.minetd.common.blocks.generators.minigenerator.MiniGeneratorBE;
import neoroutine.minetd.common.blocks.generators.minigenerator.MiniGeneratorContainer;
import neoroutine.minetd.common.blocks.towerbase.TowerBase;
import neoroutine.minetd.common.blocks.towerbase.TowerBaseBE;
import neoroutine.minetd.common.blocks.towerbase.TowerBaseContainer;
import neoroutine.minetd.common.blocks.towers.pawn.Pawn;
import neoroutine.minetd.common.blocks.towers.pawn.PawnBE;
import neoroutine.minetd.common.blocks.towers.pawn.PawnContainer;
import neoroutine.minetd.common.blocks.towers.rook.Rook;
import neoroutine.minetd.common.blocks.towers.rook.RookBE;
import neoroutine.minetd.common.blocks.towers.rook.RookContainer;
import neoroutine.minetd.common.entities.antiking.AntikingEntity;
import neoroutine.minetd.common.items.TowerAnalyzer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Registration
{
    //Registries
    public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MineTD.MODID);

    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MineTD.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MineTD.MODID);
    public static final DeferredRegister<MenuType<?>> CONTAINER_REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, MineTD.MODID);

    public static final DeferredRegister<EntityType<?>> ENTITY_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, MineTD.MODID);

    //Creative mod tab
    public static final String TAB_NAME = "MineTD";

    public static final CreativeModeTab MODE_TAB = new CreativeModeTab(TAB_NAME)
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(Items.BELL);
        }
    };


    //Block/Item generic properties
    public static final BlockBehaviour.Properties BLOCK_PROPERTIES = BlockBehaviour.Properties.of(Material.METAL).strength(2f).requiresCorrectToolForDrops();
    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(MODE_TAB);

    //MINIGENERATOR (BECS)
    public static final RegistryObject<MiniGenerator> MINIGENERATOR                     = registerBlock("minigenerator", MiniGenerator::new);
    public static final RegistryObject<Item> MINIGENERATOR_ITEM                         = registerItem(MINIGENERATOR);
    public static final RegistryObject<BlockEntityType<MiniGeneratorBE>> MINIGERATOR_BE = registerBlockEntity("minigenerator",
            MiniGeneratorBE::new,
            MINIGENERATOR);

    public static final Supplier<MenuType<MiniGeneratorContainer>> MINIGENERATOR_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new MiniGeneratorContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<MiniGeneratorContainer>> MINIGENERATOR_CONTAINER = registerContainer("minigenerator", MINIGENERATOR_CONTAINER_SUPPLIER);


    //TOWER BASE (BECS)
    public static final RegistryObject<TowerBase> TOWER_BASE     = registerBlock("tower_base", TowerBase::new);
    public static final RegistryObject<Item> TOWER_BASE_ITEM = registerItem(TOWER_BASE);
    public static final RegistryObject<BlockEntityType<TowerBaseBE>> TOWER_BASE_BE = registerBlockEntity("tower_base",
                                                                                                         TowerBaseBE::new,
                                                                                                         TOWER_BASE);
    public static final Supplier<MenuType<TowerBaseContainer>> TOWER_BASE_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new TowerBaseContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<TowerBaseContainer>> TOWER_BASE_CONTAINER = registerContainer("tower_base", TOWER_BASE_CONTAINER_SUPPLIER);

    //TOWER BASE FRAGMENT (ITEM)
    public static final RegistryObject<Item> TOWER_BASE_FRAGMENT = registerItem("tower_base_fragment");

    //TOWER ANALYZER (ITEM)
    public static final RegistryObject<TowerAnalyzer> TOWER_ANALYZER = registerItem("tower_analyzer", TowerAnalyzer::new);

    //PAWN (BECS)
    public static final RegistryObject<Pawn> PAWN = registerBlock("pawn", Pawn::new);
    public static final RegistryObject<Item> PAWN_ITEM = registerItem(PAWN);
    public static final RegistryObject<BlockEntityType<PawnBE>> PAWN_BE = registerBlockEntity("pawn", PawnBE::new, PAWN);

    public static final Supplier<MenuType<PawnContainer>> PAWN_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new PawnContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<PawnContainer>> PAWN_CONTAINER = registerContainer("pawn", PAWN_CONTAINER_SUPPLIER);

    //ROOK (BECS)
    public static final RegistryObject<Rook> ROOK = registerBlock("rook", Rook::new);
    public static final RegistryObject<Item> ROOK_ITEM = registerItem(ROOK);
    public static final RegistryObject<BlockEntityType<RookBE>> ROOK_BE = registerBlockEntity("rook", RookBE::new, ROOK);

    public static final Supplier<MenuType<RookContainer>> ROOK_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new RookContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<RookContainer>> ROOK_CONTAINER = registerContainer("rook", ROOK_CONTAINER_SUPPLIER);

    //KING (BECS)
    public static final RegistryObject<KingBlock> KING = registerBlock("king", KingBlock::new);
    public static final RegistryObject<Item> KING_ITEM = registerItem(KING);
    public static final RegistryObject<BlockEntityType<KingBlockEntity>> KING_BE = registerBlockEntity("king", KingBlockEntity::new, KING);

    public static final Supplier<MenuType<KingContainer>> KING_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new KingContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<KingContainer>> KING_CONTAINER = registerContainer("king", KING_CONTAINER_SUPPLIER);

    //TODO:Implement artificial veins later on in chess theme ??
    //public static final RegistryObject<Block> ARTIFICIAL_VEINS     = registerBlock("artificial_veins");
    //public static final RegistryObject<Item> ARTIFICIAL_VEINS_ITEM = registerItem(ARTIFICIAL_VEINS);

    //ANTIKING (ENTITY)
    public static final RegistryObject<EntityType<AntikingEntity>> ANTIKING = registerEntity("antiking", AntikingEntity::new, MobCategory.MONSTER);
    public static final RegistryObject<Item> ANTIKING_EGG = registerEntityEgg("antiking", ANTIKING);

    //LABYRINTH GLASS (BLOCK)
    public static final RegistryObject<Block> LABYRINTH_GLASS = registerBlock("labyrinth_glass", () -> new GlassBlock(BLOCK_PROPERTIES)
    {
        @Override
        public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos position)
        {
            return Shapes.box(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);
        }
    });
    public static final RegistryObject<Item> LABYRINTH_GLASS_ITEM = registerItem(LABYRINTH_GLASS);

    public static void register()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCK_REGISTER.register(bus);
        ITEM_REGISTER.register(bus);
        BLOCK_ENTITY_REGISTER.register(bus);
        CONTAINER_REGISTER.register(bus);
        ENTITY_REGISTER.register(bus);
    }


    //Helper methods


    public static RegistryObject<Block> registerBlock(String name)
    {
        return BLOCK_REGISTER.register(name, () -> new Block(BLOCK_PROPERTIES));
    }


    public static <B extends Block> RegistryObject<B> registerBlock(String name, Supplier<B> supplier)
    {
        return BLOCK_REGISTER.register(name, supplier);
    }

    public static RegistryObject<Item> registerItem(String itemName)
    {
        return ITEM_REGISTER.register(itemName, () -> new Item(ITEM_PROPERTIES));
    }

    public static <I extends Item> RegistryObject<I> registerItem(String name, Supplier<I> supplier)
    {
        return ITEM_REGISTER.register(name, supplier);
    }

    public static <B extends Block> RegistryObject<Item> registerItem(RegistryObject<B> block)
    {
        return ITEM_REGISTER.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }

    public static <BE extends BlockEntity, B extends Block> RegistryObject<BlockEntityType<BE>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<BE> supplier, RegistryObject<B> block)
    {
       return BLOCK_ENTITY_REGISTER.register(name, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
    }

    public static <C extends AbstractContainerMenu, M extends MenuType<C>> RegistryObject<MenuType<C>> registerContainer(String name, Supplier<M> supplier)
    {
        return CONTAINER_REGISTER.register(name, supplier);
    }

    public static <E extends Entity> RegistryObject<EntityType<E>> registerEntity(String name, EntityType.EntityFactory<E> supplier, MobCategory category)
    {
        return ENTITY_REGISTER.register(name, () -> EntityType.Builder.of(supplier, category)
                .sized(0.6f, 1.95f)
                .clientTrackingRange(8)
                .setShouldReceiveVelocityUpdates(false)
                .build(name));
    }

    public static <E extends Mob> RegistryObject<Item> registerEntityEgg(String name, RegistryObject<EntityType<E>> registeredEntity)
    {
        return ITEM_REGISTER.register(name, () -> new ForgeSpawnEggItem(registeredEntity, 0x000000, 0xff0000, ITEM_PROPERTIES));
    }

}
