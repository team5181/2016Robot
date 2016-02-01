package org.firs.frc.team5181.Recoder;

import org.usfirst.frc.team5181.robot.Gamepad;
import org.usfirst.frc.team5181.robot.Statics;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

import java.io.*;
import java.net.Socket;

public class ActionBased extends Thread {
	
	String recording;
	DriverStation ds;
	long timeStep;
	boolean isRecording;
	
	public ActionBased(DriverStation ds, long step) {
		recording = "";
		this.ds = ds;
		isRecording = false;
		timeStep = step;
	}
	
	private void recordAction(int button, double magnitude) {
		recording += button + ":" + magnitude + ";";
	}
	
	private void sendActions() {
		DriverStation.reportError(recording, false);
		
		try {
			//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter((new Socket("LSCHS-ROBOTICS", 5800).getOutputStream())));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/var/rcrdng/autonRecording3.rcrdng")));
			bw.write(recording);
		
		}
		catch(Exception e) {
			DriverStation.reportError(e.getMessage(), false);
		}
	}
	
	public void startRecording() {
		isRecording = true;
		this.start();
	}
	
	public void record() {
		
		//for buttons
		recordAction(Statics.A_Button, toDouble(Gamepad.A_Button_State));
		recordAction(Statics.B_Button, toDouble(Gamepad.B_Button_State));
		recordAction(Statics.X_Button, toDouble(Gamepad.X_Button_State));
		recordAction(Statics.Y_Button, toDouble(Gamepad.Y_Button_State));
		recordAction(Statics.RIGHT_Bumper, toDouble(Gamepad.RIGHT_Bumper_State));
		recordAction(Statics.LEFT_Bumper, toDouble(Gamepad.LEFT_Bumper_State));
		
		//for triggers/analog sticks
		recordAction(12, Gamepad.LEFT_Stick_Y_State);
		recordAction(11, Gamepad.LEFT_Stick_X_State);
		recordAction(16, Gamepad.RIGHT_Stick_Y_State);
		recordAction(15, Gamepad.RIGHT_Stick_X_State);
		recordAction(14, Gamepad.RIGHT_Trigger_State);
		recordAction(13, Gamepad.LEFT_Trigger_State);
	
		recording += "\n";
	}
	
	public void stopRecording() {
		if (isRecording) {
			sendActions();
		}
		isRecording = false;
		this.stop();
	}
	
	private double toDouble(boolean bool) {
		if (bool) {
			return 1.0;
		}
		else return 0.0;
	}
	
	public void run() {
		try {
			while(true) {
				if(isRecording) {
					record();
					Thread.sleep(timeStep);
				}
			}
		}
		catch(Exception e) {
			DriverStation.reportError(e.getStackTrace() + "", false);
		}
	}
}
