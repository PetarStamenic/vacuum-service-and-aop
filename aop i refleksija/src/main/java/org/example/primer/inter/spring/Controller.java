package org.example.primer.inter.spring;

import org.example.annotation.http.GET;
import org.example.annotation.http.POST;
import org.example.annotation.http.Path;
import org.example.annotation.spring.Autowired;
import org.example.annotation.spring.Qualifier;
import org.example.primer.inter.InterfacePrimer;

@org.example.annotation.spring.Controller
public class Controller {
    
    @Autowired(details = true)
    @Qualifier("One")
    private InterfacePrimer p1;

    @Autowired(details = true)
    private Service service1;
    @Autowired(details = true)
    private Service service2;
    @Autowired(details = true)
    private Service service3;
    @Autowired(details = false)
    private Service service4;
    @Autowired(details = true)
    private Service service5;


    @Autowired(details = true)
    private Component componen1;
    @Autowired(details = true)
    private Component Componen2;
    @Autowired(details = false)
    private Component Componen3;
    @Autowired(details = true)
    private Component Componen4;
    @Autowired(details = true)
    private Component Componen5;


    @Path(path = "/testGet")
    @GET
    public String getTest(){
        return "Gettest";
    }

    @Path(path = "/testPost")
    @POST
    public String postTest(){
        return "Posttest";
    }

}
