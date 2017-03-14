/*
 * L&N STEMpunks c 2017
 *
 * WOPR-JR.
 *
 * Full repo: github.com/ln-stempunks/WOPR-JR
 *
 * Full licensing here: programming.lnstempunks.org/licensing
 *
 * GPLv3
 */
package team3966.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3966.robot.commands.AlignToGearPeg;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.util.logging.Level;
import java.util.logging.Logger;
import team3966.robot.commands.AutonomousCenter;
import team3966.robot.commands.AutonomousLeft;
import team3966.robot.commands.AutonomousRight;
import team3966.robot.commands.DoNothing;
import team3966.robot.subsystems.Subsystems;
import team3966.robot.commands.DriveCircle;
import team3966.robot.commands.TankDrive;
import team3966.robot.commands.TankDriveAngle;
import team3966.robot.commands.TankDriveDistance;
import team3966.robot.commands.MoveToGearPeg;
import team3966.robot.commands.SetSolenoids;
import team3966.robot.commands.TankDriveTimed;

/**
 * Our 2017 robot code
 *
 * Once we have a working robot, we will fork this and make a MecanumBot repo
 *
 */
public class Robot extends IterativeRobot {

    public static Subsystems subsystems;
    SendableChooser autoChooser;
    NetworkTable nt;
    Command autonomousCommand;
    Command teleopCommand;

    public void robotInit() {
        nt = NetworkTable.getTable("vision");
        subsystems = new Subsystems();
        teleopCommand = new TankDrive();
        autoChooser = new SendableChooser();
        autoChooser.addObject("Do Nothing", new DoNothing(15));
  
        //autoChooser.addObject("Circle (1m)", new DriveCircle(1));
        //autoChooser.addObject("Circle (2m)", new DriveCircle(2));

        autoChooser.addObject("Drive forward (slow)", new TankDriveTimed(.5, .5, 3));
        //autoChooser.addObject("Drive forward (1m)", new TankDriveDistance(1, 1));
        //autoChooser.addObject("Drive forward (2m)", new TankDriveDistance(2, 2));
        //autoChooser.addObject("Turn (90 d)", new TankDriveAngle(90));
        
        autoChooser.addObject("Auto Center", new AutonomousCenter());
        autoChooser.addObject("Auto Left", new AutonomousLeft());
        autoChooser.addObject("Auto Right", new AutonomousRight());
        /*
        autoChooser.addObject("Align To Gearpeg", new AlignToGearPeg());
        autoChooser.addObject("Move To Gearpeg", new MoveToGearPeg());*/
        
        //autoChooser.addObject("Set Pnem", new SetSolenoids(true, false));
        //autoChooser.addObject("Auto Left !RISKY!", new AutonomousLeft());
        //autoChooser.addObject("Align to Gear ", new AlignToGearPeg());
        //autoChooser.addObject("Move to Gear ", new MoveToGearPeg());
        /*
        autoChooser.addObject("3 Left Hoppers Left Position", new LeftSide());
        autoChooser.addObject("3 Left Hoppers Middle Position", new MiddleSide());
        autoChooser.addObject("3 Left Hoppers Right Position", new RightSide());
        autoChooser.addObject("2 Left Hoppers Left Position", new Hopper2Left());
        autoChooser.addObject("2 Left Hoppers Middle Position", new Hopper2Middle());
        autoChooser.addObject("2 Left Hoppers Right Position", new Hopper2Right());
         */
        SmartDashboard.putData("Auto Program", autoChooser);
        //DriverStation.Alliance color = DriverStation

    }

    public void disabledInit() {
        
    }

    public void autonomousInit() {
        System.out.printf("Autonomous mode\n");
        //teleopCommand.cancel();
        autonomousCommand = (Command) autoChooser.getSelected();
        if (autonomousCommand != null) {
            autonomousCommand.start();
        }
    }

    public void teleopInit() {
        System.out.printf("Teleop mode\n");
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }

        if (teleopCommand != null) {
            teleopCommand.start();
        }
    }

    public void autonomousPeriodic() {
        //subsystems.sensors.pdp.startLogging();
        Scheduler.getInstance().run();
        subsystems.dumpInfo();
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        subsystems.dumpInfo();
    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
        subsystems.drive.stop();
        subsystems.dumpInfo();
    }

}
