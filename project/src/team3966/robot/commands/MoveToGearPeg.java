package team3966.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.logging.Level;
import java.util.logging.Logger;
import team3966.robot.Robot;
import team3966.robot.hardware.MotorEncoder;
import team3966.robot.subsystems.Subsystems;
import team3966.robot.values.PS4Buttons;
import team3966.robot.hardware.Controller;
import team3966.robot.hardware.DriveMotor;
import team3966.robot.pidcontrollers.MotorPIDOutput;
import team3966.robot.pidcontrollers.MotorPIDSource;
import team3966.robot.pidcontrollers.MotorTurnAndMovePIDOutput;
import team3966.robot.pidcontrollers.MotorTurnPIDOutput;
import team3966.robot.pidcontrollers.NetworkTablePIDSource;

public class MoveToGearPeg extends BaseCommand {

    private Controller cont;
    private Subsystems systems;

    private PIDController PID;

    private NetworkTablePIDSource source;

    private double[] vals = new double[20];
    private int valsIdx = 0;

    // PID constants
    public static final double kP = 0.0016;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    public static final double CAMERA_WIDTH = 320;
    public static final double MIDDLE_OF_CAMERA = 160;

    public MoveToGearPeg() {
        super(Robot.subsystems.drive);
        systems = Robot.subsystems;
        cont = systems.OI.controller;

        source = new NetworkTablePIDSource("vision/gearpeg", "x");

        MotorTurnAndMovePIDOutput out = new MotorTurnAndMovePIDOutput(
                new DriveMotor[]{
                    systems.drive.L0, systems.drive.L1
                },
                new DriveMotor[]{
                    systems.drive.R0, systems.drive.R1
                }, -.25, -.25
        );
        
        PID = new PIDController(kP, kI, kD, source, out);
        PID.setToleranceBuffer(8);
        
        //PID.setInputRange(-1, NetworkTable.getTable("vision/gearpeg").getNumber("camwidth", 320));
        PID.setInputRange(-1, CAMERA_WIDTH);
        PID.setOutputRange(-.03, .03);

        PID.setAbsoluteTolerance(3);

        //systems.drive.turnOffPID();
    }

    protected void initialize() {
        PID.enable();
        PID.setSetpoint(MIDDLE_OF_CAMERA);
        //double width = (NetworkTable.getTable("vision/gearpeg").getNumber("camwidth", 320));
        //PID.setInputRange(0, width);
        //PID.setSetpoint(width / 2.0);
    }

    protected void execute() {
        vals[valsIdx] = source.lastVal;
        valsIdx = (valsIdx + 1) % 20;
        //PID.setSetpoint();
        /*if (PID.onTarget()) {
            end();
        }*/
    }


    protected boolean isFinished() {
        return systems.sensors.ultrasonic.getDistance() <= .5;
        //return PID.get() < 0;
    }

    protected void interrupted() {
        end();
    }

    protected void end() {
        PID.disable();
        systems.drive.stop();
    }

}