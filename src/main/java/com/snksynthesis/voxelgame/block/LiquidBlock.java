package com.snksynthesis.voxelgame.block;

import java.util.function.Consumer;

public class LiquidBlock extends Block{
    public LiquidBlock() {
        super();
    }

    public LiquidBlock(Consumer<Builder> builderConsumer) {
        super(builderConsumer);
    }

    @Override
    public boolean isLiquid() {
        return true;
    }
}
