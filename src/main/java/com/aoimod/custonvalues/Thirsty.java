package com.aoimod.custonvalues;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class Thirsty {
    private float thirst = 0.0F;
    private float maxThirst = 20.0F;

    public float getThirst() {
        return thirst;
    }

    public void addThirsty(float thirst) {
        this.thirst = MathHelper.clamp(this.thirst + thirst, 0.0F, maxThirst);
    }

    public void removeThirst(float thirst) {
        this.thirst = MathHelper.clamp(this.thirst - thirst, 0.0F, maxThirst);
    }

    public void setThirst(float thirst) {
        this.thirst = MathHelper.clamp(thirst, 0.0F, maxThirst);
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putFloat("thirst", thirst);
    }

    public void readNbt(NbtCompound nbt) {
        thirst = nbt.getFloat("thirst");
    }
}
