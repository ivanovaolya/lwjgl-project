package com.education.lwjgl.app;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * @author ivanovaolya
 */
public abstract class App {

    private boolean closeRequested;

    private long lastFrameTime; // for calculating delta

    public abstract void run() throws Exception;

    protected void createWindow() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.setVSyncEnabled(true);
            Display.setTitle("Rendering Surface");
            Display.create();
        } catch (LWJGLException e) {
            System.err.print(e.getMessage());
            System.exit(0);
        }
    }

    protected void initGL() {
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();

        GL11.glViewport(0, 0, width, height);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        GLU.gluPerspective(85.0f, ((float) width / (float) height), 0.1f, 100.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
    }

    /**
     * Changes the exit flag depending on the typed keyboard keys
     */
    protected void pollInput() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
                    closeRequested = true;
            }
        }
        if (Display.isCloseRequested()) {
            closeRequested = true;
        }
    }

    /**
     * Calculates how many milliseconds have passed since last frame
     *
     * @return milliseconds passed since last frame
     */
    protected int getDelta() {
        long time = (Sys.getTime() * 1000) / Sys.getTimerResolution();
        int delta = (int) (time - lastFrameTime);
        lastFrameTime = time;

        return delta;
    }

    /**
     * Destroys and cleans up resources
     */
    protected void cleanup() {
        Display.destroy();
    }

    public boolean isCloseRequested() {
        return closeRequested;
    }

}
