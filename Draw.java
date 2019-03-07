import javax.swing.JComponent;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.sound.sampled.*;

public class Draw extends JComponent implements ActionListener{

	//Start screen variables
	public boolean beginGame = false;
	public Rectangle buttonOne;
	public int button1_x = 170;
	public int button1_y = 150;
	public int button1_width = 200;
	public int button1_height = 50;

	public MouseMotion mouse;
	public Rectangle mouseBounds;

	public Rectangle buttonTwo;
	public int button2_x = 170;
	public int button2_y = 250;
	public int button2_width = 200;
	public int button2_height = 50;

	//World setup
	private final int gravity = 370;
	private final int x = 50;
	private BufferedImage backgroundImage;
	private URL backgroundFile = getClass().getResource("backgroundImages/azureBackground.png");

	//Player Position
	public Player player;
	public int playerX = 20;
	public int playerY = 60;

	//Player Priojectiles
	public ManaAmount mana[];
	public ArrayList<ManaAmount>manaList = new ArrayList<>();
	public int manaCapacity = 1;
	public MagicMissle magicMissle;
	public int manaX = 55;
	public int manaY = 48;
	public int bManaX = 65;
	public int bManaY = 57;


	//Creature Spawner
	public Creature creature;
	public ArrayList<Creature> creatureList = new ArrayList<>();
	public int counter = 1;
	public Timer timer = new Timer(20000, this);

	//Collison Detection
	public boolean magicHits = false;

	public Draw(){
		timer.start();
		spawnPlayer();
		spawnCreature();


		mouse = new MouseMotion();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		

		try{
			backgroundImage = ImageIO.read(backgroundFile);
		}catch(IOException e){
			e.printStackTrace();
		}
		startGame();
	}

	public void spawnPlayer(){
		player = new Player(x, gravity, this);
		mana = new ManaAmount[10];

		for(int i = 0; i < 10; i++){
			mana[i] = new ManaAmount(manaX, manaY, this);
			manaList.add(mana[i]);
			manaX+=20;
		}
		

	}

	public void spawnCreature(){
		int spawnPostion = 510;
		if(creatureList.size() != 10){
			creature = new Creature(spawnPostion, gravity + 10, this);
			creatureList.add(creature);
			counter++;
		}	
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);


		

			//Background
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 500);
			g.drawImage(backgroundImage, 0, 0, this);

			//Adding Player
			g.drawImage(player.playerImage, player.xPos, player.yPos, this);

			//Load player HealthBar
			loadPlayerStats(g);

			//Player Magic Attack
			for(int i = 0; i <player.missleList.size(); i++ ){
				if(player.isUsingMagic == true){
					if(player.missleList.size() != 0){	
						g.drawImage(player.missleList.get(i).magicImage,player.missleList.get(i).magicX, player.missleList.get(i).magicY, this);	
					}
				}
			}

			//Adding Creatures
			for(int i = 0; i < creatureList.size(); i++){
				if(creatureList.size() != 0){
					g.drawImage(creatureList.get(i).creatureImage, creatureList.get(i).xPos, creatureList.get(i).yPos, this);
					g.setColor(Color.RED);
					g.fillRect(creatureList.get(i).xPos, creatureList.get(i).yPos, 35, 2);
					g.setColor(Color.GREEN);
					g.fillRect(creatureList.get(i).xPos, creatureList.get(i).yPos, creatureList.get(i).health, 2);
				}
			
		}
	}

	public void loadPlayerStats(Graphics g){

		//Player Health Frame
		String health = String.valueOf(player.healthBar);
		g.setColor(Color.BLACK);
		g.fillRect(6, 18, 264, 34);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(8, 20, 260, 30);

		//Player Mana Frame
		g.setColor(Color.MAGENTA);
		g.fillRect(6, 53, 264, 24);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(8, 55, 260, 20);

		//Player Health Bar
		g.setColor(Color.RED);
		g.fillRect(50, 25, 200, 10);
		g.setColor(Color.GREEN);
		g.fillRect(50, 25, player.healthBar, 10);

		//Strings in Health Frame
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.drawString("Health:", 10, 34);
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.drawString(health + "/200", 60, 47);

		//String for Mana Count
		g.setColor(Color.MAGENTA);
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.drawString("Mana:", 10, 66);

		//Player Mana Icons
		for(int i = 0; i < manaList.size(); i++){
			g.drawImage(manaList.get(i).manaImage, manaList.get(i).xPos, manaList.get(i).yPos, this);
		}

	}

	public void startGame(){

		Thread gameThread = new Thread(new Runnable(){
			public void run(){
				try{	
					while(true){
						
						for(int i = 0; i < creatureList.size(); i++){

							if(creatureList.get(i).checkHealth() == true){
								creatureList.get(i).creatureDeath();
								collisionDetection();
								eraseImages();
							} 
							if(creatureList.get(i).isAttacking != true){
								creatureList.get(i).movementCreature();
								collisionDetection();

							}else {
								creatureList.get(i).attackCreature();
								System.out.println("monster attack");
								collisionDetection();
								damageDetection();

							}
						AudioInputStream ai = AudioSystem.getAudioInputStream(new File("backgroundMusic.WAV"));
						Clip test = AudioSystem.getClip(); 
						test.open(ai);
						test.start();

						while (!test.isRunning())
                		Thread.sleep(10);
            			while (test.isRunning())
                		Thread.sleep(10);

            			test.close();
						}
						Thread.sleep(350);
					}
				}catch(ArrayIndexOutOfBoundsException e){
					e.printStackTrace();
					spawnCreature();
					
				}catch(InterruptedException e){
					e.printStackTrace();
				}catch(UnsupportedAudioFileException e){
					e.printStackTrace();
				}catch(LineUnavailableException e){
					e.printStackTrace();

				}catch(IOException e){
					e.printStackTrace();
				}
			}
		});
		gameThread.start();
	}

	public void actionPerformed(ActionEvent arg0) {
		spawnCreature();	
	}

	public void collisionDetection(){
		
		for( int i = 0; i < creatureList.size(); i++){
			Rectangle playerBounds = player.playerBounds();
			Rectangle creatureBounds = creatureList.get(i).creatureBounds();
			
			//Detects if creature intersects player
			if(creatureList.size() != 0){
				if(playerBounds.intersects(creatureBounds)){
					creatureList.get(i).isAttacking = true;
					System.out.println("INTERSECT MONSTERS");
				
				}else{
					creatureList.get(i).isAttacking = false;
					creatureList.get(i).isMoving = true;
				}
				//Detects if player hit Monster
				if(player.usingSword == true){
					if(playerBounds.intersects(creatureBounds)){
						System.out.println("Player attacks");
						creatureList.get(i).health-=player.power;
					}
				}
				for(MagicMissle playerMagic: player.missleList){
					//Check if monster made contact with player
					if(playerMagic.magicBounds().intersects(creatureBounds)){
						playerMagic.magicHit();
						damageDetection();
						eraseImages();
						break;
					}
				}
			}
		}
	}

	public void damageDetection(){

		for(int i = 0; i < creatureList.size(); i ++){
			for(int j = 0; j < player.missleList.size(); j++){
				System.out.println("Assessing");
				
				//Damage delt to creature calculated here
				if(player.missleList.get(j).missleImpact == true){
					System.out.println("Assessing Magic Damage");
					creatureList.get(j).health-=player.missleList.get(j).magicDmg;
				}	
			}
		}

		//Calculates Damage monster deals on Player
		for(int i = 0; i < creatureList.size(); i ++){
			if(creatureList.get(i).isAttacking == true){
				player.healthBar = player.healthBar - (creatureList.get(i).power / player.defense);
				System.out.println("Assessing Monster Damage");
			}
		}
	}

	public void eraseImages(){
		
		//When monster dies Removes it from game
		for(int i = 0; i < creatureList.size(); i++){
			if(creatureList.get(i).health <= 0 ){
				creatureList.remove(i);
				System.out.println("creature deletion");
			}
		}
		//When magic hits  or pass the frame remove it from the game
		for(int i = 0; i < player.missleList.size(); i++){
			if(player.missleList.get(i).missleImpact == true ||player.missleList.get(i).magicX >= 550){
				System.out.println("magic deletion");
				player.missleList.remove(i);			
			}
		}
		//When magic is used remove mana image from screen
		for(int i = 0; i < manaList.size(); i++){
			if(player.isUsingMagic == true){
				if(manaList.contains(manaList.get(i))){
					manaList.remove(manaList.remove(i));
					System.out.println("Mana orb Deletion");
					break;
				}
			}
		}
	}
}