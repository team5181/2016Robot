package org.usfirst.frc.team5181.autonomousThreads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.usfirst.frc.team5181.autonomousThreads.PIDFunctions.Controllers;
import org.usfirst.frc.team5181.robot.DriveTrain;
import org.usfirst.frc.team5181.robot.Gamepad;
import org.usfirst.frc.team5181.robot.Robot;
import org.usfirst.frc.team5181.sensors.LimitSwitch;
import org.usfirst.frc.team5181.sensors.RevX;

import edu.wpi.first.wpilibj.DriverStation;

public class TimedAutonomous implements Autonomous {
	private Robot robot;
	PIDFunctions pidi;
	LimitSwitch autonSwitch;
	
	private boolean inAuton;
	
	//for actionPlayback only
	ArrayList<String> commands;
	ArrayList<Double> timePeriods;
	double commandStartTime = 0;
	RevX revX;
	
	public TimedAutonomous(Robot r) {
		robot = r;
		
		revX = r.getRevX();
		//autonSwitch = r.limitSwitch;
		pidi = new PIDFunctions(r, Controllers.ROTATION, revX);
		inAuton = false;
	}
	
	public void doAuton() {
		try {
			if(commands != null && timePeriods != null) {
				int iterative = 0;
				for (String command:commands) {
					if(command.contains("SETPOINT")) {
						double rotation = Double.parseDouble(command.substring(command.indexOf("R:") + 2));
						
						//Correct E-r
						while(pidi.onTarget(Controllers.ROTATION)) {
							pidi.turnToAngle(rotation);
						}
						continue;
					}
					
					commandStartTime = System.nanoTime() / 1000000;
					while((System.nanoTime() / 1000000) - commandStartTime < timePeriods.get(iterative)) {
						robot.teleopMaster(true);
						Gamepad.setSyntheticState(command);
						
						//Breaks out of thread is autonomous ends
						if(!inAuton) {
							break;
						}
					}
					//Breaks out of thread if autonomous ends
					if(!inAuton) {
						break;
					}
					iterative++;
				}
				DriverStation.reportError("Finished", false);
			}
		}
		catch(Exception e) {
			DriverStation.reportError(e + "", true);
		}
	}
	
	/**
	 * 
	 */
	public void initializeAuton(String recordingName, String[] others) {
		commands = new ArrayList<String>();
		timePeriods = new ArrayList<Double>();
		try {
			/**
			 *  @params True : False
			 *  @params RW   : RT
			 */
			
			//BufferedReader br = new BufferedReader(new FileReader(new File((autonSwitch.get()) ? "/var/rcrdng/rockWall.rcrdng" : "/var/rcrdng/roughTerrain.rcrdng")));

			BufferedReader br = new BufferedReader(new FileReader(new File(recordingName)));
			String line = "";
			while((line = br.readLine()) != null) {
				if (line.equals("")) {
					continue;
				}
				if(line.contains("t:")) {
					timePeriods.add(Double.parseDouble(line.substring(line.indexOf("t:") + 2, line.lastIndexOf(";"))));
					continue;
				}
				commands.add(line);
			}
			br.close();
			
			do {
				revX.zeroYaw();
			} while(Math.abs(revX.getRotation()) < 0.5);
		}
		catch(Exception e) {
			DriverStation.reportError(e + "Autonomous.java, actionPlayback:\n", false);
		}
	}
	
	public void setAutonState(boolean inAuton) {
		this.inAuton = inAuton;
	}
	
}
