package com.snksynthesis.voxelgame.block;

public final class Blocks {
    // UNUSED
    public static final Block AIR = new Block();
    public static final Block DIRT = new Block();

    // USED
    public static final Block STONE = new Block(
            b -> b.setCubeAll(BlockTexture.of(1, 2))
    );

    public static final Block WATER = new LiquidBlock(
            b -> b.setCubeAll(BlockTexture.of(1, 0))
    );
    public static final Block SAND = new Block(
            b -> b.setCubeAll(BlockTexture.of(0, 0))
    );
    public static final Block SOIL = new Block(
            b -> b.setCubeAll(BlockTexture.of(0, 2))
    );
    public static final Block GRASS = new Block(
            b -> b
                    .setTexture(BlockFace.TOP, BlockTexture.of(0, 1))
                    .setTexture(BlockFace.BOTTOM, BlockTexture.of(0, 2))
                    .setCubeAll(BlockTexture.of(1, 1))
    );
}
