package com.snksynthesis.voxelgame.block;

import java.util.function.Consumer;

// TODO: Finish WIP
public class SlabBlock extends Block {

    public SlabBlock() {
        super();
    }

    public SlabBlock(Consumer<Builder> builderConsumer) {
        super(builderConsumer);
    }

    @Override
    public float[][] getShape() {
        return CUBE_POSITIONS;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    private static final float[][] CUBE_POSITIONS = {
            {
                    -0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    0.5f,  0.0f, -0.5f,
                    0.5f,  0.0f, -0.5f,
                    -0.5f,  0.0f, -0.5f,
                    -0.5f, -0.5f, -0.5f
            },

            {
                    -0.5f, -0.5f,  0.5f,
                    0.5f, -0.5f,  0.5f,
                    0.5f,  0.0f,  0.5f,
                    0.5f,  0.0f,  0.5f,
                    -0.5f,  0.0f,  0.5f,
                    -0.5f, -0.5f,  0.5f
            },

            {
                    -0.5f,  0.0f,  0.5f,
                    -0.5f,  0.0f, -0.5f,
                    -0.5f, -0.5f, -0.5f,
                    -0.5f, -0.5f, -0.5f,
                    -0.5f, -0.5f,  0.5f,
                    -0.5f,  0.0f,  0.5f
            },

            {
                    0.5f,  0.0f,  0.5f,
                    0.5f,  0.0f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f,  0.5f,
                    0.5f,  0.0f,  0.5f
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
                    -0.5f,  0.0f, -0.5f,
                    0.5f,  0.0f, -0.5f,
                    0.5f,  0.0f,  0.5f,
                    0.5f,  0.0f,  0.5f,
                    -0.5f,  0.0f,  0.5f,
                    -0.5f,  0.0f, -0.5f
            }
    };
}
