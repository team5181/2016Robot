package org.usfirst.frc.team5181.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;

public class DriveTrain {
	
	private double speedLimit;
	private boolean speedBool;
	Victor leftFront;
	Victor leftBack;
	Victor rightFront;
	Victor rightBack;
	RobotDrive drive;
	
	public DriveTrain(double speedLimit) {
		leftFront = new Victor(Statics.LEFTPortFront);
		leftBack = new Victor(Statics.LEFTPortBack);
		rightFront = new Victor(Statics.RIGHTPortFront);
		rightBack = new Victor(Statics.RIGHTPortBack);
		
		drive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
		
		this.speedLimit = speedLimit;
	}
	
	/**
	 * Single joystick to control robot drive
	 * @param controlStickX magnitude of turning axis
	 * @param controlStickY magnitude of driving axis
	 */
	public void arcadeDrive(double controlStickX, double controlStickY) {
		double xRotation = controlStickX;
		double yDrive    = controlStickY;
		
		// 2016 - 9 fix: Add variable for turning so that turning gets dumb when high-speed driving.
		// xRotation = Filters.powerCurving(controlStickX, Filters.determineControlY(yDrive)); //filter rotational values
		if(Math.abs(yDrive) >= 0.5) {
			xRotation = xRotation * 0.6; // TEMP FIX q. JACKASS HENRY
		} else {
			// do nil
		}
		
		if(Math.abs(yDrive) >= speedLimit) {
			yDrive = ((yDrive < 0)? -1 : 1) * speedLimit;
		}
		if(Math.abs(xRotation) >= speedLimit) {
			xRotation = ((xRotation < 0)? -1 : 1) * speedLimit;
		}
		drive.arcadeDrive(-yDrive, -(xRotation));
	}
	
	public void tankDrive(double controlStickLeft, double controlStickRight) {
		if(controlStickLeft >= speedLimit) {
			controlStickLeft = speedLimit;
		}
		if(controlStickRight >= speedLimit) {
			controlStickRight = speedLimit;
		}
		
		drive.tankDrive(controlStickLeft, controlStickRight);
	}
	public void updateSpeedLimit(boolean increase, boolean decrease, boolean stop) {

		//Speed Limit Control
		if(increase && !speedBool) {
			speedLimit += 0.05;
			speedBool = true;
		}
		else if(stop) {
			speedLimit = 0;
		}
		else if(decrease && !speedBool) {
			speedLimit -= 0.05;
			speedBool = true;
		}
		else if(!decrease && !increase && speedBool) {
			speedBool = false;
		}
		
	}
	
}
