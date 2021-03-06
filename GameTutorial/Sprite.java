import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Sprite{
	
	private BufferedImage image;

	public Sprite(SpriteSheet spritesheet, int x, int y){
		this.image = spritesheet.getTexture().getImage().getSubimage((x * spritesheet.getWidth()) - spritesheet.getWidth(), 
			(y * spritesheet.getHeight()) - spritesheet.getHeight(), spritesheet.getWidth(), spritesheet.getHeight());
	}
	public void render(Graphics g, double x, double y){
		g.drawImage(image, (int) x, (int) y, null);
	}	
}