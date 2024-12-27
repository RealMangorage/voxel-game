package com.snksynthesis.voxelgame.texture;

import com.snksynthesis.voxelgame.block.Block;
import com.snksynthesis.voxelgame.block.BlockFace;
import com.snksynthesis.voxelgame.block.BlockTexture;
import org.joml.Vector2f;

public class TextureAtlas {

    public static final float IMG_WIDTH_PX = 256.0f;
    private static final float TEX_WIDTH_PX = 32.0f;
    private static final float TEX_WIDTH = TEX_WIDTH_PX / IMG_WIDTH_PX;

    private static final int size = (int) (IMG_WIDTH_PX / TEX_WIDTH_PX);

    /**
     * @param type the {@link Block}
     * @param face which face of the block {@link BlockFace}
     * @return the texture coordinates based on <code>type</code> and
     *         <code>face</code> parameters
     */
    public static float[] getTexCoords(Block type, BlockFace face) {
        BlockTexture texture = type.getTexCoords(face);
        return TextureAtlas.getTexCoordsByRowCol(face, texture.getY(), texture.getX());

    }

    /**
     * @param row 0-based starts from top left corner of image
     * @param col 0-based starts from top left corner of image
     */
    private static float[] getTexCoordsByRowCol(BlockFace face, int row, int col) {
        float offsetX = TEX_WIDTH * col;
        float offsetY = TEX_WIDTH * row;
        return getTexCoordsRaw(face, new Vector2f(0f + offsetX, 1f - offsetY),
                new Vector2f(0f + TEX_WIDTH + offsetX, 1f - offsetY), new Vector2f(0f + offsetX, 1f - TEX_WIDTH - offsetY),
                new Vector2f(0f + TEX_WIDTH + offsetX, 1f - TEX_WIDTH - offsetY));
    }

    public static void main(String[] args) {
        getTexCoordsByRowCol(BlockFace.TOP, 0, 0);
    }

    private static float[] getTexCoordsRaw(
            BlockFace face,
            Vector2f topLeft,
            Vector2f topRight,
            Vector2f bottomLeft,
            Vector2f bottomRight
    ){
        switch (face) {
            case TOP:
            case BOTTOM:
                return new float[] { topLeft.x, topLeft.y, topRight.x, topRight.y, bottomRight.x, bottomRight.y,
                        bottomRight.x, bottomRight.y, bottomLeft.x, bottomLeft.y, topLeft.x, topLeft.y };
            case LEFT:
            case RIGHT:
                return new float[] { bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y, topRight.x, topRight.y,
                        topRight.x, topRight.y, topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y, };
            case FRONT:
            case BACK:
                return new float[] { topRight.x, topRight.y, topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y,
                        bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y, topRight.x, topRight.y, };
            default:
                return new float[] {};
        }
    }
}
