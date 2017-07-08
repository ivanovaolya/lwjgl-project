package com.education.lwjgl.config;

import com.education.lwjgl.app.AccelerometerApp;
import com.education.lwjgl.app.LwjglApp;
import com.education.lwjgl.app.OpenCvApp;
import com.education.lwjgl.renderer.KissSurfaceRenderer;
import com.education.lwjgl.renderer.SurfaceRenderer;
import com.education.lwjgl.surface.KissSurface;
import com.education.lwjgl.surface.Surface;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ivanovaolya
 */
@Configuration
public class AppConfig {

    @Bean
    public Surface surface(){
        return new KissSurface();
    }

    @Bean
    public SurfaceRenderer surfaceRenderer() {
        return new KissSurfaceRenderer(surface());
    }

    @Bean
    public LwjglApp application() {
        return new LwjglApp(surfaceRenderer());
    }

    @Bean
    public OpenCvApp openCvApp() {
        return new OpenCvApp(surface());
    }

    @Bean
    public AccelerometerApp accelerometerApp() {
        return new AccelerometerApp(surface());
    }
}
