package neoroutine.minetd.common.blocks.blockentity.king;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class KingHealth implements INBTSerializable<Tag>
{
    private int maxHealth = 100;
    private int health = 100;

    public KingHealth()
    {
        super();
    }

    void setMaxHealth(int value) { this.maxHealth = value;}
    int getMaxHealth() { return this.maxHealth; }

    void setHealth(int value) { this.health = value;}
    int getHealth() { return this.health; }


    protected void onUpdate() {}

    @Override
    public Tag serializeNBT()
    {
        CompoundTag health = new CompoundTag();
        health.putInt("kinghealth", this.health);

        return health;
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        CompoundTag cnbt = (CompoundTag)nbt;
        int health = cnbt.getInt("kinghealth");

        this.health = health;
        onUpdate();
    }
}