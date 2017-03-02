package gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;

import city.Population;

/**
 * 
 * @author Quentin - matthieu
 *
 */
public class GUIInfoPart extends JPanel{

	private static final long serialVersionUID = -560584172594140952L;

	private Population pop;
	
	private JPanel buttonPart;
	private JButton list;
	private JButton info;
	
	private CardLayout cl;
	private JPanel cardsContainer;
	
	private JPanel infoPart;
	private JTextPane infoText;
	private String textDefault = "First Name:\nName:\nID:\nEmotion:\nHome:\nWork:\n";
	
	private JPanel characterListPanel;
	private static final String INFOPANEL = "Information Panel";
	private static final String CHARACTERPANEL = "Character list Panel";
	private JScrollPane listScroller;
	
	private JList<String> characterList;
	
	public GUIInfoPart(Population pop){
		this.pop = pop;
		this.initButtonPart();
		this.initCardLayout();
		this.setPreferredSize(new Dimension(400, 600));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(cardsContainer);
	}
	
	public void initCardLayout(){
		cl = new CardLayout();
		cardsContainer = new JPanel();
		this.initInfoPanel();
		this.initCharacterListPanel();
		cardsContainer.setLayout(cl);
		cardsContainer.add(characterListPanel, CHARACTERPANEL);
		cardsContainer.add(infoPart, INFOPANEL);
		cl.show(cardsContainer, CHARACTERPANEL);
	}
	
	public void initButtonPart(){
		buttonPart = new JPanel();
		info = new JButton("_info");
		list = new JButton("Back");
		info.addActionListener(new DisplayInfoAction());
		list.addActionListener(new DisplayListAction());
		buttonPart.setLayout(new FlowLayout());
		buttonPart.add(list);
		//buttonPart.add(info);
	}
	
	public void initInfoPanel(){
		infoPart = new JPanel();
		infoText = new JTextPane();
		infoText.setEditable(false);
		infoText.setText(textDefault);
		infoText.setPreferredSize(new Dimension(390, 550));
		infoPart.add(infoText);
		infoPart.add(buttonPart);
	}
	
	public void initCharacterListPanel(){
		characterListPanel = new JPanel();
		DefaultListModel<String> model = new DefaultListModel<String>();
		String[] list = pop.transform();
		for (int i = 0; i < list.length; i++) {
			model.addElement(list[i]);
		}
		characterList = new JList<String>(model);
		characterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		characterList.setLayoutOrientation(JList.VERTICAL);
		characterList.setVisibleRowCount(-1);
		characterList.addMouseListener(new MouseListListener());
		listScroller = new JScrollPane(characterList);
		listScroller.setPreferredSize(new Dimension(390, 590));
		characterListPanel.add(listScroller);
	}
	
	public String getCharacInfo(character.Character c){
		String info = null;
		if (c == null)
			info = "This character is dead !";
		else{
			info = "First Name: \t" + c.getFirstName();
			info += "\nName: \t" + c.getName();
			info += "\nID: \t" + c.getId();
			info += "\nEmotion: \t" + c.getEmotion().getCounter();
			info += "\nHome: \t" + c.getHome().getAddress().toString();
			info += "\nWork: \t" + c.getWork().getAddress().toString();
		}
		return info;
	}
	
	class MouseListListener implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2){
				int i = characterList.getSelectedIndex();
				character.Character charac = pop.getListCharacter().get(i);
				infoText.setText(getCharacInfo(charac));
				cl.show(cardsContainer, INFOPANEL);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
		
	}
	
	class DisplayInfoAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			cl.show(cardsContainer, INFOPANEL);
		}
		
	}
	
	class DisplayListAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			cl.show(cardsContainer, CHARACTERPANEL);
		}
		
	}
	
}
