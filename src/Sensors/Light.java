package Actuators;

import java.awt.Color;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

public class Light{
	Relay redRelay;
	Relay greenRelay;
	Relay blueRelay;
	//final Color GOLD = new Color(255, 215, 0);
	//final Color RED = new Color(255, 0, 0);
	//final Color BLUE = new Color(0, 0, 255);
	enum Color {
		RED,
		GREEN,
		BLUE,
		OFF, //all off
		ON   //all on
	}
	
	public Light(){
		redRelay = new Relay(1, Relay.Direction.kForward);
		greenRelay = new Relay(3,Relay.Direction.kForward);
		blueRelay = new Relay(2, Relay.Direction.kForward);
	}
	
	public void setColor(Color c) {
		switch (c) {
			case RED:
				redRelay.set(Value.kOn);
				greenRelay.set(Value.kOff);
				blueRelay.set(Value.kOff);
				break;
			case BLUE:
				redRelay.set(Value.kOff);
				greenRelay.set(Value.kOff);
				blueRelay.set(Value.kOn);
				break;
			case GREEN:
				redRelay.set(Value.kOff);
				greenRelay.set(Value.kOn);
				blueRelay.set(Value.kOff);
				break;
			case ON:
				redRelay.set(Value.kOn);
				greenRelay.set(Value.kOn);
				blueRelay.set(Value.kOn);
				break;
			case OFF:
				redRelay.set(Value.kOff);
				greenRelay.set(Value.kOff);
				blueRelay.set(Value.kOff);
				break;
			default:
				break;
		}
		//redRelay.set( 1 - (c.getRed()/255));
		//greenRelay.set(1 - (c.getGreen()/255));
		//blueRelay.set(1 - (c.getBlue()/255));
	}
	
	public void periodic(){
		while (true){
			if (DriverStation.getInstance().getAlliance().equals(Alliance.Blue)) {
				this.setColor(Color.BLUE);
			}
			else if (DriverStation.getInstance().getAlliance().equals(Alliance.Red)) {
				this.setColor(Color.RED);
			}
			else {
				this.setColor(Color.ON);
			}
		}
	}
}