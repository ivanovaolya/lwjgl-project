package com.education.lwjgl.renderer;

/**
 * @author ivanovaolya
 */
public interface SurfaceRenderer {

    /**
     * Renders the surface
     */
    void renderGl();

    /**
     * Increases the rotation variable for the triangles
     * @param delta
     */
    void updateLogic(int delta);

}
