package clock;

/**
 * 
 * @author matthieu
 *
 */
public class Schedule {

	private int hour;
	private int minute;
	
	public Schedule(int hour, int minute){
		if(hour>=0 && hour<24)
			this.hour = hour;
		else
			this.hour = 0;
		//ajouter une exception ?
		
		if(minute>=0 && minute<60)
			this.minute = minute;
		else
			this.minute = 0;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	@Override
	public String toString() {
		return "Schedule [hour=" + hour + ", minute=" + minute + "]";
	}
}
