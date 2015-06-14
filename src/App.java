import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.TooManyListenersException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
	public static int rockDiameter=16;
	public static Color lineColor;
	public static Color rockColor;
	public static Color hostRockColor;
	public static Color guestRockColor;
	
	public static void main(String args[]){
		
		System.setProperty("java.library.path", "%USERPROFILE%\\Desktop\\");
		int base=(App.boardOrder+1)*(App.rockDiameter+App.rockMargin*2)+App.boardOrder;
		App.dimension=new Dimension(base, base);

		App.lineColor=new Color(144, 144, 144);
		App.rockColor=new Color(144, 144, 144, 240);
		App.hostRockColor=Color.WHITE;
		App.guestRockColor=Color.BLACK;
		
		new App();
//		new App();
	}
	

	private AppStatus status;
	private Com comPort;
	private AppBoard board;
	public Color color, opColor;
	
	private JLabel messageBox;
	private JPanel menu;
	private JComboBox<String> comList;
	private String selectedCom;
	private JPanel startGame;
	private String cor;
	
	public App(){
		this.status=AppStatus.INITIALIZED;

		this.createGameEnvironment();
		this.displayMessage("Insira a porta COM que deseja utilizar");
		this.createGameMenu();
		
		this.setVisible(true);
	}
	
	public AppStatus getStatus(){
		return this.status;
	}
	
	private void createGameEnvironment(){
		this.setTitle("Gomoku");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(null);
		
		Dimension screenSize=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		Dimension base=new Dimension(App.dimension.width+App.padding*2, App.dimension.height+App.padding*2+App.messageBoxHeight+20);
		this.setBounds((screenSize.width-base.width)/2, (screenSize.height-base.height)/2, base.width, base.height);
		
		this.messageBox=new JLabel();
		this.messageBox.setBounds(App.padding, App.padding, App.dimension.width, App.messageBoxHeight);
		this.getContentPane().add(this.messageBox);
	}
	
	private void displayMessage(String str){
		this.messageBox.setText(str);
	}
	

	private void createGameMenu(){
		
		this.menu=new JPanel();
		this.menu.setLocation(App.padding, App.padding+App.messageBoxHeight);
		this.menu.setSize(App.dimension);

		String[] ports = Com.ListarPortas();
		comList = new JComboBox<String>(ports);
		this.menu.add(comList);

		JButton button=new JButton("Usar porta COM");
		button.addActionListener(this);
		this.menu.add(button);
				
		// Pega a com selecionada e joga na variavel selectedcom
		button.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
                selectedCom = (String)(comList.getSelectedItem());
            }
        });     
		
		this.getContentPane().add(this.menu);
		
		this.startGame=new JPanel();
		this.startGame.setLocation(App.padding, App.padding+App.messageBoxHeight);
		this.startGame.setSize(App.dimension);
		
		button=new JButton("Iniciar jogo");
		button.addActionListener(this);
		this.startGame.add(button);
		
		this.startGame.setVisible(false);
		this.getContentPane().add(this.startGame);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(this.status){
			case INITIALIZED:
				try {
					this.preparePort();
				} catch (NoSuchPortException | PortInUseException
						| UnsupportedCommOperationException | TooManyListenersException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			break;
			case READY:
				try {
					this.searchGame();
				} catch (IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			break;
			case INVITED:				
				try {
					this.acceptInvite();
				} catch (IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			break;
			default:
			break;
		}
	}
	
	private void preparePort() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, TooManyListenersException{
		this.comPort=new Com(selectedCom, this);
		this.status=AppStatus.READY;
		
		this.menu.setVisible(false);
		this.startGame.setVisible(true);
		this.displayMessage("Procure um jogo clicando no botao abaixo ou aguarde um convite");
		
	}
	
	private void sendComCommand(String str) throws IOException, InterruptedException{
		ComCommand cmd=new ComCommand(str);
		this.comPort.sendCommand(cmd);
	}
	
	private void sendComCommand(String str, String arr[]) throws IOException, InterruptedException{
		ComCommand cmd=new ComCommand(str, arr);
		this.comPort.sendCommand(cmd);
	}
	
	private void searchGame() throws IOException, InterruptedException{
		this.sendComCommand("invite");
		this.displayMessage("Foi enviado um pedido de jogo");
	}
	
	public void receiveCommand(ComCommand cmd){
		switch(cmd.getName()){
			case "invite":
				this.setInvite();
			break;
			case "accepted_invite":
				this.getAcceptedInvite();
			break;
			case "move":
				this.getOpponentMove(Integer.parseInt(cmd.args[0]), Integer.parseInt(cmd.args[1]));
			break;
		}
	}
	
	public void setInvite(){
		this.status=AppStatus.INVITED;
		this.displayMessage("Você foi convidado para uma partida! Clique no botao abaixo para jogar");
	}
	
	public void acceptInvite() throws IOException, InterruptedException{
		this.sendComCommand("accepted_invite");
		this.status=AppStatus.PLAYING;
		this.color=App.guestRockColor;
		this.opColor=App.hostRockColor;
		this.generateGameBoard();
		this.displayMessage("O jogo comecou! Faca seu primeiro movimento clicando em uma das posicoes abaixo");
	}
	
	public void getAcceptedInvite(){
		this.status=AppStatus.WAITING;
		this.color=App.hostRockColor;
		this.opColor=App.guestRockColor;
		this.generateGameBoard();
		this.displayMessage("Foi encontrado um jogo! Aguarde pelo movimento de seu oponente");
	}

	private void generateGameBoard(){

		this.board=new AppBoard(this);
		this.board.setLocation(App.padding, App.padding+App.messageBoxHeight);
		this.board.setSize(App.dimension);
		this.getContentPane().add(this.board);
		
		this.startGame.setVisible(false);
	}
	
	
	public void getPlayerMove(Integer x, Integer y) throws IOException, InterruptedException{
		String[] arr={x.toString(), y.toString()};
		this.sendComCommand("move", arr);
		
		if(this.board.setPlayerRock(x, y, 1)){
			this.endGame(true);
			return;
		}
		
		this.status=AppStatus.WAITING;
		this.displayMessage("Aguardando jogada do oponente...");
	}
	
	public void getOpponentMove(int x, int y){
		if(this.board.setPlayerRock(x, y, -1)){
			this.endGame(false);
			return;
		}
		
		this.status=AppStatus.PLAYING;
		this.displayMessage("Faca sua jogada");
	}
	
	private void endGame(boolean b){
		this.status=AppStatus.PLAYING;
		this.displayMessage(b?"Voce venceu, parabens!":"Voce perdeu!");
		this.messageBox.setForeground(b?Color.GREEN:Color.RED);
		JOptionPane.showMessageDialog(menu,b?"Voce venceu, parabens!":"Voce perdeu!");
		this.closePort();
	}
	
	private void closePort(){
		if(this.comPort!=null)
			this.comPort.close();
	}
	
}
