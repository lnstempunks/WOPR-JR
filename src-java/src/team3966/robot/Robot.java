/*
	2017 L&N STEMpunks

	lnstempunks.org

Authors:
	Cade Brown <cade@cade.site>

Website:
	programming.lnstempunks.org (or ln-stempunks.github.io)

 */
package team3966.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

import team3966.robot.subsystems.Subsystems;
import team3966.robot.commands.Autonomous;
import team3966.robot.commands.MecDrive;
import team3966.robot.commands.StopDrive;
import team3966.robot.commands.TankDrive;
import team3966.robot.commands.TankDriveTimed;

/**
 * Our 2017 robot code
 * 
 * Once we have a working robot, we will fork this and make a MecanumBot repo
 *
 */

public class Robot extends IterativeRobot {

	public static Subsystems subsystems;
	
	Command autonomousCommand;
	Command teleopCommand;

	public void robotInit() {
		subsystems = new Subsystems();

		// teleopCommand = new TankDrive();
		teleopCommand = new TankDrive();

		// stay still during autonomous
		autonomousCommand = new Autonomous();
	}

	/*
	
		These functions are called right before autonomous/teleop
	
	 */
	public void disabledInit() {

	}

	public void autonomousInit() {
		System.out.printf("Autonomous mode\n");
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

	/**
	 * This function is called periodically during autonomous/teleop
	 */
	public void autonomousPeriodic() {
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
