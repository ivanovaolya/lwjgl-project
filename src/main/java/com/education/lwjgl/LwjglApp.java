package com.education.lwjgl;

import com.education.lwjgl.config.AppConfig;
import com.education.lwjgl.renderer.SurfaceRenderer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author ivanovaolya
 */
public class LwjglApp {

    private SurfaceRenderer surfaceRenderer;

    private boolean closeRequested = false;  // exit flag

    private long lastFrameTime; // for calculating delta

    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float scaleZ = 1.0f;

    public LwjglApp(final SurfaceRenderer surfaceRenderer) {
        this.surfaceRenderer = surfaceRenderer;
    }

    public void run() {
        createWindow();
        getDelta(); // initialise delta timer
        initGL();

        while (!closeRequested) {
            pollInput();
            pollMouse();
            surfaceRenderer.updateLogic(getDelta());
            surfaceRenderer.renderGl();
            Display.update();
        }

        cleanup();
    }

    private void createWindow() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.setVSyncEnabled(true);
            Display.setTitle("Kiss Surface Rendering");
            Display.create();
        } catch (LWJGLException e) {
            System.err.print(e.getMessage());
            System.exit(0);
        }
    }

    private void initGL() {
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();

        GL11.glViewport(0, 0, width, height);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(75.0f, ((float) width / (float) height), 0.1f, 100.0f);
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
    public void pollInput() {
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
     * Changes the surface scale and TriangleAngle depending on mouse actions
     */
    private void pollMouse() {
        while(Mouse.next()) {

            if(Mouse.getEventButtonState()) {

                // left button pressed
                if(Mouse.getEventButton() == 0 ) {
                    surfaceRenderer.changeTriangleAngle(-15.0f);
                }

                // right button pressed
                if(Mouse.getEventButton() == 1) {
                    surfaceRenderer.changeTriangleAngle(15.0f);
                }
                // scroll wheel button pressed - Mouse.getEventButton() == 2
            }
            if (Mouse.hasWheel()) {
                int intMouseMovement = Mouse.getDWheel() ;

                float scaleStep = 0.1f;

                // mousewheel has been scrolled up
                if(intMouseMovement > 0) {
                    surfaceRenderer.changeScale(scaleStep);
                }
                // mousewheel has been scrolled down
                if(intMouseMovement < 0) {
                    if (surfaceRenderer.isScalable(scaleStep)) {
                        surfaceRenderer.changeScale(-scaleStep);
                    }
                }
            }
        }
    }

    /**
     * Calculates how many milliseconds have passed since last frame
     *
     * @return milliseconds passed since last frame
     */
    public int getDelta() {
        long time = (Sys.getTime() * 1000) / Sys.getTimerResolution();
        int delta = (int) (time - lastFrameTime);
        lastFrameTime = time;

        return delta;
    }

    /**
     * Destroys and cleans up resources
     */
    private void cleanup() {
        Display.destroy();
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        final LwjglApp lwjglApp = context.getBean(LwjglApp.class);

        lwjglApp.run();
    }
}
