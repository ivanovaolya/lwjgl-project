package com.education.lwjgl.app;

import com.education.lwjgl.config.AppConfig;
import com.education.lwjgl.surface.Surface;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.videoio.VideoCapture;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.opencv.imgcodecs.Imgcodecs.imencode;

/**
 * @author ivanovaolya
 */
public class OpenCvApp extends App {

    private Surface surface;

    private static VideoCapture camera;

    private float triangleAngle;

    public OpenCvApp(Surface surface) {
        this.surface = surface;
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        camera = new VideoCapture(0);
        camera.open(0);
        if(!camera.isOpened()){
            throw new RuntimeException("Cannot open the camera and start streaming");
        }
    }

    public void run() throws IOException {
        createWindow();
        getDelta(); // initialise delta timer
        initGL();

        while (!isCloseRequested()) {
            pollInput();
            updateLogic(getDelta());      // note: disable auto-rotation for practical assignment #4
            renderGl();
            renderCameraImage();          // practical assignment #3
            Display.update();
        }

        camera.release();
        cleanup();
    }

    private static int loadTexture(BufferedImage image){
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));             // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));     // Alpha component. Only for RGBA
            }
        }

        buffer.flip();

        int textureID = glGenTextures(); //Generate texture ID
        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

        //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        //Return the texture ID so we can bind it later again
        return textureID;
    }

    private void renderCameraImage() throws IOException {
        double size = 20;
        MatOfByte matOfByte = new MatOfByte();
        Mat frame = new Mat();
        GL11.glEnable(GL_TEXTURE_2D);
        if (camera.read(frame)) {
            imencode(".bmp", frame, matOfByte);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
            int textureID = loadTexture(bufferedImage);
            GL11.glColor3d(2d, 2d, 2d);
            glBindTexture(GL_TEXTURE_2D, textureID);
            GL11.glLoadIdentity();
            glBegin(GL_TRIANGLES);
            glTexCoord2f(1, 1);
            glVertex3d(- size, - size, - size);
            glTexCoord2f(1, 0);
            glVertex3d(- size, size, - size);
            glTexCoord2f(0, 0);
            glVertex3d(size, size, - size);
            glTexCoord2f(0, 0);
            glVertex3d(size, size, - size);
            glTexCoord2f(0, 1);
            glVertex3d(size, - size, - size);
            glTexCoord2f(1, 1);
            glVertex3d(- size, - size, - size);
            glEnd();
            GL11.glLoadIdentity();
        }
    }

    private void renderGl() {
        double step = 0.05;

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glTranslatef(0.0f, 0.0f, -10.0f);
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

    /**
     * This method increases the rotation variable for the triangles
     *
     * @param delta
     */
    private void updateLogic(int delta) {
        triangleAngle += 0.1f * delta;
    }

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        OpenCvApp openCvApp = context.getBean(OpenCvApp.class);

        openCvApp.run();
    }

}