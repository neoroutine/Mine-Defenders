package neoroutine.minetd.common.items.staffs;

import neoroutine.minetd.common.grandmaster.EloRatingProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.EntityTeleportEvent;

import java.util.List;

import static neoroutine.minetd.common.setup.Registration.ITEM_PROPERTIES;

//Add ThrowableItemProjectile logic
public class Staff extends Item
{
    protected double reach = 0;
    protected int damage   = 0;
    protected int delay    = 0;

    public Staff()
    {
        super(ITEM_PROPERTIES);
    }

    public double getReach() {
        return reach;
    }

    public void setReach(double reach) {
        this.reach = reach;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        HitResult targeted = player.pick(20.0D, 0.0F, false);

        BlockPos targetedPos = targeted.getType() != HitResult.Type.BLOCK ? null : ((BlockHitResult) targeted).getBlockPos();
        player.getCooldowns().addCooldown(this, delay);
        if (!level.isClientSide() && targetedPos != null)
        {
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(targetedPos).inflate(reach));
            int killed = attack(entities);
            player.getCapability(EloRatingProvider.PLAYER_ELO_POINTS).ifPresent(capability ->
            {
                int earned = killed*2;
                capability.addPoints(player, earned);
                int points = capability.getPoints();
                String message = String.format("You killed %d enemies with your staff. Points +%d (%d)", killed, earned, points);
                if (earned > 0)
                {
                    player.displayClientMessage(new TranslatableComponent(message), true);
                }
            });
        }

        return super.use(level, player, hand);
    }

    int attack(List<Entity> entities)
    {
        int killed = 0;

        for (Entity entity: entities)
        {
            entity.hurt(DamageSource.MAGIC, damage);
            if (!entity.isAlive())
            {
                killed++;
            }
        }

        return killed;
    }
}
