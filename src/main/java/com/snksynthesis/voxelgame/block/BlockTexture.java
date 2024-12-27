package com.snksynthesis.voxelgame.block;

public final class BlockTexture {
    public static BlockTexture of(int x, int y) {
        return new BlockTexture(x, y);
    }

    private final int x, y;

    private BlockTexture(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
