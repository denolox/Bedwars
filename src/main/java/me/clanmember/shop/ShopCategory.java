package me.clanmember.shop;

public enum ShopCategory {
    BLOCKS("Блоки"),
    WEAPONS("Оружие"),
    ARMOR("Броня"),
    TOOLS("Инструменты"),
    BOWS("Луки"),
    POTIONS("Зелья"),
    SPECIAL("Особое");

    private final String displayName;

    ShopCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
