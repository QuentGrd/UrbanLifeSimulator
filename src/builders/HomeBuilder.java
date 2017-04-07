package builders;

import building.Building;
import building.Home;

/**
 * 
 * @author quentin
 *
 */

public class HomeBuilder {
	
private Home home;
	
	public HomeBuilder(){
	}
	
	public void creatWork(){
		home = new Home();	
		home.setReward(new double[3]);
		home.setReward(10, 0);
		home.setReward(0, 1);
		home.setReward(30, 0);
		home.setMaxUser(Building.density);
	}

	public Home getHome() {
		return home;
	}

	public void setHome(Home home) {
		this.home = home;
	}

}
