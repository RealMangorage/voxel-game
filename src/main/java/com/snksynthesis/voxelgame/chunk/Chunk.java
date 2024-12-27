package com.snksynthesis.voxelgame.chunk;

import com.snksynthesis.voxelgame.Entity;
import com.snksynthesis.voxelgame.Noise;
import com.snksynthesis.voxelgame.block.Block;
import com.snksynthesis.voxelgame.block.BlockFace;
import com.snksynthesis.voxelgame.block.Blocks;
import com.snksynthesis.voxelgame.gfx.Mesh;
import com.snksynthesis.voxelgame.gfx.Shader;
import com.snksynthesis.voxelgame.gfx.Window;
import com.snksynthesis.voxelgame.texture.Texture;
import com.snksynthesis.voxelgame.texture.TextureAtlas;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL33.glGetUniformLocation;
import static org.lwjgl.opengl.GL33.glUniformMatrix4fv;

public class Chunk implements Entity {

    public final static int WIDTH = 16;
    public final int HEIGHT = 256;

    private final int WATER_HEIGHT = 4;

    private float x;
    private final float startX;
    private float z;
    private final float startZ;

    private Mesh mesh;
    private Mesh waterMesh;
    private final Matrix4f model;
    private final FloatBuffer allocatedMem;
    private final Texture tex;
    private final List<Float> vertices;
    private final List<Float> waterVertices;
    private final Block[][][] blocks;
    private int blockCount = 0;
    private int waterBlockCount = 0;
    private final Random rand;
    private boolean isGenerating;

    public Chunk(float startX, float startZ) {
        isGenerating = false;
        model = new Matrix4f();
        allocatedMem = MemoryUtil.memAllocFloat(16);
        tex = Texture.loadRGBA("textures/atlas.png");
        vertices = new ArrayList<>();
        waterVertices = new ArrayList<>();
        blocks = new Block[WIDTH][HEIGHT][WIDTH];
        rand = new Random();

        this.startX = startX;
        this.startZ = startZ;

        for (int x = 0; x < WIDTH; x++) {
            for (int z = 0; z < WIDTH; z++) {
                for (int y = 1; y <= WATER_HEIGHT; y++) {
                    if (rand.nextBoolean()) {
                        addBlock(x, 0, z, Blocks.SAND);
                    } else {
                        addBlock(x, 0, z, Blocks.STONE);
                    }
                    addBlock(x, y, z, Blocks.WATER);
                }
            }
        }
    }

    @Override
    public void draw(Shader shader, MemoryStack stack) {
        if (mesh != null) {
            int modelLoc = glGetUniformLocation(shader.getProgramId(), "model");
            glUniformMatrix4fv(modelLoc, false, model.get(allocatedMem));
            tex.bind();
            // blockCount * 6 faces * 2 triangles * 8 vertices
            mesh.draw(blockCount * 6 * 2 * 8);
            if (waterMesh != null) {
                waterMesh.draw(waterBlockCount * 6 * 2 * 8);
            }
            tex.unbind();
        }
    }

    @Override
    public void update(Window window) {
        genWorld();
    }

    @Override
    public void destroy() {
        MemoryUtil.memFree(allocatedMem);
        if (mesh != null) {
            mesh.destroy();
        }
        if (waterMesh != null) {
            waterMesh.destroy();
        }
    }

    private void genPillar(float x, float z, float height) {
        // `int y = 0; ...` makes everything stay in integers and not floats
        for (int y = 0; y < height; y++) {
            Block type;
            while (true) {
                if (height < WATER_HEIGHT + 2 && y < height) {
                    type = Blocks.SAND;
                    if (height < WATER_HEIGHT && y < height) {
                        if (rand.nextBoolean()) {
                            type = Blocks.SAND;
                        } else {
                            type = Blocks.STONE;
                        }
                    }
                    break;
                } else if (y < height * 0.5) {
                    type = Blocks.STONE;
                    break;
                } else if (y < height * 0.95) {
                    type = Blocks.SOIL;
                    break;
                } else {
                    type = Blocks.GRASS;
                    break;
                }
            }
            addBlock(x, y, z, type);
        }
    }

    private void addFace(BlockFace face, float x, float y, float z, Block type, boolean blockHeightReducible) {
        float[] texCoords = TextureAtlas.getTexCoords(type, face);

        List<Float> vertices = new ArrayList<>();

        int i = 0;
        int j = 0;
        do {
            // Positions
            if (type.isLiquid()) {
                vertices.add(type.getShape()[face.getIndex()][i + 0] + x);
                if (type.getShape()[face.getIndex()][i + 1] == 0.5f && blockHeightReducible) {
                    vertices.add(type.getShape()[face.getIndex()][i + 1] * 0.4f + y);
                } else {
                    vertices.add(type.getShape()[face.getIndex()][i + 1] + y);
                }
                vertices.add(type.getShape()[face.getIndex()][i + 2] + z);
            } else {
                vertices.add(type.getShape()[face.getIndex()][i + 0] + x);
                vertices.add(type.getShape()[face.getIndex()][i + 1] + y);
                vertices.add(type.getShape()[face.getIndex()][i + 2] + z);
            }

            // Texture Coordinates
            vertices.add(texCoords[j + 0]);
            vertices.add(texCoords[j + 1]);

            // Alpha Values
            if (type.isLiquid()) {
                vertices.add(0.8f);
            } else {
                vertices.add(1.0f);
            }

            // Ambient Values
            switch (face) {
                case BACK:
                    vertices.add(0.7f);
                    break;
                case BOTTOM:
                    vertices.add(0.3f);
                    break;
                case FRONT:
                    vertices.add(0.65f);
                    break;
                case LEFT:
                    vertices.add(0.8f);
                    break;
                case RIGHT:
                    vertices.add(0.85f);
                    break;
                case TOP:
                    vertices.add(1.0f);
                    break;
                default:
                    vertices.add(1.0f);
                    break;
            }

            i += 3;
            j += 2;

        } while (i < type.getShape()[face.getIndex()].length);

        if (type.isLiquid()) {
            this.waterVertices.addAll(vertices);
        } else {
            this.vertices.addAll(vertices);
        }
    }

    public void addBlock(float x, float y, float z, Block type) {
        if (type.isLiquid()) {
            waterBlockCount++;
        } else {
            blockCount++;
        }
        blocks[(int) x][(int) y][(int) z] = type;
    }

    private boolean isVisibleFrom(int x, int y, int z, Block type, BlockFace otherFace, BlockFace face) {
        try {
            /**
            if (x > blocks.length || y > blocks[x].length || z > blocks[x][y].length) {
                return true;
            } else if (x < 0 || y < 0 || z < 0) {
                return true;
            } else if (blocks[x][y][z] != null && blocks[x][y][z].isLiquid() && !type.isLiquid()) {
                return true;
            }
            return blocks[x][y][z] == null;
             **/
            var otherType = blocks[x][y][z];
            return otherType == null || type.isVisible(otherType, otherFace, face);
        } catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
    }

    private List<BlockFace> getVisibleFaces(int x, int y, int z, Block type) {
        List<BlockFace> faces = new ArrayList<>();

        if (isVisibleFrom(x + 1, y, z, type, BlockFace.FRONT, BlockFace.BACK)) {
            faces.add(BlockFace.BACK);
        }

        if (isVisibleFrom(x - 1, y, z, type, BlockFace.BACK, BlockFace.FRONT)) {
            faces.add(BlockFace.FRONT);
        }

        if (isVisibleFrom(x, y + 1, z, type, BlockFace.BOTTOM, BlockFace.TOP)) {
            faces.add(BlockFace.TOP);
        }

        if (isVisibleFrom(x, y - 1, z, type, BlockFace.TOP, BlockFace.BOTTOM)) {
            faces.add(BlockFace.BOTTOM);
        }
        if (isVisibleFrom(x, y, z + 1, type, BlockFace.LEFT, BlockFace.RIGHT)) {
            faces.add(BlockFace.RIGHT);
        }

        if (isVisibleFrom(x, y, z - 1, type, BlockFace.RIGHT, BlockFace.LEFT)) {
            faces.add(BlockFace.LEFT);
        }

        return faces;
    }

    public void genWorld() {
        if (z < WIDTH) {
            while (x < WIDTH) {
                float height = Noise.getNoiseHeight(x + startX, z + startZ);
                height = (float) Math.pow(height, 2.01);
                height += 0.5;
                height *= 5;
                genPillar(x, z, height);
                x++;
            }
            x = 0;
            z++;
        } else {
            isGenerating = true;
        }

        addBlock(0, 30, 0, Blocks.STONE);
        addBlock(0, 31, 0, Blocks.DIRT);
        addBlock(1, 30, 0, Blocks.DIRT);
    }

    public void genMesh() {
        if (mesh == null && isGenerating) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    for (int z = 0; z < WIDTH; z++) {
                        if (blocks[x][y][z] != null) {
                            var visibleFaces = getVisibleFaces(x, y, z, blocks[x][y][z]);
                            for (BlockFace face : visibleFaces) {
                                if (blocks[x][y][z] != null && blocks[x][y][z].isLiquid()) {
                                    if (visibleFaces.contains(BlockFace.TOP)) {
                                        // Show only top face for water blocks and exclude all other faces
                                        addFace(BlockFace.TOP, x + startX, y, z + startZ, blocks[x][y][z], true);
                                    }
                                } else {
                                    addFace(face, x + startX, y, z + startZ, blocks[x][y][z], false);
                                }
                            }
                        }
                    }
                }
            }

            var verticesArr = new float[vertices.size()];
            for (int i = 0; i < vertices.size(); i++) {
                verticesArr[i] = vertices.get(i).floatValue();
            }

            mesh = new Mesh(verticesArr);

            if (waterVertices.size() > 0) {
                var waterVerticesArr = new float[waterVertices.size()];
                for (int i = 0; i < waterVertices.size(); i++) {
                    waterVerticesArr[i] = waterVertices.get(i).floatValue();
                }
                waterMesh = new Mesh(waterVerticesArr);
            }
        }
    }

    public Vector3f getStartPos() {
        return new Vector3f(startX, 0f, startZ);
    }

    public Vector3f getPos() {
        return new Vector3f(startX + WIDTH / 2f, 0f, startZ + WIDTH / 2f);
    }

    public Vector3f getEndPos() {
        return new Vector3f(startX + WIDTH, 0f, startZ + WIDTH);
    }
}
