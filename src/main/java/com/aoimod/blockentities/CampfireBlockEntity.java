package com.aoimod.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CampfireBlockEntity extends BlockEntity {
    public record Packet(List<ItemStack> stacks, BlockPos pos) {
            public Packet(List<ItemStack> stacks, BlockPos pos) {
                this.stacks = stacks.stream().filter(stack -> !stack.isEmpty()).toList();
                this.pos = pos;
            }

            public static Packet create(RegistryByteBuf buf) {
                BlockPos pos = buf.readBlockPos();
                ArrayList<ItemStack> twigs = new ArrayList<>();
                int size = buf.readInt();
                for (int i = 0; i < size; i++) {
                    twigs.add(ItemStack.PACKET_CODEC.decode(buf));
                }

                return new Packet(twigs, pos);
            }

            public void write(RegistryByteBuf buf) {
                buf.writeBlockPos(pos);
                buf.writeInt(stacks.size());
                for (var stack : stacks) {
                    ItemStack.PACKET_CODEC.encode(buf, stack);
                }
            }
        }

    public static final PacketCodec<RegistryByteBuf, Packet> PACKET_CODEC =
            new PacketCodec<>() {
                public Packet decode(RegistryByteBuf buf) { return Packet.create(buf); }
                public void encode(RegistryByteBuf buf, Packet packet) { packet.write(buf); }
            };

    private final DefaultedList<ItemStack> twigs = DefaultedList.ofSize(4, ItemStack.EMPTY);

    public CampfireBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public List<ItemStack> getTwigs() {
        return twigs;
    }

    public void addTwigs(ItemStack stack) {
        for (int i=0; i<twigs.size(); i++) {
            if (twigs.get(i).isEmpty() && stack.getCount() != 0)
                twigs.set(i, stack.split(1));
        }
    }

    public Packet createPacket() {
        System.out.printf("Server end: %s%n", twigs);
        return new Packet(twigs, pos);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList list = new NbtList();
        twigs.stream()
                .filter(stack -> !stack.isEmpty())
                .map(stack -> stack.toNbt(registries))
                .forEach(list::add);
        nbt.put("stacks", list);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        List<ItemStack> stacks = ((NbtList) Objects.requireNonNull(nbt.get("stacks"))).stream()
                .map(stack -> ItemStack.fromNbt(registries, stack).get())
                .toList();

        twigs.clear();
        for (int i=0; i<stacks.size(); i++) {
            twigs.set(i, stacks.get(i));
        }
    }
}
