package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncStatusHud(Type type) implements CustomPayload {
    public static final CustomPayload.Id<SyncStatusHud> PACKET_ID = new CustomPayload.Id<>(new Identifier(GreatBigWorld.NAMESPACE, "sync_status_hud"));
    public static final PacketCodec<RegistryByteBuf, SyncStatusHud> PACKET_CODEC = PacketCodec.of(SyncStatusHud::write, SyncStatusHud::new);

    public SyncStatusHud(RegistryByteBuf buf) {
        this(Type.values()[buf.readByte()]);
    }

    public void write(RegistryByteBuf buf) {
        buf.writeByte((byte) type.ordinal());
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public enum Type {
        HEALTH, FOOD, ARMOR, EXPERIENCE
    }
}
