package com.education.lwjgl.surface;

/**
 * @author ivanovaolya
 * @version Sep 25, 2016
 */
public class KissSurface implements Surface {

    public final double A_COEFFICIENT = 2;

    /**
     * Parametric equations
     **/
    public double getX(double u, double v) {
        return A_COEFFICIENT * Math.pow(v, 2) * Math.sqrt(1 - v) * Math.cos(u);
    }

    public double getY(double u, double v) {
        return A_COEFFICIENT * Math.pow(v, 2) * Math.sqrt(1 - v) * Math.sin(u);
    }

    public double getZ(double v) {
        return A_COEFFICIENT * v;
    }
}
