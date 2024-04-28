package org.example.primer;

import org.example.annotation.spring.Autowired;
import org.example.annotation.spring.Bean;
import org.example.annotation.spring.Qualifier;
import org.example.primer.inter.InterfacePrimer;

@Bean(singleton = true)
public class Korisnik {
    private String ime = "Pera";

    @Autowired(details = true)
    private Klasa klasa;

    @Autowired(details = true)
    @Qualifier("One")
    private InterfacePrimer interfacePrimer;
    @Autowired(details = true)
    @Qualifier("One")
    private InterfacePrimer interfacePrimer2;
    @Autowired(details = true)
    @Qualifier("One")
    private InterfacePrimer interfacePrimer3;
    @Autowired(details = true)
    @Qualifier("One")
    private InterfacePrimer interfacePrimer4;
    @Autowired(details = true)
    @Qualifier("Two")
    private InterfacePrimer interfacePrimer5;
    @Autowired(details = true)
    @Qualifier("Two")
    private InterfacePrimer interfacePrimer6;
    @Autowired(details = true)
    @Qualifier("Two")
    private InterfacePrimer interfacePrimer7;


}
