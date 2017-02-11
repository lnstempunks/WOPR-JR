package team3966.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import team3966.robot.Robot;
import team3966.robot.hardware.MotorEncoder;
import team3966.robot.pidcontrollers.DistancePID;
import team3966.robot.pidcontrollers.PIDOutputArray;
import team3966.robot.subsystems.Subsystems;
import team3966.robot.values.PS4Buttons;
import team3966.robot.hardware.Controller;


public class TankDriveTimed extends BaseCommand {
	
	private Controller cont;
	private Subsystems systems;
	
	private long stime;
	
	private double speed, time;
	private boolean hasStarted = false;
	
	public TankDriveTimed(double _speed, double _time) {
		super(Robot.subsystems.drive);
		systems = Robot.subsystems;
		cont = systems.OI.controller;
		speed = _speed;
		time = _time;
		stime = System.nanoTime();
	}

	protected void execute() {
		systems.drive.tank_speed(speed, speed);
		
	}
	
	protected boolean isFinished() {
		return (System.nanoTime() - stime) * Math.pow(10,  -9) > time;
	}
	
	protected void interrupted() {
		systems.drive.turnOffPID();
	}
	
	protected void end() {
		systems.drive.turnOffPID();
	}

}
