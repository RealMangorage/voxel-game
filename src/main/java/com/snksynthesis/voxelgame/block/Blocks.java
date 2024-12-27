package com.snksynthesis.voxelgame.block;

public final class Blocks {
    // UNUSED
    public static final Block AIR = new Block();
    public static final Block DIRT = new Block();

    // USED
    public static final Block WATER = new LiquidBlock(
            b -> b.setCubeAll(BlockTexture.of(1, 0))
    );

    public static final Block SAND = new Block(
            b -> b.setCubeAll(BlockTexture.of(2, 0))
    );

    public static final Block GRASS = new Block(
            b -> b
                    .setTexture(BlockFace.TOP, BlockTexture.of(4, 0))
                    .setTexture(BlockFace.BOTTOM, BlockTexture.of(6, 0))
                    .setCubeAll(BlockTexture.of(3, 0))
    );

    public static final Block STONE = new Block(
            b -> b.setCubeAll(BlockTexture.of(5, 0))
    );

    public static final Block SOIL = new Block(
            b -> b.setCubeAll(BlockTexture.of(6, 0))
    );
}
