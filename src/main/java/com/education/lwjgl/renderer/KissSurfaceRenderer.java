package com.education.lwjgl.renderer;

import com.education.lwjgl.surface.Surface;

import org.lwjgl.opengl.GL11;

/**
 * @author ivanovaolya
 */
public class KissSurfaceRenderer implements SurfaceRenderer {

    private Surface surface;

    private float triangleAngle; // angle of rotation for the triangles

    public KissSurfaceRenderer(final Surface surface) {
        this.surface = surface;
    }

    public void renderGl() {
        double step = 0.05;

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glTranslatef(0.0f, 0.0f, -6.0f);
        GL11.glRotatef(triangleAngle, 2.0f, 2.0f, 2.0f); // rotate the surface on it's X, Y, Z axis

        GL11.glBegin(GL11.GL_TRIANGLES);
        for (double u = 0; u < 2 * Math.PI; u += step) {
            for (double z = -1; z < 1; z += step) {
                GL11.glVertex3d(surface.getX(u, z), surface.getY(u, z), surface.getZ(z));
                GL11.glVertex3d(surface.getX(u + step, z), surface.getY(u + step, z), surface.getZ(z));
                GL11.glVertex3d(surface.getX(u, z + step), surface.getY(u, z + step), surface.getZ(z + step));
            }
        }
        GL11.glEnd();
    }

    public void updateLogic(int delta) {
        triangleAngle += 0.1f * delta;
    }
}
