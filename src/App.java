import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class App extends JFrame implements ActionListener {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Dimension dimension;
	public static int padding=20;
	public static int messageBoxHeight=40;
	
	public static int boardOrder=19;
	public static int rockMargin=4;
	public static int rockWidth=16;
	public static Color hostRockColor;
	public static Color guestRockColor;
	
	public static void main(String args[]){
		int base=(App.boardOrder+1)*(App.rockWidth+App.rockMargin*2)+App.boardOrder;
		App.dimension=new Dimension(base, base+App.messageBoxHeight);
		
		App.hostRockColor=new Color(255, 0, 0);
		App.guestRockColor=new Color(0, 0, 255);
		
		new App();
	}
	

	private AppStatus status;
	private Hub hub;
	
	private JPanel menu;
	private JTextField inputCOMPort;
	private JPanel board;
	
	public App(){
		this.status=AppStatus.STARTED;
		this.hub=Hub.getInstance();
		
		
		
		this.createGameEnvironment();
		this.createGameMenu();
		
		this.setVisible(true);
	}
	
	private void createGameEnvironment(){
		this.setTitle("Gomoku");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(null);
		
		Dimension screenSize=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		Dimension base=new Dimension(App.dimension.width+App.padding*2, App.dimension.height+App.padding*2);
		this.setBounds((screenSize.width-base.width)/2, (screenSize.height-base.height)/2, base.width, base.height);
	}
	
	private void createGameMenu(){
		this.menu=new JPanel();
//		this.menu.setLayout(null);
		this.menu.setLocation(App.padding, App.padding);
		this.menu.setSize(App.dimension);
		
		JLabel label=new JLabel("Insira a porta COM que deseja utilizar:");
		this.menu.add(label);

		this.inputCOMPort=new JTextField();
		this.inputCOMPort.setColumns(10);
		this.menu.add(this.inputCOMPort);
		
		JButton button=new JButton("Usar porta COM");
		button.addActionListener(this);
		this.menu.add(button);
		
		this.getContentPane().add(this.menu);
	}

	private void createGameBoard(){
		this.board=new AppBoard();
		this.board.setLocation(App.padding, App.padding);
		this.board.setSize(App.dimension);
		this.getContentPane().add(this.board);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.menu.setVisible(false);
		System.out.println(this.inputCOMPort.getText());
		this.createGameBoard();
	}
}
