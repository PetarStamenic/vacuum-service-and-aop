package org.example.primer.inter;

import org.example.annotation.spring.Bean;
import org.example.annotation.spring.Qualifier;


@Bean(singleton = true)
@Qualifier(value = "One")
public class Primer1 implements  InterfacePrimer{
    private String primer = "Primer1";

    @Override
    public String getPrimer() {
        return primer;
    }

    @Override
    public void setPrimer(String primer) {
        this.primer = primer;
    }
}
