import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.awt.Rectangle;

public class Creature{
	
	//Creature Image variables
	public BufferedImage creatureImage;
	public URL creatureFile = getClass().getResource("slimeIdle/slime0.png");

	//Creature Position
	public int xPos;
	public int yPos;
	public int height;
	public int width;

	//Creature Stats
	public int health = 35;
	public int power = 4;

	//Creature States
	public boolean isIdle = false;
	public boolean isMoving = true;
	public boolean isFacingRight = false;
	public boolean isAttacking = false;
	public boolean isDead = false;

	public Draw draw;

	public Creature(Draw canvas){

		try{
			creatureImage = ImageIO.read(creatureFile);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public Creature(int x, int y, Draw canvas){

		xPos = x;
		yPos = y;

		//Gets Creature from file
		try{
			creatureImage = ImageIO.read(creatureFile);
		}catch(IOException e){
			e.printStackTrace();
		}

		//Assigns image size to variables
		this.draw = canvas;
		this.width = creatureImage.getWidth();
		this.height = creatureImage.getHeight();

		idleCreature();
	}

	//For collision detection places creature image inside a rectangle
	public Rectangle creatureBounds(){
		return (new Rectangle(xPos, yPos, width, height));
	}

	//checks what direction the creature is facing
	public boolean creatureDirection(){
		if(xPos < draw.player.xPos){
			return isFacingRight = false;
		}else{
			return isFacingRight = true;
		}
	}

	//if creature is idle do this method
	public void idleCreature(){
		Thread idleThread = new Thread(new Runnable(){
			public void run(){
				//if isIdle is true do this code
				while(isIdle){
					for(int i = 0; i < 4; i ++){
						//Assigns array of images to the URL variable
						creatureFile = getClass().getResource("slimeIdle/slime" + i + ".png ");

						try{
							//Repaints image in Draw class
							draw.repaint();
							creatureImage = ImageIO.read(creatureFile);
							Thread.sleep(150);

						}catch(IOException e){
							e.printStackTrace();
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
				}
			}
		});
		//Starts thread
		idleThread.start();
	}

	//Creature movement method
	public void movementCreature(){
		isIdle = false;
		isMoving = true;
		creatureDirection();
		Thread moveThread = new Thread(new Runnable(){
			public void run(){
				while(isMoving){
					for(int i = 0; i < 4; i++){
						//iF creature is facing right use facing right images 
						if(isFacingRight == true){
							if(xPos > draw.player.xPos){
								//Changes the position of the image to right side by 1
								xPos--;
								creatureFile = getClass().getResource("slimeIdle/move"+i+".png");
							}
							
						//else use facing left images and move to the left
						}else{
							creatureFile = getClass().getResource("slimeIdle/moveback"+i+".png");
							xPos++;
						}
						try{
							draw.repaint();
							creatureImage = ImageIO.read(creatureFile);
							Thread.sleep(333);

						}catch(IOException e){
							e.printStackTrace();
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
				}		
			}
		});
		moveThread.start();
	}

	//Creature attacking method
	public void attackCreature(){
		
		//Turns creature moving off
		isMoving = false;

		//Gets creature direction
		creatureDirection();
		
		for(int i = 0; i < 5; i++){
			//If creature direction is right and creature isAttacking is on do this code
			if(isFacingRight == true && isAttacking == true){
				creatureFile = getClass().getResource("slimeIdle/attack" + i+".png");
				xPos-=5;
			}else{
				creatureFile = getClass().getResource("slimeIdle/attackback" + i+".png");
				xPos+=5;
			}

			try{
				draw.repaint();
				creatureImage = ImageIO.read(creatureFile);
				Thread.sleep(200);

			}catch(IOException e){
				e.printStackTrace();
			
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	//Checks monsters Health Points returns true if creature is dead, false if alive
	public boolean checkHealth(){
		
		if(health <= 0){
			isAttacking = false;
			isMoving = false;
			return true;
		}
		return false;
	}

	//creature death method
	public void creatureDeath(){
	
		for(int i = 0; i < 4; i++){
			creatureFile = getClass().getResource("slimeIdle/die" + i+".png");

			try{
				draw.repaint();
				creatureImage = ImageIO.read(creatureFile);
				Thread.sleep(300);
			}catch(IOException e){
				e.printStackTrace();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		System.out.println("Creature Death");
		//Changes status of death to true
		isDead = true;
	}
}