package me.yummykang;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        FetcherInit.start();
        SpiderInit.start();
    }
}
