package com.education.lwjgl.app;

import com.education.lwjgl.config.AppConfig;
import com.education.lwjgl.renderer.SurfaceRenderer;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author ivanovaolya
 */
public class LwjglApp extends App {

    private SurfaceRenderer surfaceRenderer;

    public LwjglApp(final SurfaceRenderer surfaceRenderer) {
        this.surfaceRenderer = surfaceRenderer;
    }

    public void run() {
        createWindow();
        getDelta(); // initialise delta timer
        initGL();

        while (!isCloseRequested()) {
            pollInput();
            pollMouse();
            surfaceRenderer.updateLogic(getDelta());
            surfaceRenderer.renderGl();
            Display.update();
        }

        cleanup();
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

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        final LwjglApp lwjglApp = context.getBean(LwjglApp.class);

        lwjglApp.run();
    }
}
