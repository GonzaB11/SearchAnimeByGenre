package com.animeRecommendation;

import java.awt.EventQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.animeRecommendation.front.SwingApp;

@SpringBootApplication
public class AnimeRecommendationApplication {

	public static void main(String[] args) {
		new Thread(() -> {
			SpringApplication.run(AnimeRecommendationApplication.class, args);
			System.out.println("Solicitud ejecutada");
		}).start();

		EventQueue.invokeLater(() -> {
            try {
                SwingApp frame = new SwingApp();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}
}
