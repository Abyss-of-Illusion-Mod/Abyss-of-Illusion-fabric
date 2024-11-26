package com.aoimod.blockentities;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blocks.Campfire;
import com.aoimod.networking.packet.CampfireDataS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class CampfireBlockEntity extends BlockEntity implements RecipeInput {
    public record Packet(List<ItemStack> twigs, ItemStack twigResult, ItemStack fuel, List<ItemStack> inventory, BlockPos pos) {
            public static Packet create(RegistryByteBuf buf) {
                BlockPos pos = buf.readBlockPos();

                DefaultedList<ItemStack> twigs = DefaultedList.ofSize(buf.readInt(), ItemStack.EMPTY);
                twigs.replaceAll(ignored -> ItemStack.PACKET_CODEC.decode(buf));

                ItemStack twigResult = buf.readBoolean() ? ItemStack.PACKET_CODEC.decode(buf) : ItemStack.EMPTY;
                ItemStack fuel = buf.readBoolean() ? ItemStack.PACKET_CODEC.decode(buf) : ItemStack.EMPTY;

                DefaultedList<ItemStack> inventory = DefaultedList.ofSize(buf.readInt(), ItemStack.EMPTY);
                inventory.replaceAll(ignored -> ItemStack.PACKET_CODEC.decode(buf));

                return new Packet(twigs, twigResult, fuel, inventory, pos);
            }

            public void write(RegistryByteBuf buf) {
                buf.writeBlockPos(pos);
                List<ItemStack> stacks = twigs.stream().filter(stack -> !stack.isEmpty()).toList();
                List<ItemStack> result = inventory.stream().filter(stack -> !stack.isEmpty()).toList();
                buf.writeInt(stacks.size());
                for (var stack : stacks) {
                    ItemStack.PACKET_CODEC.encode(buf, stack);
                }

                buf.writeBoolean(!twigResult.isEmpty());
                if (!twigResult.isEmpty())
                    ItemStack.PACKET_CODEC.encode(buf, twigResult);

                buf.writeBoolean(!fuel.isEmpty());
                if (!fuel.isEmpty())
                    ItemStack.PACKET_CODEC.encode(buf, fuel);

                buf.writeInt(result.size());
                for (var stack: result) {
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
    private ItemStack twigResult = ItemStack.EMPTY;
    private ItemStack fuel = ItemStack.EMPTY;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public CampfireBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        ServerPlayConnectionEvents.JOIN.register(
                Identifier.of(AbyssOfIllusionMod.MOD_ID, "init_campfire"),
                (handler, sender, server) -> server.execute(() ->
                        ServerPlayNetworking.send(handler.player, new CampfireDataS2CPacket(createPacket()))));
    }

    public List<ItemStack> getTwigs() {
        return twigs;
    }

    public void addTwigs(ItemStack stack) {
        for (int i=0; i<twigs.size() && stack.getCount() != 0; i++) {
            if (twigs.get(i).isEmpty()) {
                twigs.set(i, stack.split(1));
            }
        }
    }

    public void setStack(List<ItemStack> stacks) {
        twigs.clear();
        for (int i=0; i<stacks.size(); i++) {
            twigs.set(i, stacks.get(i));
        }
    }

    public ItemStack setFuel(ItemStack fuel) {
        ItemStack result = this.fuel.copy();
        this.fuel = fuel;
        return result;
    }

    public ItemStack setItem(ItemStack item) {
        ItemStack result = this.inventory.getFirst().copy();
        this.inventory.set(0, item);
        return result;
    }

    public void loadPacket(Packet packet) {
        twigs.clear();
        for (int i=0, size=packet.twigs.size(); i<size; i++) {
            twigs.set(i, packet.twigs.get(i));
        }

        twigResult = packet.twigResult;
        fuel = packet.fuel;

        inventory.clear();
        for (int i=0, size=packet.inventory.size(); i<size; i++) {
            inventory.set(i, packet.inventory.get(i));
        }
    }

    public Packet createPacket() {
        return new Packet(twigs, twigResult, fuel, inventory, pos);
    }

    public void updateState(World world) {
        int twigSize = (int) getTwigs().stream().filter(stack -> !stack.isEmpty()).count();
        world.setBlockState(pos, getCachedState().with(Campfire.TWIGS, twigSize));
    }

    public void dropItems(World world) {
        for (var stack: twigs) {
            if (!stack.isEmpty())
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, CampfireBlockEntity blockEntity) {

    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getFirst();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList list = new NbtList();
        twigs.stream()
                .filter(stack -> !stack.isEmpty())
                .map(stack -> stack.toNbt(registries))
                .forEach(list::add);
        nbt.put("twigs", list);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        List<ItemStack> stacks = ((NbtList) Objects.requireNonNull(nbt.get("twigs"))).stream()
                .map(stack -> ItemStack.fromNbt(registries, stack).orElseThrow())
                .toList();

        twigs.clear();
        for (int i=0; i<stacks.size(); i++) {
            twigs.set(i, stacks.get(i));
        }
    }
}
