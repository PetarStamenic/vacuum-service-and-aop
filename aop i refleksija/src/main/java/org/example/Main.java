package org.example;

import org.example.di.DI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        DI.getInstance().initClasses();
        System.out.println("Hello world!");
    }



}