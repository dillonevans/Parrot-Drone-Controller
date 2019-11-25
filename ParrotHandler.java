import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.nio.*;

/**
 * The Model for the Drone
 */
public class ParrotHandler
{
    
    static Socket socket;
    static PrintWriter out;

    public ParrotHandler()
    {
        try
        {
            String path = "private-lear.cs.okstate.edu";
            socket = new Socket(path, 9095);
            out = new PrintWriter(socket.getOutputStream());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Passes a JSON String to the Drone
     * @param command The JSON Command to Initiate
     */
    public static void command(String command)
    {
        out.write(command);
        out.flush();
    }
}