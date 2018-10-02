package com.github.maxopoly.angeliacore.block;

import com.github.maxopoly.angeliacore.model.item.Material;

public enum BlockState {
    AIR(Material.AIR, 0.0f, "air", "Air"),
    STONE(Material.STONE, 1.5f, "stone", "Stone"),
    GRANITE(Material.STONE, (byte)1, 1.5f, "granite", "Granite"),
    POLISHED_GRANITE(Material.STONE, (byte)2, 1.5f, "granite_smooth", "Polished Diorite"),
    DIORITE(Material.STONE, (byte)3, 1.5f, "diorite", "Diorite"),
    POLISHED_DIORITE(Material.STONE, (byte)4, 1.5f, "diorite_smooth", "Polished Diorite"),
    ANDESITE(Material.STONE, (byte)5, 1.5f, "andesite", "Andesite"),
    POLISHED_ANDESITE(Material.STONE, (byte)6, 1.5f, "andesite_smooth", "Polished Andesite"),
    GRASS(Material.GRASS, 0.6f, "grass", "Grass Block"),
    DIRT(Material.DIRT, 0.5f, "dirt", "Dirt"),
    COARSE_DIRT(Material.DIRT, (byte)1, 0.5f, "coarse_dirt", "Coarse Dirt"),
    PODZOL(Material.DIRT, (byte)2, 0.5f, "podzol", "Podzol"),
    COBBLESTONE(Material.COBBLESTONE, 2.0f, "cobblestone", "Cobblestone"),
    OBSIDIAN(Material.OBSIDIAN, 50.0f, "obsidian", "Obsidian"),
    WHITE_CARPET(Material.CARPET, 0.1f, "carpet", "White Carpet"),
    ;

    public static BlockState getStateById(int id) {
        return getStateByMaterial(Material.getByID(id));
    }

    public static BlockState getStateByData(int data) {
        int blockType = data >> 4;
        int blockMeta = data & 15;
        for (BlockState blockState : values()) {
            if (blockState.getType().getID() != blockType) {
                continue;
            }
            if (blockState.getMetadata() != blockMeta) {
                continue;
            }
            return blockState;
        }
        return BlockState.AIR;
    }

    public static BlockState getStateByMaterial(Material material) {
        for (BlockState blockState : values()) {
            if (blockState.getType().equals(material)) {
                return blockState;
            }
        }
        return BlockState.AIR;
    }

    private Material type;
    private byte metadata;
    private float hardness;
    private String textureSlug;
    private String niceName;

    BlockState(Material type, float hardness, String textureSlug, String niceName) {
        this(type, (byte)0, hardness, textureSlug, niceName);
    }

    BlockState(Material type, byte metadata, float hardness, String textureSlug, String niceName) {
        this.type = type;
        this.metadata = metadata;
        this.hardness = hardness;
        this.textureSlug = textureSlug;
        this.niceName = niceName;
    }

    public Material getType() {
        return type;
    }

    public byte getMetadata() {
        return metadata;
    }

    public double getHardness() {
        return hardness;
    }

    public String getNiceName() {
        return niceName;
    }

    public String getTexturePackIdentifier() {
        return textureSlug;
    }

    public boolean isFullBlock() {
        return type.isSolid();
    }

    public boolean isLiquid() {
        return type.equals(Material.LAVA) || type.equals(Material.WATER);
    }

    public boolean isOpaque() {
        return type.isOpaque();
    }

    @Override
    public String toString() {
        return niceName != null ? niceName : type.getID() + ":" + type.getMaximumDurability();
    }

}