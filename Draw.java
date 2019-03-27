import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;

public class Draw extends JComponent{

	private BufferedImage image;
	private BufferedImage backgroundImage;
	public URL resource = getClass().getResource("stance0.png");

	// circle's position
	public int x = 300;
	public int y = 300;
	public int height = 0;
	public int width = 0;
	public int direction = 0;
	/*right = 0 left= 1*/

	// animation states
	public int state = 0;

	// randomizer
	public Random randomizer;

	// enemy
	public int enemyCount;
	Monster[] monsters = new Monster[10];
	knight[] knights = new knight[10];

	public Draw(){
		randomizer = new Random();
		spawnEnemy();
		
		try{
			image = ImageIO.read(resource);
			backgroundImage = ImageIO.read(getClass().getResource("background.jpg"));
		}
		catch(IOException e){
			e.printStackTrace();
		}

		height = image.getHeight();
		width = image.getWidth();

		startGame();
	}

	public void startGame(){
		Thread gameThread = new Thread(new Runnable(){
			public void run(){
				while(true){
					try{
						for(int c = 0; c < monsters.length; c++){
							if(monsters[c]!=null){
								monsters[c].moveTo(x,y);
								repaint();
							}
						}
						Thread.sleep(100);
						for(int c = 0; c < knights.length; c++){
							if(knights[c]!=null){
								knights[c].moveTo(x,y);
								repaint();
							}
						}
					} catch (InterruptedException e) {
							e.printStackTrace();
					}
				}
			}
		});
		gameThread.start();
	}

	public void spawnEnemy(){
		if(enemyCount < 10){
			monsters[enemyCount] = new Monster(randomizer.nextInt(500), randomizer.nextInt(500), this);
			knights[enemyCount] = new knight(randomizer.nextInt(500), randomizer.nextInt(500), this);
			enemyCount++;
		}
	}

	public void reloadImage(){
		state++;
		if(direction == 0){
			if(state == 0){
				resource = getClass().getResource("dash0.png");
			}
			else if(state == 1){
				resource = getClass().getResource("dash1.png");
			}
			else if(state == 2){
				resource = getClass().getResource("dash2.png");
			}
			else if(state == 3){
				resource = getClass().getResource("dash3.png");
			}
			else if(state == 4){
				resource = getClass().getResource("dash4.png");
			}
			else if(state == 5){
				resource = getClass().getResource("dash5.png");
			}
			else if(state == 6){
				resource = getClass().getResource("dash6.png");
			}
			else if(state == 7){
				resource = getClass().getResource("dash7.png");
				state = 0;
			}
		} else{
			if(state == 0){
				resource = getClass().getResource("dash0R.png");
			}
			else if(state == 1){
				resource = getClass().getResource("dash1R.png");
			}
			else if(state == 2){
				resource = getClass().getResource("dash2R.png");
			}
			else if(state == 3){
				resource = getClass().getResource("dash3R.png");
			}
			else if(state == 4){
				resource = getClass().getResource("dash4R.png");
			}
			else if(state == 5){
				resource = getClass().getResource("dash5R.png");
			}
			else if(state == 6){
				resource = getClass().getResource("dash6R.png");
			}
			else if(state == 7){
				resource = getClass().getResource("dash7R.png");
				state = 0;
			}
		}
		try{
			image = ImageIO.read(resource);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public void attackAnimation(){
		Thread thread1 = new Thread(new Runnable(){
			public void run(){
				if(direction == 0){
					for(int ctr = 1; ctr < 4; ctr++){
						try {
							if(ctr==3){
								resource = getClass().getResource("stance0.png");
							}
							else{
								resource = getClass().getResource("attack"+ctr+".png");
							}
							
							try{
								image = ImageIO.read(resource);
							}
							catch(IOException e){
								e.printStackTrace();
							}
							repaint();
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}else{
					for(int ctr = 1; ctr < 4; ctr++){
					try {
						if(ctr==3){
							resource = getClass().getResource("stance0R.png");
						}
						else{
							resource = getClass().getResource("attack"+ctr+"R.png");
						}
						
						try{
							image = ImageIO.read(resource);
						}
						catch(IOException e){
							e.printStackTrace();
						}
				        repaint();
				        Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	
				}
				for(int x=0; x<monsters.length; x++){
					if(monsters[x]!=null){
						if(monsters[x].contact){
							monsters[x].life = monsters[x].life - 10;
						}
					}
				}
				for(int x=0; x<knights.length; x++){
					if(knights[x]!=null){
						if(knights[x].contact){
							knights[x].life = knights[x].life - 10;
						}
					}
				}
			}
		});
		thread1.start();
	}

	public void attack(){
		attackAnimation();
	}

	public void moveUp(){
		y = y - 5;
		reloadImage();
		repaint();
		checkCollision();
	}

	public void moveDown(){
		y = y + 5;
		reloadImage();
		repaint();
		checkCollision();
	}

	public void moveLeft(){
		direction = 1;
		x = x - 5;
		reloadImage();
		repaint();
		checkCollision();
	}

	public void moveRight(){
		direction = 0;
		x = x + 5;
		reloadImage();
		repaint();
		checkCollision();
	}

	public void checkCollision(){
		int xChecker = x + width;
		int yChecker = y;

		for(int x=0; x<monsters.length; x++){
			boolean collideX = false;
			boolean collideY = false;

			if(monsters[x]!=null){
				monsters[x].contact = false;

				if(yChecker > monsters[x].yPos){
					if(yChecker-monsters[x].yPos < monsters[x].height){
						collideY = true;
						System.out.println("collideY");
					}
				}
				else{
					if(monsters[x].yPos - (yChecker+height) < monsters[x].height){
						collideY = true;
						System.out.println("collideY");
					}
				}

				if(xChecker > monsters[x].xPos){
					if((xChecker-width)-monsters[x].xPos < monsters[x].width){
						collideX = true;
						System.out.println("collideX");
					}
				}
				else{
					if(monsters[x].xPos-xChecker < monsters[x].width){
						collideX = true;
						System.out.println("collideX");
					}
				}
			}
			
			

			if(collideX && collideY){
				System.out.println("collision!");
				monsters[x].contact = true;
			}
		}
		
		for(int x=0; x<knights.length; x++){
			boolean collideX = false;
			boolean collideY = false;

			if(knights[x]!=null){
				knights[x].contact = false;

				if(yChecker > knights[x].yPos){
					if(yChecker-knights[x].yPos < knights[x].height){
						collideY = true;
						System.out.println("collideY");
					}
				}
				else{
					if(knights[x].yPos - (yChecker+height) < knights[x].height){
						collideY = true;
						System.out.println("collideY");
					}
				}

				if(xChecker > knights[x].xPos){
					if((xChecker-width)-knights[x].xPos < knights[x].width){
						collideX = true;
						System.out.println("collideX");
					}
				}
				else{
					if(knights[x].xPos-xChecker < knights[x].width){
						collideX = true;
						System.out.println("collideX");
					}
				}
			}
			
			

			if(collideX && collideY){
				System.out.println("collision!");
				monsters[x].contact = true;
				knights[x].contact = true;
			}
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, this);

		// character grid for hero
		// g.setColor(Color.YELLOW);
		// g.fillRect(x, y, width, height);
		g.drawImage(image, x, y, this);
		
		for(int c = 0; c < monsters.length; c++){		
			if(monsters[c]!=null){
				// character grid for monsters
				// g.setColor(Color.BLUE);
				// g.fillRect(monsters[c].xPos, monsters[c].yPos+5, monsters[c].width, monsters[c].height);
				g.drawImage(monsters[c].image, monsters[c].xPos, monsters[c].yPos, this);
				g.setColor(Color.GREEN);
				g.fillRect(monsters[c].xPos+7, monsters[c].yPos, monsters[c].life, 2);
			}	
		}
		
		for(int c = 0; c < knights.length; c++){		
			if(knights[c]!=null){
				// character grid for monsters
				// g.setColor(Color.BLUE);
				// g.fillRect(monsters[c].xPos, monsters[c].yPos+5, monsters[c].width, monsters[c].height);
				g.drawImage(knights[c].image, knights[c].xPos, knights[c].yPos, this);
				g.setColor(Color.GREEN);
				g.fillRect(knights[c].xPos+7, knights[c].yPos, knights[c].life, 2);
			}	
		}
	}

	public void checkDeath(){
		for(int c = 0; c < monsters.length; c++){
			if(monsters[c]!=null){
				if(!monsters[c].alive){
					monsters[c] = null;
				}
			}			
		}
		
		for(int c = 0; c < knights.length; c++){
			if(knights[c]!=null){
				if(!knights[c].alive){
					knights[c] = null;
				}
			}			
		}
	}
}