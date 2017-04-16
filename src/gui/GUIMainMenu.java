package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import run.NRun;
import run.QRun;

public class GUIMainMenu extends JFrame{

	private static final long serialVersionUID = 6386363257594856967L;
	
	private JPanel buttonPanel;
	private JButton normal;
	private JButton auto;
	private JButton rules;
	private JButton exitButton;
	
	private static int mode;
	private static boolean choose;
	private static boolean exit;

	public GUIMainMenu(){
		this.initComponent();
		this.addListener();
		this.getContentPane().add(buttonPanel);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(800, 600));
		this.setTitle("MainMenu");
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		exit = false;
	}
	
	public void initComponent(){
		buttonPanel = new JPanel()
		{
			protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
 
                ImageIcon m = new ImageIcon(new File(System.getProperty("user.dir") + "/res/img/ville2.png").getPath());
                Image monImage = m.getImage();
                g.drawImage(monImage, 0, 0,this);
 
            }
		};
		
		buttonPanel.setLayout(null);
		normal = new JButton("Normal mode");
		auto = new JButton("Auto mode");
		rules = new JButton("Rules");
		exitButton = new JButton("Exit");
		buttonPanel.add(normal);
		buttonPanel.add(auto);
		buttonPanel.add(rules);
		buttonPanel.add(exitButton);
		normal.setBounds(310, 300, 180, 30);
		auto.setBounds(310, 350, 180, 30);
		rules.setBounds(310, 400, 180, 30);
		exitButton.setBounds(310, 450, 180, 30);
		
		JLabel title = new JLabel("Urban Life Simulator");
		Font font = new Font("Arial",Font.BOLD,42);
		title.setFont(font);
		buttonPanel.add(title);
		title.setBounds(150, 150, 490, 100);
	}
	
	public void addListener(){
		normal.addActionListener(new NormalModeListener());
		auto.addActionListener(new AutoModeListener());
		exitButton.addActionListener(new ExitListener());
	}
	
	class NormalModeListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			/*NRun run = new NRun();
			run.initialisation();
			run.run();*/
			mode = 1;
			choose = true;
		}
	}
	
	class AutoModeListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			/*QRun run = new QRun();
			run.initialisation();
			run.run();*/
			mode = 2;
			choose = true;
		}
	}
	
	class ExitListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			exit = true;
		}
	}
	
	public static void main(String[] args){
		GUIMainMenu menu = new GUIMainMenu();
		while (!exit){
			System.out.print(" "); //Il faut trouver une autre instruction (orienté poubelle)
			switch(mode){
				case 1:
					menu.setVisible(false);
					NRun nrun = new NRun();
					nrun.initialisation();
					nrun.run();
					menu.setVisible(true);
					choose = false;
					mode = 0;
					break;
				case 2:
					menu.setVisible(false);
					QRun qrun = new QRun();
					qrun.initialisation();
					qrun.run();
					menu.setVisible(true);
					choose = false;
					mode = 0;
					break;
			}
		}
		System.exit(0);
	}
	
}