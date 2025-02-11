package com.poniansoft.shrtly.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void initialize() throws IOException {
        InputStream serviceAccount = Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream("firebase_config.json"),
                "firebase_config.json not found!"
        );

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }
}
