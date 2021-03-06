import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Fish {
	private BufferedImage image;

	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;

	private int x;
	private int y;

	private int deltaX;
	private int deltaY;

	private Color color;
	private SeaView parentSea;
	private Graphics2D g2d;
	
	public Fish(SeaView parent, int x, int y) {
		this.parentSea = parent;

		this.x = x;
		this.y = y;
		deltaX = RandomUtilities.random(-4, 4) + 1;
		deltaY = RandomUtilities.random(-4, 4) + 2;

		URL resource = getClass().getResource("/nemo.png");
		try {
			image = ImageIO.read(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}

		color = new Color(RandomUtilities.random(0, 255), RandomUtilities.random(0, 255),
				RandomUtilities.random(0, 255));
	}
	
	public void move() {

		x += deltaX;
		y += deltaY;

		if (x + WIDTH > parentSea.getWidth()) {
			x = parentSea.getWidth() - WIDTH;
			deltaX *= -1;
		} else if (x < 0) {
			x = 0;
			deltaX *= -1;
		}
		if (y + HEIGHT > parentSea.getHeight()) {
			y = parentSea.getHeight() - HEIGHT;
			deltaY *= -1;
		} else if (y < 0) {
			y = 0;
			deltaY *= -1;
		}
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void paint(Graphics2D g2d) {
		this.g2d = g2d;
		
		if(null == this.g2d && null != parentSea){
			this.g2d = (Graphics2D) parentSea.getGraphics().create();
		}
		this.g2d.drawImage(image, x, y, parentSea);
	}
	
	public void findBait(int targetX, int targetY) {

		boolean isBackX = false;
		boolean isBackY = false;

		if (targetX < x) {
			isBackX = true;
		}

		if (targetY < y) {
			isBackY = true;
		}

		int stateMoveCount = Math.abs(targetX - x) + Math.abs(targetY - y);
		int[] binaryList = createBinary(stateMoveCount, Math.abs(targetY - y));
		move(binaryList, isBackX, isBackY);
	}

	private int[] createBinary(int stateMoveCount, int k) {

		int[] binary = new int[stateMoveCount];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = 0;
		}

		boolean stop = false;
		
		while (!stop) {
			int j = stateMoveCount - 1;
			while (j > 0 && binary[j] > 0) {
				binary[j] = 0;
				j--;
			}

			if (j == 0) {
				stop = true;
			} else {
				binary[j] = 1;
			}

			if (checkWithK(binary, k)) {
				return binary;
			}
		}

		return new int[stateMoveCount];
	}

	private boolean checkWithK(int[] binary, int k) {

		int count = 0;
		for (int i = 0; i < binary.length; i++) {
			count += binary[i];
		}

		if (count == k) {
			return true;
		}

		return false;
	}

	private void move(int[] binaryList, boolean isBackX, boolean isBackY) {

		for (int i = 0; i < binaryList.length; i++) {

			int deltaLocalX = 0;
			int deltaLocalY = 0;

			if (binaryList[i] == 0) {
				deltaLocalX++;
			}

			if (binaryList[i] == 1) {
				deltaLocalY++;
			}

			if (isBackX) {
				x -= deltaLocalX;
			} else {
				x += deltaLocalX;
			}

			if (isBackY) {
				y -= deltaLocalY;
			} else {
				y += deltaLocalY;
			}

			/*
			 * if (x + WIDTH > parentSea.getWidth()) { x =
			 * parentSea.getWidth() - WIDTH; deltaLocalX *= -1; } else if (x
			 * < 0) { x = 0; deltaLocalX *= -1; } if (y + HEIGHT >
			 * parentSea.getHeight()) { y = parentSea.getHeight() - HEIGHT;
			 * deltaLocalY *= -1; } else if (y < 0) { y = 0; deltaLocalY *=
			 * -1; }
			 */
			if(null == g2d && null != parentSea){
				g2d = (Graphics2D) parentSea.getGraphics().create();
			}
			g2d.drawImage(image, x, y, parentSea);
		}
	}
}
