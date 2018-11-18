package com.github.maxopoly.angeliacore.block.states;

public class SolidStaticBlockState extends SingleConstBlockState {

    public SolidStaticBlockState(int id, byte metaData, float hardness, String texturePackIdentifier,
            String niceName) {
        super(id, metaData, hardness, texturePackIdentifier, niceName);
    }

    public SolidStaticBlockState(int id, float hardness, String texturePackIdentifier,
            String niceName) {
        super(id, (byte) 0, hardness, texturePackIdentifier, niceName);
    }

    @Override
    public boolean isFullBlock() {
        return true;
    }

    @Override
    public boolean isLiquid() {
        return false;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

}
