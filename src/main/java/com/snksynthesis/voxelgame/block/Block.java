package com.snksynthesis.voxelgame.block;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class Block {
    private static final BlockTexture UNDEFINED_TEXTURE = BlockTexture.of(0, 0);

    private final Map<BlockFace, BlockTexture> textureMap;

    public Block() {
        this.textureMap = null;
    }

    public Block(Consumer<Builder> builderConsumer) {
        Builder builder = new Builder();
        builderConsumer.accept(builder);
        this.textureMap = builder.build();
    }

    public BlockTexture getTexCoords(BlockFace face) {
        return textureMap == null ? UNDEFINED_TEXTURE : textureMap.get(face);
    }

    public float[][] getShape() {
        return CUBE_POSITIONS;
    }

    public boolean isSolid() {
        return true;
    }

    // @formatter:off
    /**
     * Face Index:
     * <ul>
     *  <li>0 - Left</li>
     *  <li>1 - Right</li>
     *  <li>2 - Front</li>
     *  <li>3 - Back</li>
     *  <li>4 - Bottom</li>
     *  <li>5 - Top</li>
     * </ul>
     */
    private static final float[][] CUBE_POSITIONS = {
        {
            -0.5f, -0.5f, -0.5f,
             0.5f, -0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f
        },

        {
            -0.5f, -0.5f,  0.5f,
             0.5f, -0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f
        },

        {
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f
        },

        {
            0.5f,  0.5f,  0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f,  0.5f,  0.5f
        },

        {
            -0.5f, -0.5f, -0.5f,
             0.5f, -0.5f, -0.5f,
             0.5f, -0.5f,  0.5f,
             0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f
        },

        {
            -0.5f,  0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
             0.5f,  0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f
        }
    };

    public boolean isLiquid() {
        return false;
    }

    public static class Builder {
        private final EnumMap<BlockFace, BlockTexture> textureMap = new EnumMap<>(BlockFace.class);

        public Builder setTexture(BlockFace blockFace, BlockTexture texture) {
            this.textureMap.put(blockFace, texture);
            return this;
        }

        /**
         * Will not override already set BlockFace textures
         * @param texture
         * @return
         */
        public Builder setCubeAll(BlockTexture texture) {
            for (BlockFace face : BlockFace.values()) {
                this.textureMap.computeIfAbsent(face, f -> texture);
            }
            return this;
        }

        private Map<BlockFace, BlockTexture> build() {
            return Map.copyOf(textureMap);
        }
    }
}
