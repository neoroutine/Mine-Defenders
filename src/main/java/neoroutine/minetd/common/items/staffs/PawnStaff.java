package neoroutine.minetd.common.items.staffs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class PawnStaff extends Staff
{
    public PawnStaff()
    {
        super();
        setReach(1);
        setDamage(10);
        setDelay(100);
    }

}
