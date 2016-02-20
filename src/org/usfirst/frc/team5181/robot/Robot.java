package org.usfirst.frc.team5181.robot;

import sensors.LimitSwitch;
import sensors.Potentiometer;
import sensors.RevX;
import actuators.BallPickup;
import actuators.LadderArm;
import actuators.LinearActuator;
import autonomousThreads.ActionBased;
import autonomousThreads.Autonomous;
import autonomousThreads.FrequencyAutonomous;
import autonomousThreads.TimedAutonomous;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalOutput;

public class Robot extends SampleRobot {
	
	//Speed Limit vars
	private static double speedLimit = .6; 
	
	//General vars
	ActionBased recorder;
	DriverStation ds = DriverStation.getInstance();
	Autonomous auton;
	DriveTrain drive;
	
	//Special
	Bear koala;
	SimpleClient client;
	
	//Sensors
	RevX revX;
	
	//Actuators
	BallPickup ballPickUp;
	LadderArm arm;
	
	//Recorder Vars
	final long timeFrequency = 500; //in actions/second
	final long period = 5; //in MS
	boolean isRecording;
	
	//Sensors
	Potentiometer potent;
	LimitSwitch limitSwitch;

	private boolean ballTracker;

	private boolean clientStarted;

	
	public void robotInit(){ 
		auton = new TimedAutonomous(this);
		recorder = new ActionBased();
		drive = new DriveTrain(speedLimit);
		
		//Sensors
		revX = new RevX(SPI.Port.kMXP);
		
		//Actuators
		ballPickUp = new BallPickup();
		arm = new LadderArm(6, 7);
		
		koala = new Bear();
		ballTracker = false;
	}
	
	public void autonomous() {
		auton.actionPlayback("/var/rcrdng/autonRecording4.rcrdng", timeFrequency);
		while(this.isAutonomous()) {
			auton.setAutonState(this.isAutonomous());
		}
	}
	
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
			recording();
			teleopMaster(false);
		}
	}
	public void teleopMaster(boolean inAuton) {	
		if (!inAuton) {
			Gamepad.setNaturalState();
		}
		
		ballPickUp.setBallIntake(Gamepad.RIGHT_Trigger_State, Gamepad.LEFT_Stick_Y_State);
		
//		if (!clientStarted) {
//			client = new SimpleClient();
//			clientStarted = true;
//		}
		
		if (Gamepad.B_Button_State && !ballTracker) {
			ballTracker = true;
		}
		if (Gamepad.X_Button_State && ballTracker) {
			ballTracker = false;
		}
		
		if (ballTracker) {
			int currX = client.centerX;
			int currY = client.centerY;
			DriverStation.reportError("\n(" + currX + ", " + currY + ")", false);
			if (currX == -1) {
				drive.tankDrive(0, 0);
			}
			else if (currX > 320) {
				drive.tankDrive(0, 0);
			}
			else {
				drive.tankDrive(0, 0);
			}
		}
		
		if(revX.hadCollision()) {
			DriverStation.reportError("Collision\n", false);
		}
		
		//Ladder
		if(Gamepad.A_Button_State) {
			arm.extend(LadderArm.extensionPositions.CONTRACT, 1);
		}
		
		if(Gamepad.Y_Button_State) {
			arm.extend(LadderArm.extensionPositions.EXTEND, 1);
		}
		
		if(!Gamepad.A_Button_State && !Gamepad.Y_Button_State) {
			arm.extendFree(0);
		}
		
		arm.rotateFree(-Gamepad.LEFT_Stick_Y_State, 0.4);
		//End ladder
		
		drive.updateSpeedLimit(Gamepad.RIGHT_Bumper_State, Gamepad.LEFT_Bumper_State, Gamepad.B_Button_State);
		drive.ArcadeDrive(Gamepad.RIGHT_Stick_X_State, Gamepad.RIGHT_Stick_Y_State);
	}
	
	
	/**
	 * Controls the starting and stopping of the recorder
	 */
	public void recording() {	
		if(Gamepad.START_State && !isRecording) {
			isRecording = true;
			   
			recorder.startRecording(true, period);
			
			DriverStation.reportError("Started\n", false); 	
		}
		
		if(Gamepad.BACK_State && isRecording)  {
			isRecording = false;
			recorder.stopRecording();
		}

		if(Gamepad.LEFT_Stick_DOWN_State) {
			recorder.incrementRecording();
		}
	}
}
