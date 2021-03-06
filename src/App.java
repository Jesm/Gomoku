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
	public static int victorySequenceLength=5;
	public static Color lineColor;
	public static Color rockColor;
	public static Color hostRockColor;
	public static Color guestRockColor;
	public static Color boardBgColor;
	
	public static void main(String args[]){
		
		System.setProperty("java.library.path", "%USERPROFILE%\\Desktop\\");
		int base=(App.boardOrder+1)*(App.rockDiameter+App.rockMargin*2)+App.boardOrder;
		App.dimension=new Dimension(base, base);

		App.lineColor=new Color(144, 144, 144);
		App.rockColor=new Color(144, 144, 144, 240);
		App.hostRockColor=Color.WHITE;
		App.guestRockColor=Color.BLACK;
		App.boardBgColor=new Color(208, 208, 208);
		
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
		
//		C�digo para fechar porta antes de fechar aplica��o
		final App appReference=this;
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	appReference.closePort();
		    }
		});
		
		this.setLayout(null);
		
		Dimension screenSize=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		Dimension base=new Dimension(App.dimension.width+App.padding*2, App.dimension.height+App.padding*2+App.messageBoxHeight+20);
		this.setBounds((screenSize.width-base.width)/2, (screenSize.height-base.height)/2, base.width, base.height);
		
		this.messageBox=new JLabel();
		this.messageBox.setBounds(App.padding, App.padding, App.dimension.width, App.messageBoxHeight);
		this.getContentPane().add(this.messageBox);		

		this.getContentPane().setBackground(App.boardBgColor);
	}
	
	private void displayMessage(String str){
		this.messageBox.setText(str);
	}
	

	private void createGameMenu(){
		
		this.menu=new JPanel();
		this.menu.setLocation(App.padding, App.padding+App.messageBoxHeight);
		this.menu.setSize(App.dimension);
		this.menu.setOpaque(false);

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
		this.startGame.setOpaque(false);
		
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
	
	private void sendComCommand(String str, byte arr[]) throws IOException, InterruptedException{
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
				this.getOpponentMove((int)cmd.args[0], (int)cmd.args[1]);
			break;
		}
	}
	
	public void setInvite(){
		this.status=AppStatus.INVITED;
		this.displayMessage("Voc� foi convidado para uma partida! Clique no botao abaixo para jogar");
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
		this.board.setOpaque(false);
		this.getContentPane().add(this.board);
		
		this.startGame.setVisible(false);
	}
	
	
	public void getPlayerMove(Integer x, Integer y) throws IOException, InterruptedException{
		byte[] arr={ComCommand.reduceToByte(x), ComCommand.reduceToByte(y)};
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
		this.status=AppStatus.READY;
		this.displayMessage(b?"Voce venceu, parabens!":"Voce perdeu!");
		this.messageBox.setForeground(b?Color.GREEN:Color.RED);
		this.closePort();
		JOptionPane.showMessageDialog(menu, b?"Voce venceu, parabens!":"Voce perdeu!");
		System.exit(0);
	}
	
	private void closePort(){
		if(this.comPort!=null)
			this.comPort.close();
	}
	
}
