import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * The Controller for the Drone
 */
public class ParrotFrame extends JFrame implements ActionListener
{
    ArrayList<JButton> buttons = new ArrayList<JButton>();
    ParrotHandler handler;
    
    static double x1 = 0, x2 = 0, y1 = 0, y2 = 0, z1 = 0, z2 = 0;
    static double speedMod = 0;
    static JPanel controlPanel = new JPanel();
    static JPanel panel = new JPanel();
    static JButton reset = new JButton("Reset");
    static JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0 , 4, 2);
    static GridLayout gLayout = new GridLayout(5, 2);
    static BorderLayout bLayout = new BorderLayout();
    
    public ParrotFrame(ParrotHandler handler)
    {
        super();
        this.handler = handler;
        
        //Set up the Form
        setVisible(true);
        setLayout(bLayout);
        setSize(800, 800);
        setTitle("Drone Controller");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        buttons.add(new JButton("Take Off"));
        buttons.add(new JButton("Land"));
        buttons.add(new JButton("Move Left"));
        buttons.add(new JButton("Move Right"));
        buttons.add(new JButton("Move Up"));
        buttons.add(new JButton("Move Down"));
        buttons.add(new JButton("Increase Alt"));
        buttons.add(new JButton("Decrease Alt"));
        buttons.add(new JButton("Rotate CounterClockwise"));
        buttons.add(new JButton("Rotate Clockwise"));
        
        //Add the Controls to the Controller
        for (JButton button: buttons)
        {
            panel.add(button);
            button.addActionListener(this);
        }
        
        //Add the Content to the Form
        panel.setLayout(gLayout);;
        controlPanel.add(new JLabel("Speed: "));
        controlPanel.add(speedSlider);
        controlPanel.add(reset);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        reset.addActionListener(this);
        add(controlPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }
    
   
    @Override
    public void actionPerformed(ActionEvent e)
    {
        speedMod = (double)(speedSlider.getValue() * 0.75);
        double posSpeed = 0.25;
        double negSpeed = -0.25;
        double linPosVel = posSpeed + (posSpeed * speedMod);
        double linNegVel = negSpeed + (negSpeed * speedMod);
        double angPosVel = 1 + (1 * speedMod);
        double angNegVel = -1 + (-1 * speedMod);
        Object source = e.getSource();
        JButton temp = (JButton)source;
        String command = temp.getText();
        String droneCommand = " ";
        
        if (temp.equals(reset))
        {
            speedMod = 0;
            speedSlider.setValue(2);
            droneCommand = generateCommand(x1, y1, z1, x2, y2, z2);
        }
        //Based on the Button clicked, Perform the Corresponding Command
        switch(command)
        {
            case ("Take Off"):
            {
                droneCommand = "{\"op\":\"publish\",\"topic\":\"/ardrone/takeoff\",\"msg\":{}}";
                break;
            }

            case ("Land"):
            {
                droneCommand = "{\"op\":\"publish\",\"topic\":\"/ardrone/land\",\"msg\":{}}";
                break;
            }
            case ("Increase Alt"):
            {
                z1 = linPosVel;
                droneCommand = generateCommand(x1, y1, z1, x2, y2, z2);
                break;
            }
            case ("Decrease Alt"):
            {
                z1 = linNegVel;
                droneCommand = generateCommand(x1, y1, z1, x2, y2, z2);
                break;
            }
            case ("Move Right"):
            {
                y1 = linNegVel; 
                droneCommand = generateCommand(x1, y1, z1, x2, y2, z2);
                break;
            }
            case ("Move Left"):
            {
                y1 = linPosVel; 
                droneCommand = generateCommand(x1, y1, z1, x2, y2, z2);
                break;
            }
            case ("Move Up"):
            {
                x1 = linPosVel;
                droneCommand = generateCommand(x1, y1, z1, x2, y2, z2);
                break;
            }
            case ("Move Down"):
            {
                x1 = linNegVel;
                droneCommand = generateCommand(x1, y1, z1, x2, y2, z2);
                break;
            }
            case ("Rotate Clockwise"):
            {
                z2 = angNegVel;
                droneCommand = generateCommand(x1, y1, z1, x2, y2, z2);
                break;
            }
            case ("Rotate CounterClockwise"):
            {
                z2 = angPosVel;
                droneCommand = generateCommand(x1, y1, z1, x2, y2, z2);
                break;
            }
        }
        //Pass the command to the Drone
        try
        {
            ParrotHandler.command(droneCommand);
            x1 = 0; x2 = 0;
            y1 = 0; y2 = 0;
            z1 = 0; z2 = 0;
            Thread.sleep(800);
            droneCommand = generateCommand(x1, y1, z1, x2, y2, z2);
            ParrotHandler.command(droneCommand);
        }
        catch (Exception a)
        {
            System.out.println(a.getMessage());
        }
    }

    /**
     * Generates a new command to pass to the AR Drone
     * @param x1 Linear X Velocity
     * @param y1 Linear Y Velocity
     * @param z1 Linear Z Velocity
     * @param x2 Angular X Velocity
     * @param y2 Angular Y Velocity
     * @param z2 Angular Z Velocity
     * @return The JSON command to pass to the drone
     */
    public String generateCommand(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        String droneCommand ="{\"op\":\"publish\",\"topic\":\"/cmd_vel\",\"msg\":{\"linear\":{\"x\":"+x1+",\"y\":"+y1+",\"z\":"+z1+"},\"angular\":{\"x\":"+x2+",\"y\":"+y2+",\"z\":"+z2+"}}}";
        return droneCommand;
    }
    
}