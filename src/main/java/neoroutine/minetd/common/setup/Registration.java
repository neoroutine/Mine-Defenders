package neoroutine.minetd.common.setup;

import neoroutine.minetd.MineTD;
import neoroutine.minetd.common.blocks.generators.densegenerator.DenseGenerator;
import neoroutine.minetd.common.blocks.generators.densegenerator.DenseGeneratorBlockEntity;
import neoroutine.minetd.common.blocks.generators.densegenerator.DenseGeneratorContainer;
import neoroutine.minetd.common.blocks.generators.neutrongenerator.NeutronGenerator;
import neoroutine.minetd.common.blocks.generators.neutrongenerator.NeutronGeneratorBlockEntity;
import neoroutine.minetd.common.blocks.generators.neutrongenerator.NeutronGeneratorContainer;
import neoroutine.minetd.common.blocks.generators.softgenerator.SoftGenerator;
import neoroutine.minetd.common.blocks.generators.softgenerator.SoftGeneratorBlockEntity;
import neoroutine.minetd.common.blocks.generators.softgenerator.SoftGeneratorContainer;
import neoroutine.minetd.common.blocks.kings.black.BlackKingBlock;
import neoroutine.minetd.common.blocks.kings.black.BlackKingBlockEntity;
import neoroutine.minetd.common.blocks.kings.black.BlackKingContainer;
import neoroutine.minetd.common.blocks.kings.white.WhiteKingBlock;
import neoroutine.minetd.common.blocks.kings.white.WhiteKingBlockEntity;
import neoroutine.minetd.common.blocks.kings.white.WhiteKingContainer;
import neoroutine.minetd.common.blocks.towerbase.TowerBase;
import neoroutine.minetd.common.blocks.towerbase.TowerBaseBE;
import neoroutine.minetd.common.blocks.towerbase.TowerBaseContainer;
import neoroutine.minetd.common.blocks.towers.bishop.BishopBlock;
import neoroutine.minetd.common.blocks.towers.bishop.BishopBlockEntity;
import neoroutine.minetd.common.blocks.towers.bishop.BishopContainer;
import neoroutine.minetd.common.blocks.towers.knight.KnightBlock;
import neoroutine.minetd.common.blocks.towers.knight.KnightBlockEntity;
import neoroutine.minetd.common.blocks.towers.knight.KnightContainer;
import neoroutine.minetd.common.blocks.towers.pawn.PawnBlock;
import neoroutine.minetd.common.blocks.towers.pawn.PawnBlockEntity;
import neoroutine.minetd.common.blocks.towers.pawn.PawnContainer;
import neoroutine.minetd.common.blocks.towers.queen.QueenBlock;
import neoroutine.minetd.common.blocks.towers.queen.QueenBlockEntity;
import neoroutine.minetd.common.blocks.towers.queen.QueenContainer;
import neoroutine.minetd.common.blocks.towers.rook.RookBlock;
import neoroutine.minetd.common.blocks.towers.rook.RookBlockEntity;
import neoroutine.minetd.common.blocks.towers.rook.RookContainer;
import neoroutine.minetd.common.entities.antiking.AntikingEntity;
import neoroutine.minetd.common.items.TowerAnalyzer;
import neoroutine.minetd.common.items.staffs.*;
import neoroutine.minetd.common.items.swords.KingMarch;
import neoroutine.minetd.common.items.swords.QueenGambit;
import neoroutine.minetd.common.items.swords.SicilianDefense;
import neoroutine.minetd.common.items.swords.Sword;
import net.minecraft.core.BlockPos;
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
            return new ItemStack(Registration.PAWN.get());
        }
    };


    //Block/Item generic properties
    public static final BlockBehaviour.Properties BLOCK_PROPERTIES = BlockBehaviour.Properties.of(Material.METAL).strength(2f).requiresCorrectToolForDrops();
    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(MODE_TAB);

    //SOFT_GENERATOR (BECS)
    public static final RegistryObject<SoftGenerator> SOFT_GENERATOR = registerBlock("soft_generator", SoftGenerator::new);
    public static final RegistryObject<Item> SOFT_GENERATOR_ITEM = registerItem(SOFT_GENERATOR);
    public static final RegistryObject<BlockEntityType<SoftGeneratorBlockEntity>> SOFT_GENERATOR_BE = registerBlockEntity("soft_generator",
            SoftGeneratorBlockEntity::new,
            SOFT_GENERATOR);

    public static final Supplier<MenuType<SoftGeneratorContainer>> SOFT_GENERATOR_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new SoftGeneratorContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<SoftGeneratorContainer>> SOFT_GENERATOR_CONTAINER = registerContainer("soft_generator", SOFT_GENERATOR_CONTAINER_SUPPLIER);

    //DENSE GENERATOR (BECS)
    public static final RegistryObject<DenseGenerator> DENSE_GENERATOR = registerBlock("dense_generator", DenseGenerator::new);
    public static final RegistryObject<Item> DENSE_GENERATOR_ITEM = registerItem(DENSE_GENERATOR);
    public static final RegistryObject<BlockEntityType<DenseGeneratorBlockEntity>> DENSE_GENERATOR_BE = registerBlockEntity("dense_generator",
            DenseGeneratorBlockEntity::new,
            DENSE_GENERATOR);


    public static final Supplier<MenuType<DenseGeneratorContainer>> DENSE_GENERATOR_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new DenseGeneratorContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<DenseGeneratorContainer>> DENSE_GENERATOR_CONTAINER = registerContainer("dense_generator", DENSE_GENERATOR_CONTAINER_SUPPLIER);

    //NEUTRON GENERATOR (BECS)
    public static final RegistryObject<NeutronGenerator> NEUTRON_GENERATOR = registerBlock("neutron_generator", NeutronGenerator::new);
    public static final RegistryObject<Item> NEUTRON_GENERATOR_ITEM = registerItem(NEUTRON_GENERATOR);
    public static final RegistryObject<BlockEntityType<NeutronGeneratorBlockEntity>> NEUTRON_GENERATOR_BE = registerBlockEntity("neutron_generator",
            NeutronGeneratorBlockEntity::new,
            NEUTRON_GENERATOR);


    public static final Supplier<MenuType<NeutronGeneratorContainer>> NEUTRON_GENERATOR_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new NeutronGeneratorContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<NeutronGeneratorContainer>> NEUTRON_GENERATOR_CONTAINER = registerContainer("neutron_generator", NEUTRON_GENERATOR_CONTAINER_SUPPLIER);


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

    //PAWN MOVE (ITEM)
    public static final RegistryObject<Item> PAWN_MOVE = registerItem("pawn_move");

    //ROOK MOVE (ITEM)
    public static final RegistryObject<Item> ROOK_MOVE = registerItem("rook_move");

    //KNIGHT MOVE (ITEM)
    public static final RegistryObject<Item> KNIGHT_MOVE = registerItem("knight_move");

    //BISHOP MOVE (ITEM)
    public static final RegistryObject<Item> BISHOP_MOVE = registerItem("bishop_move");

    //QUEEN MOVE (ITEM)
    public static final RegistryObject<Item> QUEEN_MOVE = registerItem("queen_move");

    //STAFFS (ITEM)
    public static final RegistryObject<PawnStaff> PAWN_STAFF = registerItem("pawn_staff", PawnStaff::new);
    public static final RegistryObject<RookStaff> ROOK_STAFF = registerItem("rook_staff", RookStaff::new);
    public static final RegistryObject<KnightStaff> KNIGHT_STAFF = registerItem("knight_staff", KnightStaff::new);
    public static final RegistryObject<BishopStaff> BISHOP_STAFF = registerItem("bishop_staff", BishopStaff::new);
    public static final RegistryObject<QueenStaff> QUEEN_STAFF = registerItem("queen_staff", QueenStaff::new);

    //SWORDS
    public static final RegistryObject<Item> KING_MARCH       = registerItem("king_march", KingMarch::new);
    public static final RegistryObject<Item> SICILIAN_DEFENSE = registerItem("sicilian_defense", SicilianDefense::new);
    public static final RegistryObject<Item> QUEEN_GAMBIT     = registerItem("queen_gambit", QueenGambit::new);

    //SOFT COAL (ITEM)
    public static final RegistryObject<Item> SOFT_COAL = registerItem("soft_coal");

    //DENSE COAL (ITEM)
    public static final RegistryObject<Item> DENSE_COAL = registerItem("dense_coal");

    //NEUTRON COAL (ITEM)
    public static final RegistryObject<Item> NEUTRON_COAL = registerItem("neutron_coal");

    //PAWN (BECS)
    public static final RegistryObject<PawnBlock> PAWN = registerBlock("pawn", PawnBlock::new);
    public static final RegistryObject<Item> PAWN_ITEM = registerItem(PAWN);
    public static final RegistryObject<BlockEntityType<PawnBlockEntity>> PAWN_BE = registerBlockEntity("pawn", PawnBlockEntity::new, PAWN);

    public static final Supplier<MenuType<PawnContainer>> PAWN_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new PawnContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<PawnContainer>> PAWN_CONTAINER = registerContainer("pawn", PAWN_CONTAINER_SUPPLIER);

    //KNIGHT (BECS)
    public static final RegistryObject<KnightBlock> KNIGHT = registerBlock("knight", KnightBlock::new);
    public static final RegistryObject<Item> KNIGHT_ITEM = registerItem(KNIGHT);
    public static final RegistryObject<BlockEntityType<KnightBlockEntity>> KNIGHT_BE = registerBlockEntity("knight", KnightBlockEntity::new, KNIGHT);

    public static final Supplier<MenuType<KnightContainer>> KNIGHT_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new KnightContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<KnightContainer>> KNIGHT_CONTAINER = registerContainer("knight", KNIGHT_CONTAINER_SUPPLIER);

    //BISHOP (BECS)
    public static final RegistryObject<BishopBlock> BISHOP = registerBlock("bishop", BishopBlock::new);
    public static final RegistryObject<Item> BISHOP_ITEM = registerItem(BISHOP);
    public static final RegistryObject<BlockEntityType<BishopBlockEntity>> BISHOP_BE = registerBlockEntity("bishop", BishopBlockEntity::new, BISHOP);

    public static final Supplier<MenuType<BishopContainer>> BISHOP_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new BishopContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<BishopContainer>> BISHOP_CONTAINER = registerContainer("bishop", BISHOP_CONTAINER_SUPPLIER);

    //ROOK (BECS)
    public static final RegistryObject<RookBlock> ROOK = registerBlock("rook", RookBlock::new);
    public static final RegistryObject<Item> ROOK_ITEM = registerItem(ROOK);
    public static final RegistryObject<BlockEntityType<RookBlockEntity>> ROOK_BE = registerBlockEntity("rook", RookBlockEntity::new, ROOK);

    public static final Supplier<MenuType<RookContainer>> ROOK_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new RookContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<RookContainer>> ROOK_CONTAINER = registerContainer("rook", ROOK_CONTAINER_SUPPLIER);

    //QUEEN (BECS)
    public static final RegistryObject<QueenBlock> QUEEN = registerBlock("queen", QueenBlock::new);
    public static final RegistryObject<Item> QUEEN_ITEM = registerItem(QUEEN);
    public static final RegistryObject<BlockEntityType<QueenBlockEntity>> QUEEN_BE = registerBlockEntity("queen", QueenBlockEntity::new, QUEEN);

    public static final Supplier<MenuType<QueenContainer>> QUEEN_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new QueenContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<QueenContainer>> QUEEN_CONTAINER = registerContainer("queen", QUEEN_CONTAINER_SUPPLIER);

    //BLACK KING (BECS)
    public static final RegistryObject<BlackKingBlock> BLACK_KING = registerBlock("black_king", BlackKingBlock::new);
    public static final RegistryObject<Item> BLACK_KING_ITEM = registerItem(BLACK_KING);
    public static final RegistryObject<BlockEntityType<BlackKingBlockEntity>> BLACK_KING_BE = registerBlockEntity("black_king", BlackKingBlockEntity::new, BLACK_KING);

    public static final Supplier<MenuType<BlackKingContainer>> BLACK_KING_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new BlackKingContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<BlackKingContainer>> BLACK_KING_CONTAINER = registerContainer("king", BLACK_KING_CONTAINER_SUPPLIER);

    //WHITE KING (BECS)
    public static final RegistryObject<WhiteKingBlock> WHITE_KING = registerBlock("white_king", WhiteKingBlock::new);
    public static final RegistryObject<Item> WHITE_KING_ITEM = registerItem(WHITE_KING);
    public static final RegistryObject<BlockEntityType<WhiteKingBlockEntity>> WHITE_KING_BE = registerBlockEntity("white_king", WhiteKingBlockEntity::new, WHITE_KING);

    public static final Supplier<MenuType<WhiteKingContainer>> WHITE_KING_CONTAINER_SUPPLIER = () ->
            IForgeMenuType.create((windowId, inv, data) -> new WhiteKingContainer(windowId, data.readBlockPos(), inv, inv.player));
    public static final RegistryObject<MenuType<WhiteKingContainer>> WHITE_KING_CONTAINER = registerContainer("white_king", WHITE_KING_CONTAINER_SUPPLIER);

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
