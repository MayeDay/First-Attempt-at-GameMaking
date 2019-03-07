import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.awt.Rectangle;
import java.util.ArrayList;

public class ManaAmount{

	//Image variables
	public BufferedImage manaImage;
	public URL manaFile = getClass().getResource("manaImages/mana0.png");

	public Draw canvas;

	//Image Position
	public int xPos; 
	public int yPos;

	public ManaAmount(int x, int y, Draw canvas){

		this.canvas = canvas;

		this.xPos = x;
		this.yPos = y;

		try{
			manaImage = ImageIO.read(manaFile);
		}catch(IOException e){
			e.printStackTrace();
		}
		manaCycle();
	}	

	public void manaCycle(){
		Thread manaThread = new Thread(new Runnable(){
			public void run(){
				while(true){
					for(int i = 0; i < 8; i++){
						manaFile = getClass().getResource("manaImages/mana"+ i +".png");

						try{
							manaImage = ImageIO.read(manaFile);
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
		manaThread .start();
	}
}