package autonomousThreads;

import org.usfirst.frc.team5181.robot.DriveTrain;
import org.usfirst.frc.team5181.robot.Robot;

import sensors.RevX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDFunctions implements PIDOutput {
	Robot robot;
	DriveTrain drive;
	
	public PIDController pidiR, pidiD;
	public GyroSource gyroPID;
	public DisplacementSource displacePID;
	
	static double kPr = 0.15, kPd = 0.06; 
	static double kIr = 0.00004, kId = 0.00; 
	static double kDr = 0.1, kDd = 0.00; 
	static double kFr = 0.00, kFd = 0.00; 
	
	static final double toleranceRotation = 0.5;
	static final double toleranceDistance = .5;
	
	double pidValue = 0;
	
	public PIDFunctions(Robot r, DriveTrain drive) {
		robot = r;
		this.drive = drive;
		
		gyroPID = new GyroSource(r.getRevX());
		displacePID = new DisplacementSource(r.getRevX());
		
		pidiR = new PIDController(kPr, kIr, kDr, kFr, gyroPID, this);
		
		pidiR.setInputRange(-180.0, 180.0);
		pidiR.setOutputRange(-1, 1);
		pidiR.setAbsoluteTolerance(toleranceRotation);
		pidiR.setContinuous(true);
		
		pidiD = new PIDController(kPr, kIr, kDr, kFr, displacePID, this);
		pidiD.setInputRange(-3, 3);
		pidiD.setOutputRange(-1, 1);
		pidiD.setAbsoluteTolerance(toleranceDistance);
		pidiD.setContinuous(true);
		
	}
	public void turnToAngle(double angle) {
		pidiR.setSetpoint(angle);
		pidiR.enable();
		drive.arcadeDrive(pidValue, 0);
	}
	public void moveTo(double distance) {
		
		pidiD.setSetpoint(distance);
		pidiD.enable();
		
		drive.arcadeDrive(pidValue, pidValue);
	}
	
	public void pidWrite(double output) {
		pidValue = output;
	}
	
	
	public class GyroSource implements PIDSource {
		
		RevX revX;
		
		public GyroSource(RevX r) {
			revX = r;
		}
		
		public void setPIDSourceType(PIDSourceType pidSource) {
			//do nothing lololololz
		}

		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kDisplacement;
		}

		public double pidGet() {
			return revX.getYaw();
		}
	}
	
	class DisplacementSource implements PIDSource {

		RevX revX;
		
		public DisplacementSource(RevX r) {
			revX = r;
		}
		
		public void setPIDSourceType(PIDSourceType pidSource) {
			//do nothing lololololz
		}

		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kDisplacement;
		}

		/**
		 * returns the hypotenuse
		 */
		public double pidGet() {
			//return Math.sqrt(Math.pow(revX.getDisplacementX(), 2) + Math.pow(revX.getDisplacementY(), 2));
			return revX.getDisplacementX();
		}
		
	}
}