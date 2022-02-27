package de.banarnia.api;

import org.jetbrains.annotations.NotNull;

public class Test {

    public static void main(String[] args) {
        String test0 = "Hallo";
        String test1 = "Hallo_";
        String test2 = "Hallo_Welt_";

        System.out.println(test0.split("_").length);
        System.out.println(test1.split("_").length);
        System.out.println(test2.split("_").length);
    }

    private static int getCenter(int columns) {
        return columns / 2;
    }

}
