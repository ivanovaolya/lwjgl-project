package com.education.lwjgl.app;

import com.education.lwjgl.config.AppConfig;
import com.education.lwjgl.surface.Surface;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.vecmath.Vector3d;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ivanovaolya
 */
public class AccelerometerApp extends App {

    private Surface surface;

    private UdpClient udpClient;

    private double[] accelerometerData;

    private double angle;

    private float triangleAngle;

    public AccelerometerApp(Surface surface) {
        this.surface = surface;
        udpClient = new UdpClient();
        accelerometerData = new double[3];
    }

    public void run() throws Exception {
        createWindow();
        getDelta(); // initialise delta timer
        initGL();

        while (!isCloseRequested()) {
            pollInput();
            activateLiveXmlStream();
            renderGl();
            Display.update();
        }

        cleanup();
    }

    private void renderGl() {
        double step = 0.05;

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glTranslatef(0.0f, 0.0f, -10.0f);
        GL11.glRotated(angle*4, accelerometerData[0], accelerometerData[1], accelerometerData[2]);

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

    private void activateLiveXmlStream() {
        try {
            byte[] buffer = new byte[1024];
            Vector3d accelerometerVector = new Vector3d();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            DatagramSocket datagramSocket = udpClient.call();
            datagramSocket.receive(packet);
            byte[] data = packet.getData();
            String xml = new String(data);
            System.out.println(xml);
            parseAccelerometerData(xml, accelerometerVector);
            System.out.println("Accelerometer data: [" + accelerometerVector.x + ", " + accelerometerVector.y +
                    ", " + accelerometerVector.z + "]");

            Vector3d vector = new Vector3d(0, 1, 0);
            double angle = accelerometerVector.angle(vector);
            accelerometerVector.cross(vector, accelerometerVector);
            accelerometerVector.normalize();

            accelerometerData[0] = accelerometerVector.x;
            accelerometerData[1] = accelerometerVector.y;
            accelerometerData[2] = accelerometerVector.z;
            this.angle = angle;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseAccelerometerData(String xml, Vector3d vector3d) {
        Pattern acc1 = Pattern.compile(Pattern.quote("<Accelerometer1>") + "(.*?)" + Pattern.quote("</Accelerometer1>"));
        Pattern acc2 = Pattern.compile(Pattern.quote("<Accelerometer2>") + "(.*?)" + Pattern.quote("</Accelerometer2>"));
        Pattern acc3 = Pattern.compile(Pattern.quote("<Accelerometer3>") + "(.*?)" + Pattern.quote("</Accelerometer3>"));
        Matcher matcher1 = acc1.matcher(xml);
        Matcher matcher2 = acc2.matcher(xml);
        Matcher matcher3 = acc3.matcher(xml);
        while (matcher1.find() && matcher2.find() && matcher3.find()) {
            vector3d.x = Double.parseDouble(matcher1.group(1));
            vector3d.y = Double.parseDouble(matcher2.group(1));
            vector3d.z = Double.parseDouble(matcher3.group(1));
        }
    }

    private class UdpClient {
        private String ip = "192.168.1.101";
        private int port = 8087;

        DatagramSocket call() throws UnknownHostException, SocketException {
            return new DatagramSocket(port, InetAddress.getByName(ip));
        }
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        AccelerometerApp accelerometerApp = context.getBean(AccelerometerApp.class);

        accelerometerApp.run();
    }
}
