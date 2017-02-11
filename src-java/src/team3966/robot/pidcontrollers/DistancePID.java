package team3966.robot.pidcontrollers;

import team3966.robot.hardware.MotorEncoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class DistancePID implements PIDSource {
	
	private MotorEncoder enc;
	private PIDSourceType sourceType = PIDSourceType.kDisplacement;
	private double scale;
	
	public DistancePID(double sc, MotorEncoder _e) {
		enc = _e;
		scale = sc;
	}

	public PIDSourceType getPIDSourceType() {
		return sourceType;
	}

	public double pidGet() {
		return enc.getDistance()*scale;
	}

	public void setPIDSourceType(PIDSourceType arg0) {
		sourceType = PIDSourceType.kDisplacement;
		
	}
}