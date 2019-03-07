import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.io.File;
import javax.sound.sampled.*;

public class Player {
	
	//Image Variables
	public BufferedImage playerImage;
	public URL imageFile = getClass().getResource("IdleImages/idle0.png");
	

	//Character Position
	public int xPos = 50;
	public int yPos = 370;
	public int height;
	public int width;
	public int state = 0;

	//Player stats
	public int healthBar = 200;
	public int power = 7;
	public int defense = 2;
	public int manaPoints = 10;

	//Character states
	public boolean isIdle = true;
	public boolean isFacingRight = true;
	public boolean isJumping = false;
	public boolean isUsingMagic = false;
	public boolean isDead = false;
	public boolean usingSword = false;

	//Gravity
	public int gravity = 370;

	//Player Attacks states
	public ArrayList<MagicMissle> missleList = new ArrayList<>(10);
	public MagicMissle magicMissle[] = new MagicMissle[10];
	public int magicAmmo = 1;

	public Draw canvas;

	public Player(Draw canvas){
		try{
			playerImage = ImageIO.read(imageFile);

		}catch(IOException e){
			e.printStackTrace();
		}

		this.canvas = canvas;
		standingAnimation(canvas);
	}

	public Player(int x, int y, Draw canvas){
		x = xPos;
		y = yPos;


		try{
			playerImage = ImageIO.read(imageFile);
		}catch(IOException e){
			e.printStackTrace();
		}

		this.canvas = canvas;

		//Assign image size do variables
		width = playerImage.getWidth();
		height = playerImage.getHeight();

		standingAnimation(canvas);
	}

	//For collision detection sets the rectangle as the image size
	public Rectangle playerBounds(){
		return(new Rectangle (xPos, yPos, width, height));
	}

	//Method for Idle player
	public void standingAnimation(Draw canvas){
		isIdle = true;
		Thread idleThread = new Thread(new Runnable(){
			public void run(){
				//While no keys are pressed this code will run
				while(isIdle){
					for(int i = 0; i < 4; i++){
						if(isFacingRight == true){
							imageFile = getClass().getResource("IdleImages/idle"+i+".png");
						}else{
							imageFile = getClass().getResource("IdleImages/idleback"+i+".png");
						}
						//Check to see if character has died
						verifyDeath();

						try{
							playerImage = ImageIO.read(imageFile);
							Thread.sleep(1000/30);
							canvas.repaint();

						}catch(IOException e){
							e.printStackTrace();
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}					
				}
			}
		});
		idleThread.start();
	}
	//Method for player movement
	public void movementAnimation(){
		
			if(state < 6){
				if(isFacingRight == true){
					imageFile = getClass().getResource("runImages/run"+state+".png");
				}else{
					imageFile = getClass().getResource("runImages/runback"+state+".png");
				}
				state++;
			}else{
				state=0;
			}

		try{
			canvas.repaint();
			playerImage = ImageIO.read(imageFile);
		}catch(IOException e){
			e.printStackTrace();	
		}	
	}

	//Method for player jumping
	public void jumpingAnimation(){
		Thread jumpingThread = new Thread(new Runnable(){
			public void run(){
				for(int i = 0; i < 7; i++){
					if(isFacingRight == true){
						imageFile = getClass().getResource("jumImages/jump"+i+".png");
						yPos-=10;			
						xPos+=15;
					}else{
						imageFile = getClass().getResource("jumImages/jumpback"+i+".png");
						yPos-=10;		
						xPos-=15;						
					}
					try{
						canvas.repaint();
						playerImage = ImageIO.read(imageFile);
						Thread.sleep(1000/30);
					}catch(IOException e){
						e.printStackTrace();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
				//After Jumping go to fall method
				fallingAnimation();				
			}
		});		
		jumpingThread.start();
	}

	//Player Falling method
	public void fallingAnimation(){
		Thread fallingThread = new Thread(new Runnable(){
			public void run(){
				do{
					for(int i = 0; i < 4; i ++){
						if(isFacingRight == true){
							imageFile = getClass().getResource("fallImages/fall"+i+".png");
							
						}else{
							imageFile = getClass().getResource("fallImages/fallback"+i+".png");
						}
						yPos+=2;
					}							
					try{
						canvas.repaint();
						playerImage = ImageIO.read(imageFile);
						Thread.sleep(1000/30);

					}catch(IOException e){
						e.printStackTrace();
					}catch(InterruptedException e){
						e.printStackTrace();
					}	

				//Do this code while the player is Higher than the gravity variable					
				}while(yPos < gravity);
				//return to idle position after falling
				standingAnimation(canvas);
			}
		});
		fallingThread.start();
	}

	//Player crouching method
	public void crouchingAnimation(){
		if(state < 4){
			if(isFacingRight){
				imageFile = getClass().getResource("crouchImages/crouch"+state+".png");
			}else{				
				imageFile = getClass().getResource("crouchImages/crouchback"+state+".png");
			}
			state++;

		}else{
			state = 0;
		}	

		try{
			canvas.repaint();
			playerImage = ImageIO.read(imageFile);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void playerMagic(Draw canvas){
		
		Thread spellThread = new Thread(new Runnable(){
			public void run(){
				for(int i = 0; i < 7; i++){
					if(isFacingRight == true){
						imageFile = getClass().getResource("spellImages/spell" +i+".png");
					}else{
						imageFile = getClass().getResource("spellImages/spell" +i+".png");
					}

					try{
						canvas.repaint();
						playerImage = ImageIO.read(imageFile);
						Thread.sleep(1000/30);
					}catch(IOException e){
						e.printStackTrace();

					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
				//Go to fire magic method
				fireMagic(canvas);
				//Return to idle animation
				standingAnimation(canvas);
			}
		});
		spellThread.start();
	}

	//Player Magic Creation method
	public void fireMagic(Draw canvas){
		//The Magic Array List is not equal to 11 do this code
		if(missleList.size() != 11){
			
			//Creates a new magic object
			magicMissle[magicAmmo] = new MagicMissle(xPos + 20, yPos, canvas);
			
			//Adds that new magic object to an arraylist
			missleList.add(magicMissle[magicAmmo]);
			//Increases magicAmmo by 1
			magicAmmo++;
			//Changes isUsingMagic to true
			isUsingMagic = true;
		}else{

		}	
		System.out.println(missleList.size() +"MISSLE LIST");
	}

	public void swordAttack(){
		Thread swordThread = new Thread(new Runnable(){
			public void run(){
				for(int i = 0; i < 15; i++){
					if(usingSword == true){
						if(isFacingRight == true){
							imageFile = getClass().getResource("swordImages/sword" +i+".png");
						}else{
							imageFile = getClass().getResource("swordImages/swordback" +i+".png");
						}
					}
					
					try{
						
						canvas.repaint();
						playerImage = ImageIO.read(imageFile);
						Thread.sleep(1000/30);

						

					}catch(IOException e){
						e.printStackTrace();
					}catch(InterruptedException e){
						e.printStackTrace();
					}catch(UnsupportedAudioFileException e){
						e.printStackTrace();
					}catch(LineUnavailableException e){
						e.printStackTrace();
					}
				}
				usingSword = false;
				standingAnimation(canvas);
			}
		});
		swordThread.start();
	}

	//Verify player health points
	public void verifyDeath(){
		//if player hp is less than 0 play the death animation
		if(healthBar <= 0){
			isDead = true;
			isIdle = false;
			playerDeath();
		}
	}

	public void playerDeath(){
		
		for(int i = 0; i < 7; i++){	
			if(isFacingRight == true){
				imageFile = getClass().getResource("deathImages/die"+i+".png");
			
			}else{
				imageFile = getClass().getResource("deathImages/dieback"+i+".png");
			}

			try{
				canvas.repaint();
				playerImage = ImageIO.read(imageFile);

			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	public void moveLeft(){
		xPos-= 5;
		isIdle = false;
		isFacingRight = false;
		verifyDeath();
		movementAnimation();
	}

	public void moveRight(){
		xPos = xPos + 5;
		isIdle = false;
		isFacingRight = true;
		verifyDeath();
		movementAnimation();
	}

	public void jump(){
		isIdle = false;
		verifyDeath();
		jumpingAnimation();
	}

	public void crouch(){
		isIdle = false;
		verifyDeath();
		crouchingAnimation();
	}
	public void useMagic(Draw canvas){
		isIdle = false;
		verifyDeath();
		playerMagic(canvas);
	}

	public void useSword(){
		isIdle = false;
		isUsingMagic = false;
		usingSword = true;
		canvas.collisionDetection();
		verifyDeath();
		swordAttack();
	}
}