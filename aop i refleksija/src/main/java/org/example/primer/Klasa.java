package org.example.primer;

import org.example.annotation.spring.Bean;

@Bean(singleton = false)
public class Klasa {
    private String string = "Neki string";
}
