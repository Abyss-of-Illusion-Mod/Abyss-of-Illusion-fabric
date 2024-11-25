package com.aoimod.blockentities;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blocks.Campfire;
import com.aoimod.networking.packet.CampfireDataS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CampfireBlockEntity extends BlockEntity {
    public record Packet(List<ItemStack> stacks, BlockPos pos) {
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
        ServerPlayConnectionEvents.JOIN.register(
                Identifier.of(AbyssOfIllusionMod.MOD_ID, "init_campfire"),
                (handler, sender, server) -> {
                    server.execute(() -> {
                        ServerPlayNetworking.send(handler.player, new CampfireDataS2CPacket(createPacket()));
                    });
                });
    }

    public List<ItemStack> getTwigs() {
        return twigs.stream().filter(stack -> !stack.isEmpty()).toList();
    }

    public void addTwigs(ItemStack stack) {
        for (int i=0; i<twigs.size() && stack.getCount() != 0; i++) {
            if (twigs.get(i).isEmpty()) {
                twigs.set(i, stack.split(1));
            }
        }
    }

    public Packet createPacket() {
        return new Packet(getTwigs(), pos);
    }

    public void setStack(List<ItemStack> stacks) {
        twigs.clear();
        for (int i=0; i<stacks.size(); i++) {
            twigs.set(i, stacks.get(i));
        }
    }

    public void updateState(World world) {
        world.setBlockState(pos,
                getCachedState().with(Campfire.TWIGS, getTwigs().size()));
    }

    public void dropItems(World world) {
        for (var stack: twigs) {
            if (!stack.isEmpty())
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
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
