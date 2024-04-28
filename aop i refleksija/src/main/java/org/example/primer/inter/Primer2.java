package org.example.primer.inter;

import org.example.annotation.spring.Bean;
import org.example.annotation.spring.Qualifier;

@Bean(singleton = false)
@Qualifier(value = "Two")
public class Primer2 implements  InterfacePrimer{
    private String primer = "Primer2";

    @Override
    public String getPrimer() {
        return primer;
    }

    @Override
    public void setPrimer(String primer) {
        this.primer = primer;
    }
}
