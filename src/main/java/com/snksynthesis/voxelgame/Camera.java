package com.snksynthesis.voxelgame;

import com.snksynthesis.voxelgame.gfx.Shader;
import com.snksynthesis.voxelgame.gfx.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;

public class Camera implements Entity {

    private Vector3f front;
    private final Vector3f pos;
    private boolean firstMouse;
    private float lastX, lastY, yaw, pitch;

    private static final float BASE_WALK_SPEED = 10.0f;
    private static final float BASE_PRINT_BOOST = 7.0f;
    private static final Vector3f UP = new Vector3f(0.0f, 1.0f, 0.0f);
    private static final Vector3f DOWN = new Vector3f(0.0f, -1.0f, 0.0f);

    public Camera() {
        front = new Vector3f(0.0f, 0.0f, -1.0f);
        pos = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    public void procInput(Window window) {

        // Keyboard input
        float SPEED = BASE_WALK_SPEED * window.getDeltaTime();
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
            SPEED = (BASE_WALK_SPEED + BASE_PRINT_BOOST) * window.getDeltaTime();
        }
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_W) == GLFW_PRESS) {
            pos.add(new Vector3f(front).mul(SPEED));
        }
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_S) == GLFW_PRESS) {
            pos.sub(new Vector3f(front).mul(SPEED));
        }
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_A) == GLFW_PRESS) {
            pos.sub(new Vector3f(front).cross(Camera.UP).normalize().mul(SPEED));
        }
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_D) == GLFW_PRESS) {
            pos.add(new Vector3f(front).cross(Camera.UP).normalize().mul(SPEED));
        }
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_SPACE) == GLFW_PRESS) {
            pos.add(new Vector3f(Camera.UP).mul(SPEED)); // Going up
        }
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            pos.add(new Vector3f(Camera.DOWN).mul(SPEED)); // Going down
        }

        // Toggle cursor mode
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_Q) == GLFW_PRESS) {
            glfwSetInputMode(window.getRawWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetInputMode(window.getRawWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            firstMouse = true;
        }

        // Exit
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_F1) == GLFW_PRESS) {
            System.exit(0);
        }
    }

    public void addMouseCallback(Window window) {

        lastX = window.getWidth() / 2f;
        lastY = window.getHeight() / 2f;
        firstMouse = true;

        glfwSetCursorPos(window.getRawWindow(), lastX, lastY);
        glfwSetInputMode(window.getRawWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        glfwSetCursorPosCallback(window.getRawWindow(), (_window, xpos, ypos) -> {
            if (glfwGetInputMode(window.getRawWindow(), GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
                if (firstMouse) {
                    glfwSetCursorPos(window.getRawWindow(), lastX, lastY);
                    // lastX = (float) xpos;
                    // lastY = (float) ypos;
                    firstMouse = false;
                }

                var xOffset = (float) (xpos - lastX);
                var yOffset = (float) (lastY - ypos);
                lastX = (float) xpos;
                lastY = (float) ypos;

                final float SENSITIVITY = 0.1f;

                xOffset *= SENSITIVITY;
                yOffset *= SENSITIVITY;

                yaw += xOffset;
                pitch += yOffset;

                if (pitch > 89.0f) {
                    pitch = 89.0f;
                } else if (pitch < -89.0f) {
                    pitch = -89.0f;
                }

                var direction = new Vector3f();
                direction.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
                direction.y = (float) Math.sin(Math.toRadians(pitch));
                direction.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
                front = direction.normalize();
            }
        });
    }

    public void setPos(float x, float y, float z) {
        pos.set(x, y, z);
    }

    public Vector3f getPos() {
        return pos;
    }

    public Matrix4f getViewMat() {
        return new Matrix4f().lookAt(pos, new Vector3f(pos).add(front), Camera.UP);
    }

    @Override
    public void draw(Shader shader, MemoryStack stack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Window window) {
        procInput(window);
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException();
    }
}
