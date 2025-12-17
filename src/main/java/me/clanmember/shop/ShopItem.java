package me.clanmember.shop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ShopItem {
    private final String name;
    private final ItemStack item;
    private final Material currency;
    private final int price;
    private final ShopCategory category;

    public ShopItem(String name, ItemStack item, Material currency, int price, ShopCategory category) {
        this.name = name;
        this.item = item;
        this.currency = currency;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public Material getCurrency() {
        return currency;
    }

    public int getPrice() {
        return price;
    }

    public ShopCategory getCategory() {
        return category;
    }
}
