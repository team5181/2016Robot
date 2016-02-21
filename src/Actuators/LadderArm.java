package actuators;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Talon;
import sensors.LimitSwitch;

public class LadderArm {
	Talon armRotationController, armExtensionController;
	LimitSwitch BoundsMax, BoundsMin; //extension
	
	/**
	 * Controlled movement 
	 * @param rotationPort rotation motor port
	 * @param extensionPort extension motor port
	 * @param limitMaxPort max extension
	 * @param limitMinPort min extension
	 * @param limit15Port 15" extension
	 */
	public LadderArm(int rotationPort, int extensionPort, int limitMaxPort, int limitMinPort) {
		BoundsMin = new LimitSwitch(limitMinPort);
		BoundsMax = new LimitSwitch(limitMaxPort);
		
		armRotationController = new Talon(rotationPort); 
		armExtensionController = new Talon(extensionPort);
	}
	
	/**
	 * Free movement
	 * @param rotationPoer
	 * @param extensionPort
	 */
	public LadderArm(int rotationPort, int extensionPort) {
		armRotationController = new Talon(rotationPort); 
		armExtensionController = new Talon(extensionPort);
	}
	
	 /**
	  * Control without limit switches
	  * @param value
	  * @param speedLimit
	  */
	public void rotateFree(double value, double speedLimit) {
		if(value <= speedLimit && value > 0.05) {
			armRotationController.set(speedLimit);
		}
		else if(value <= speedLimit && value < 0.05) {
			armRotationController.set(speedLimit / 5);
		}
		else if(value < 0.05) {
			armRotationController.set(value / 5); 
		}
		else if(value > 0.05) {
			armRotationController.set(value);
		}
		else {
			armRotationController.set(0);
		}
	}
	
	/**
	 * Limit by encoder
	 * @param value
	 * @param speedLimit
	 */
	public void rotate(double value, double speedLimit) {
		if(value <= speedLimit && value > 0.05) {
			armRotationController.set(speedLimit);
		}
		else if(value <= speedLimit && value < 0.05) {
			armRotationController.set(speedLimit / 5);
		}
		else if(value < 0.05) {
			armRotationController.set(value / 5); 
		}
		else if(value > 0.05) {
			armRotationController.set(value);
		}
		else {
			armRotationController.set(0);
		}
		
		/*
		 * Encoder goes here
		 */
	}
	
	public enum extensionDirections {
		EXTEND, CONTRACT;
	}
	
	
	public void extend(extensionDirections direction, double magnitude) {
		if(direction == extensionDirections.EXTEND && Math.abs(magnitude) > 0.1) {
			armExtensionController.set(Math.abs(magnitude));
		}
		if(direction == extensionDirections.CONTRACT && Math.abs(magnitude) > 0.1) {
			armExtensionController.set(-Math.abs(magnitude));
		}
		
		if(BoundsMin != null && BoundsMax != null) {
			//Limit Switches
			if(BoundsMin.get()) {
				armExtensionController.set(0);
			}
			else if(BoundsMax.get()) {
				armExtensionController.set(0);
			}
		}
	}
	
	public void extendFree(double mag) {
		armExtensionController.set(mag);
	}
}
