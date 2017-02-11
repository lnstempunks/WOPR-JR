package team3966.robot.subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDController;

import team3966.robot.pidcontrollers.MotorPIDOutput;
import team3966.robot.pidcontrollers.MotorPIDSource;

import team3966.robot.hardware.DriveMotor;
import team3966.robot.hardware.MotorEncoder;

import team3966.robot.values.IDs;
import team3966.robot.commands.TankDrive;

/**
 * Drive subsystem, controls left and right motors.
 *
 * Supplies methods for controlling the drive by various functions. The motors
 * objects should not be directly set from any other place.
 */

public class Drive extends Subsystem {
	

	public DriveMotor L0, L1, R0, R1;
	public MotorEncoder Lenc, Renc;

	public MotorPIDSource Lsource, Rsource;
	public MotorPIDOutput Lout, Rout;
	
	public PIDController LPID, RPID;
	
	// PID constants
	public static final double kLP = 0.24;
	public static final double kLI = 0.15;
	public static final double kLD = 0.2;
	public static final double kLF = 0.0;

	public static final double kRP = 0.29;
	public static final double kRI = 0.16;
	public static final double kRD = 0.15;
	public static final double kRF = 0.0;
	
	
	public Drive(boolean _usePID) {
		Lenc = new MotorEncoder(IDs.L_encoder_dio);
		Renc = new MotorEncoder(IDs.R_encoder_dio);
		
		Renc.setReverseDirection(true);
		
		L0 = new DriveMotor(IDs.L0_motor);
		L1 = new DriveMotor(IDs.L1_motor);
		R0 = new DriveMotor(IDs.R0_motor);
		R1 = new DriveMotor(IDs.R1_motor);
		
		Lsource = new MotorPIDSource(Lenc);
		Rsource = new MotorPIDSource(Renc);
		
		Lout = new MotorPIDOutput(L0, L1);
		Rout = new MotorPIDOutput(R0, R1);
		
		LPID = new PIDController(kLP, kLI, kLD, kLF, Lsource, Lout);
		RPID = new PIDController(kRP, kRI, kRD, kRF, Rsource, Rout);
			
		LPID.setOutputRange(-1, 1);
		RPID.setOutputRange(-1, 1);
	}
	
	public Drive() {
		this(false);
	}


	public void initDefaultCommand() {
		setDefaultCommand(new TankDrive());
	}
	
	public void useSpeed() {
		LPID.setInputRange(-MotorEncoder.MAX_SPEED, MotorEncoder.MAX_SPEED);
		RPID.setInputRange(-MotorEncoder.MAX_SPEED, MotorEncoder.MAX_SPEED);
		
		LPID.setAbsoluteTolerance(MotorEncoder.MAX_TOLERANCE_SPEED);
		RPID.setAbsoluteTolerance(MotorEncoder.MAX_TOLERANCE_SPEED);
		
		Lsource.useSpeed();
		Rsource.useSpeed();
	}
	
	public void useDistance() {
		LPID.setInputRange(-MotorEncoder.MAX_DISTANCE, MotorEncoder.MAX_DISTANCE);
		RPID.setInputRange(-MotorEncoder.MAX_DISTANCE, MotorEncoder.MAX_DISTANCE);
		
		LPID.setAbsoluteTolerance(MotorEncoder.MAX_TOLERANCE_DISTANCE);
		RPID.setAbsoluteTolerance(MotorEncoder.MAX_TOLERANCE_DISTANCE);
		
		Lsource.useDistance();
		Rsource.useDistance();
	}
	
	public void turnOnPID() {
		LPID.enable();
		RPID.enable();
	}
	
	public void turnOffPID() {
		LPID.disable();
		RPID.disable();
	}
	
	public boolean isPIDEnabled() {
		return (LPID.isEnabled() || RPID.isEnabled());
	}

	public void stop() {
		turnOffPID();
		tank_power(0, 0);
	}
	
	// using raw power
	public void tank_power(double L_power, double R_power) {
		turnOffPID();

		L0.set(L_power);
		L1.set(L_power);
		
		R0.set(R_power);
		R1.set(R_power);
	}

	public void tank_speed(double L_s, double R_s) {
		if (!isPIDEnabled()) {
  			turnOnPID();
		}
		
		useSpeed();

		LPID.setSetpoint(L_s);
		RPID.setSetpoint(R_s);
	}

	public void tank_dist(double L_d, double R_d) {
		if (!isPIDEnabled()) {
  			turnOnPID();
		}
		
		useDistance();
		
		LPID.setSetpoint(L_d + Lenc.getDistance());
		RPID.setSetpoint(R_d + Renc.getDistance());
	}
}
