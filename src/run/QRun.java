package run;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import autoMode.QActions;
import autoMode.State;
import builders.CityBuilder;
import building.Home;
import character.Character;
import character.QCharacter;
import city.City;
import city.Infrastructure;
import clock.Clock;
import gui.GUIMain;

/**
 * 
 * @author matthieu
 *
 */
public class QRun {
	
	private City city;
	private GUIMain gui;
	private static Clock clock;
	
	private boolean run;
	private static boolean play;
	
	private double learnFactor;
	private double discountedFactor;
	private int exploration;
	
	
	public QRun(){
		run = true;
		play = true;
		
		learnFactor = 0.5;
		discountedFactor = 0.5;
		exploration = 10;
	}
	
	public void initialisation(){
		city = new City();
		CityBuilder cBuilder = new CityBuilder(city, true);
		clock = new Clock(0, 0, 1, 1, 2017);
		
		gui = new GUIMain(city.getMap(), clock, city.getPopulation(), 1);
	}
	
	public void run(){
		
		while(run){
			if (play){
				
				for (int i = 0; i < city.getPopulation().getNbOfCharacter(); i++) {
					QActions actionChosen;
					QCharacter car = (QCharacter)city.getPopulation().getListCharacter().get(i);
					//System.out.println(car.getCurrentState().getCoord() + " - " + car.getPosition());
					if(car.getAlive() == true){
						if(clock.getMin().getCounter()%exploration == 0){
							actionChosen = randomActionChoise(car.getCurrentState().getListAction());
						}
						else{
							actionChosen = QLDecision(car);
							System.out.println("[" + car.getNbOfDeath() +"] " + actionChosen.getValue());
						}
						
						moveAgent(actionChosen, car);
					}
				}
				System.out.println("----------");
				
				lifeManagment();
				gui.refreshGUI(city.getPopulation(), clock);
				clock.increment();
			}
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
		JOptionPane jop1 = new JOptionPane();

		jop1.showMessageDialog(null, "Vous avez perdu !\nMerci d'avoir joué", "Fin du jeu", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * this methode manage the life of character
	 */
	public void lifeManagment(){
		ArrayList<Character> carList = city.getPopulation().getListCharacter();
		int carListSize = city.getPopulation().getNbOfCharacter();
		
		for (int i = 0; i < carListSize; i++) {
			QCharacter car = (QCharacter) carList.get(i);
			 
			if(car.getAlive() == false){
				car.getLife(0).setCounter(75);
				car.setAlive(true);
				
				Home newHome;
				//tirage aleatoire de sa nouvelle maison
				Random rand = new Random();
				int choise = rand.nextInt(city.getMap().getHomeList().size());
				
				newHome = city.getMap().getHomeList().get(choise);
				car.setHome(newHome);
				car.setInitialPosition(newHome.getAddress());
				car.setPosition(newHome.getAddress());
				car.setCurrentState(car.getEnvironment().getState(car.getPosition().getX(), car.getPosition().getY()));
			}
			
			if(car.getAlive() == true){
				if(car.getLife(0).getCounter() == 0){
					car.setAlive(false);
					car.setNbOfDeath(car.getNbOfDeath()+1);
				}
			}
			
		}
	}
	
	
	/**
	 * this methode say the right action to do
	 * @return action to do
	 */
	public QActions QLDecision(QCharacter agentQL){
		QActions action;
		/*liste des actions possibles*/
		ArrayList<QActions> choiseList = agentQL.getCurrentState().getListAction();
		
		/*creation et remplissage d'une liste contenant le/les actions maximale(s)*/
		ArrayList<QActions> listMax = new ArrayList<QActions>();
		double max = choiseList.get(0).getValue();
		for(int i=0; i<choiseList.size(); i++){
			if(choiseList.get(i).getValue() > max){
				listMax.clear();
				listMax.add(choiseList.get(i));
				max = choiseList.get(i).getValue();
			}
			else if(choiseList.get(i).getValue() == max){
				listMax.add(choiseList.get(i));
			}
		}
		
		if(listMax.size()>1)
			action = randomActionChoise(listMax);
		else
			action = listMax.get(0);
		
		return action;
	}
	
	/**
	 * this methode choose a random action in a list of actions
	 * @param list of actions
	 * @return random action
	 */
	public QActions randomActionChoise(ArrayList<QActions> list){
		Random rand = new Random();
		int choise;
		
		choise = rand.nextInt(list.size());
		
		return list.get(choise);
	}
	
	/**
	 * this methode change the state of the agent using a action
	 * @param nextState
	 */
	public void moveAgent(QActions action, QCharacter agentQL){
		agentQL.setCurrentState(action.getNextState());
		agentQL.setPosition(agentQL.getCurrentState().getCoord());
		setActionQValue(action);
		agentQL.getLife(0).decrement();
		
		if(agentQL.getCurrentState().getReward() != 0){
			Infrastructure infra = city.getMap().getInfrastructure(agentQL.getPosition().getX(), agentQL.getPosition().getY());
			if(infra.getType() == 3){
				agentQL.getLife(0).increment((int)agentQL.getCurrentState().getReward());
			}
			else if(infra.getType() == 2){
				agentQL.getLife(0).decrement(Math.abs((int)agentQL.getCurrentState().getReward()));
			}
		}
	}
	
	/**
	 * this methode set the value a action using the Q-learning equation
	 * @param action
	 */
	public void setActionQValue(QActions action){
		double newValue;
		
		double maxFutureQValue = maxQValue(action.getNextState());
		
		//Q-Learning actualization
		newValue = action.getValue() + learnFactor * (action.getNextState().getReward() + discountedFactor * maxFutureQValue - action.getValue());
		
		action.setValue(newValue);
	}
	
	/**
	 * this methode found the the action with the biggest QValue of a state and return
	 * the biggest QValue
	 * @param state
	 * @return biggest QValue
	 */
	public double maxQValue(State state){
		double max = state.getListAction().get(0).getValue();
		
		for(int i=0; i<state.getListAction().size(); i++){
			if(state.getListAction().get(i).getValue() > max){
				max = state.getListAction().get(i).getValue();
			}
		}
		
		return max;
	}
	
	public static void switchPlayStatus(){
		if (play)
			play = false;
		else
			play = true;
	}

	public static boolean isPlay() {
		return play;
	}
	
}

