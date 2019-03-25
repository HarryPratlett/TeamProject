package junit.myst.GUI;

import com.myst.GUI.GUI;
import com.myst.rendering.Window;
import org.junit.Test;
import org.lwjgl.opengl.GL;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

public class GUITest {

    @Test
    public void addButton() throws Exception {
        Window.setCallbacks();
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialise GLFW");
        }
        Window window = new Window();

        window.createWindow("test");
        window.setFullscreen(false);
        GL.createCapabilities();


        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GUI gui = new GUI(window, window.getInput());


        gui.addButton(0, 0, 0, 0, "test");
        String path = gui.buttons.get(new Rectangle2D.Float(0, 0, 0, 0));
        String correctPath = "test";
        assertEquals(path, correctPath);
    }

    @Test
    public void alterVertices() throws Exception {
        Window.setCallbacks();
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialise GLFW");
        }
        Window window = new Window();

        window.createWindow("test");
        window.setFullscreen(false);
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        float[] baseVertices = new float[] {
                1f, 1f, 1f, /*0*/  1f, 1f, 1f, /*1*/    1f, 1f, 1f, /*2*/
                1f, 1f, 1f/*3*/
        };
        GUI gui = new GUI(window, window.getInput());
        float[] vertices = gui.alterVertices(baseVertices, 1, 1, 0.1, 0.1);
        float[] correctVertices = new float[] {
                0.1f, 0.1f, 1f, /*0*/  0.1f, 0.1f, 1f, /*1*/    0.1f, 0.1f, 1f, /*2*/
                0.1f, 0.1f, 1f/*3*/
        };
        assertTrue(Arrays.equals(vertices, correctVertices));
    }


}
