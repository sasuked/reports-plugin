package io.github.sasuked.reportsplugin.util;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder {

    private final ItemStack itemStack;

    private final ItemMeta itemMeta;



    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }


    public ItemBuilder type(Material type) {
        itemStack.setType(type);
        return this;
    }


    public ItemBuilder name(String name) {
        itemMeta.setDisplayName(name.replace("&", "§"));
        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder lore(List<String> lore) {

        itemMeta.setLore(lore.stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);


        return this;
    }

    public ItemBuilder lore(String... lore) {
        return lore(Lists.newArrayList(lore));
    }

    public ItemBuilder addLore(String... lore) {
        String[] additionalLore = Arrays.stream(lore)
          .map(it -> ChatColor.translateAlternateColorCodes('&', it))
          .toArray(String[]::new);


        List<String> actualLore = itemMeta.getLore();
        if (actualLore == null) {
            itemMeta.setLore(Arrays.asList(additionalLore));
        } else {
            actualLore.addAll(Arrays.asList(additionalLore));
            itemMeta.setLore(actualLore);
        }


        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder owner(UUID owner) {
        Material type = itemStack.getType();
        if (type != Material.SKULL) {
            type = Material.SKULL;
            itemStack.setType(type);
        }


        SkullMeta skullMeta = (SkullMeta) itemMeta;

        OfflinePlayer offlinePlayer = PlayerProvider.getOfflinePlayer(owner);
        if (offlinePlayer != null) {
            skullMeta.setOwner(offlinePlayer.getName());
        }

        itemStack.setItemMeta(skullMeta);

        return this;
    }

    public ItemBuilder flag(ItemFlag... itemFlags) {
        itemMeta.addItemFlags(itemFlags);
        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int value) {
        itemStack.addUnsafeEnchantment(enchantment, value);
        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }

}