package com.education.lwjgl.config;

import com.education.lwjgl.LwjglApp;
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
}
