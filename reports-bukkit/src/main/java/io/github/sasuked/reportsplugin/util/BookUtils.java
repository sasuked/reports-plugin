package io.github.sasuked.reportsplugin.util;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BookUtils {

    public static void openBook(
      @NotNull Player player,
      @NotNull ItemStack book
    ) {
        ItemStack itemInHand = player.getItemInHand();
        player.setItemInHand(book);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload(
          "MC|BOpen",
          new PacketDataSerializer(Unpooled.buffer())
        ));

        player.setItemInHand(itemInHand);
    }
}
