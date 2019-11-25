/**
 * Author: Dillon Evans
 * Program: Parrot.java
 * Description: A simple drone controller app
 * using the Parrot AR Drone courtesy of 
 * Oklahoma State University
 */
import java.io.*;
import java.net.*;

/**
 * The Main Class for the Drone
 */
public class Parrot
{
    public static void main(String[] args)
    {
        ParrotHandler handler = new ParrotHandler();
        ParrotFrame frame = new ParrotFrame(handler);
    }
}