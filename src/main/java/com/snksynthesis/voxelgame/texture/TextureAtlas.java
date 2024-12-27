package com.snksynthesis.voxelgame.texture;

import com.snksynthesis.voxelgame.block.Block;
import com.snksynthesis.voxelgame.block.BlockFace;
import com.snksynthesis.voxelgame.block.BlockTexture;
import org.joml.Vector2f;

public class TextureAtlas {

    public static final float IMG_WIDTH_PX = 256.0f;
    private static final float TEX_WIDTH_PX = 32.0f;
    private static final float TEX_WIDTH = TEX_WIDTH_PX / IMG_WIDTH_PX;

    /**
     * @param type the {@link Block}
     * @param face which face of the block {@link BlockFace}
     * @return the texture coordinates based on <code>type</code> and
     *         <code>face</code> parameters
     */
    public static float[] getTexCoords(Block type, BlockFace face) {
        BlockTexture texture = type.getTexCoords(face);
        return TextureAtlas.getTexCoordsByRowCol(face, texture.getX(), texture.getY());

    }

    /**
     * Gets a 32x32 px area of the TextureAtlas based on X/Y coords
     * Starting from top left (0,0) (0px, 0px, 32px, 32px)
     * (1, 0) (32px, 0px) (64px, 32px)
     */
    private static float[] getTexCoordsByRowCol(BlockFace face, int x, int y) {
        float offsetX = TEX_WIDTH * x;
        float offsetY = TEX_WIDTH * y;
        return getTexCoordsRaw(
                face,
                new Vector2f(0f + offsetX, 1f - offsetY),
                new Vector2f(0f + TEX_WIDTH + offsetX, 1f - offsetY),
                new Vector2f(0f + offsetX, 1f - TEX_WIDTH - offsetY),
                new Vector2f(0f + TEX_WIDTH + offsetX, 1f - TEX_WIDTH - offsetY));
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
