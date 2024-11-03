package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record LearnEnchantment(Identifier enchantment) implements CustomPayload {
    public static final CustomPayload.Id<LearnEnchantment> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "learn_enchantment"));
    public static final PacketCodec<RegistryByteBuf, LearnEnchantment> PACKET_CODEC = PacketCodec.of(LearnEnchantment::write, LearnEnchantment::new);

    public LearnEnchantment(RegistryByteBuf buf) {
        this(buf.readIdentifier());
    }

    public void write(RegistryByteBuf buf) {
        buf.writeIdentifier(enchantment);
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
