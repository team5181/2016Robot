package org.usfirst.frc.team5181.sensors;

import org.usfirst.frc.team5181.robot.Statics;
import edu.wpi.first.wpilibj.Ultrasonic;

public class SonicRangeSensor {

	int ULTRASONIC_ECHO_PULSE_OUTPUT; //Currently 4
	int ULTRASONIC_TRIGGER_PULSE_INPUT; //Currently 3
	Ultrasonic ultra;

	public SonicRangeSensor(int echoOut, int triggerIn) {
		ULTRASONIC_ECHO_PULSE_OUTPUT = echoOut;
		ULTRASONIC_TRIGGER_PULSE_INPUT = triggerIn;
		try { 
			ultra = new Ultrasonic(ULTRASONIC_ECHO_PULSE_OUTPUT,
			ULTRASONIC_TRIGGER_PULSE_INPUT, Ultrasonic.Unit.kInches);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		restartSensor();
		ultra.setAutomaticMode(true);
	}

	/**
	 * Get range in Inches
	 * @return double
	 * @return 0.0314159 -> error
	 */
	public double getRangeInches() {
		if (ultra.isRangeValid()) {
			return ultra.getRangeInches();
		} else {
			return 0.0314159;
		}
	}
	
	/**
	 * Get range in Foot
	 * @return double
	 * @return 0.0314159 -> error
	 */
	public double getRangeFt() {
		if (ultra.isRangeValid()) {
			return getRangeInches() * 0.0833333;
		} else {
			return 0.0314159;
		}
	}

	/**
	 * Get range in Yards
	 * @return double
	 * @return 0.0314159 -> error
	 */
	public double getRangeYd() {
		if (ultra.isRangeValid()) {
			return getRangeInches() * 0.0277778;
		} else {
			return 0.0314159;
		}
	}


	/**
	 * Get range in Milimeter
	 * @return double
	 * @return 0.0314159 -> error
	 */
	public double getRangeMm() {
		if (ultra.isRangeValid()) {
			return ultra.getRangeMM();
		} else {
			return 0.0314159;
		}
	}
	

	/**
	 * Get range in Centimeter
	 * @return double
	 * @return 0.0314159 -> error
	 */
	public double getRangeCm() {
		if (ultra.isRangeValid()) {
			return getRangeMm() * 0.1;
		} else {
			return 0.0314159;
		}
	}
	
	/**
	 * Get range in Dm
	 * @return double
	 * @return 0.0314159 -> error
	 */
	public double getRangeDm() {
		if (ultra.isRangeValid()) {
			return getRangeMm() * 0.01;
		} else {
			return 0.0314159;
		}
	}
	
	/**
	 * Get range in Meters
	 * @return double
	 * @return 0.0314159 -> error
	 */
	public double getRangeMeters() {
		if (ultra.isRangeValid()) {
			return getRangeMm() * 0.001;
		} else {
			return 0.0314159;
		}
	}

	public Ultrasonic.Unit feederPID() {
		return ultra.getDistanceUnits();
	}

	public void updateValues() {
		ultra.updateTable();
	}

	public void restartSensor() {
		if (ultra.isEnabled()) {
			ultra.setEnabled(false);
			ultra.setEnabled(true);
		} else {
			ultra.setEnabled(true);
		}
	}
	/**
	 * @param Using multiple range sensors.
	 * We don't want hardcoded results while we still don't want to use an associative array,
	 * so we use a nested class to call to the objects.
	 */
	public static class UseRangeSensors {
		public SonicRangeSensor srFront, srBack, srRight, srLeft;
		public UseRangeSensors() {
			this.srFront = new SonicRangeSensor(Statics.FRONT_Ultra_Echo, Statics.FRONT_Ultra_Trigger);
			this.srBack = new SonicRangeSensor(Statics.BACK_Ultra_Echo, Statics.BACK_Ultra_Trigger);
			this.srRight = new SonicRangeSensor(Statics.RIGHT_Ultra_Echo, Statics.LEFT_Ultra_Trigger);
			this.srLeft = new SonicRangeSensor(Statics.LEFT_Ultra_Echo, Statics.LEFT_Ultra_Trigger);
			
		}
	}

}
