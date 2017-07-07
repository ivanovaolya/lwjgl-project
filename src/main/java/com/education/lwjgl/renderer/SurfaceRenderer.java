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

    /**
     * Changes angle of rotation for the triangles
     * @param delta
     */
    void changeTriangleAngle(float delta);

    /**
     * Changes parameter for scaling for the triangles
     * @param delta
     */
    void changeScale(float delta);

    boolean isScalable(float scaleStep);

}
