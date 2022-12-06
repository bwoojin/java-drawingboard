package drawingboard_package;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DrawingBoard {
	static RectPanel rectPanel = new RectPanel();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

	private static void createAndShowGUI() {
		System.out.println("Created GUI on EDT?" + SwingUtilities.isEventDispatchThread());
		JFrame f = new JFrame("Swing Paint Demo");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(rectPanel, BorderLayout.NORTH);
		f.pack();
		f.setVisible(true);

	}

	public static void chcursor(String cur) {
		if (cur == "선" || cur == "원" || cur == "네모" || cur == "펜" || cur == "지우개" || cur == "그림") {
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image cursorimage = tk.getImage("C:\\Users\\dntjd\\Desktop\\img\\" + cur + ".png");// C:\\Users\\dntjd\\Desktop\\img\\자리에
																								// 커서 폴더 경로 지정
			Point point = new Point(10, 10);
			Cursor cursor = tk.createCustomCursor(cursorimage, point, "");
			rectPanel.setCursor(cursor);
		} else {
			rectPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

}

class RectPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

	String shapeString = ""; // 도형의 형태를 담는 변수
	Point firstPointer = new Point(0, 0);
	Point secondPointer = new Point(0, 0);
	BufferedImage bufferedImage;
	static Color colors = Color.black;
	Float stroke = (float) 5;
	JComboBox<Color> colorComboBox;
	// JComboBox<Float> strokeComboBox; // float로 설정해주는 이유는 setStroke에서 받는 인자 자료형이
	// float
	JComboBox<String> fillComboBox;
	JTextField pensizeTbox;

	boolean fill = false;
	int width;
	int height;
	int minPointx;
	int minPointy;

	public RectPanel() {

		pensizeTbox = new JTextField("10");
		colorComboBox = new JComboBox<Color>();
		// strokeComboBox = new JComboBox<Float>();
		fillComboBox = new JComboBox<String>();
		JButton eraseAllButton = new JButton("전체지우기");
		JButton strokeButton = new JButton("펜크기");
		JButton rectButton = new JButton("네모");
		JButton lineButton = new JButton("선");
		JButton circleButton = new JButton("원");
		JButton penButton = new JButton("펜");
		JButton eraseButton = new JButton("지우개");
		JButton pictureButton = new JButton("그림");
		JButton saveButton = new JButton("Save");
		JButton openButton = new JButton("Open");
		JButton colorChooser = new JButton("색지정");

		colorComboBox.setModel(new DefaultComboBoxModel<Color>(new Color[] { Color.black, Color.red, Color.blue,
				Color.green, Color.yellow, Color.pink, Color.magenta }));

		// strokeComboBox.setModel(new DefaultComboBoxModel<Float>(
		// new Float[] { (float) 5, (float) 10, (float) 15, (float) 20, (float) 25 }));
		fillComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { (String) "비우기", (String) "채우기" }));

		add(eraseAllButton);
		add(penButton);
		add(lineButton);
		add(rectButton);
		add(circleButton);
		// add(colorComboBox);
		add(colorChooser);
		// add(strokeComboBox);
		add(pensizeTbox);
		add(strokeButton);
		add(fillComboBox);
		add(eraseButton);
		add(pictureButton);
		add(saveButton);
		add(openButton);

		pensizeTbox.setColumns(5);

		Dimension d = getPreferredSize();
		bufferedImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
		setImageBackground(bufferedImage); // save 할 때 배경이 default로 black이여서 흰색으로

		eraseAllButton.addActionListener(this);
		rectButton.addActionListener(this);
		lineButton.addActionListener(this);
		circleButton.addActionListener(this);
		penButton.addActionListener(this);
		eraseButton.addActionListener(this);
		pictureButton.addActionListener(this);
		strokeButton.addActionListener(this);
		colorComboBox.addActionListener(this);
		// strokeComboBox.addActionListener(this);
		fillComboBox.addActionListener(this);
		colorChooser.addActionListener(this);
		saveButton.addActionListener(new SaveL(this, bufferedImage));
		openButton.addActionListener(new OpenL(this, bufferedImage));

		addMouseListener(this);
		addMouseMotionListener(this);

		eraseAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lineButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		circleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		penButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		eraseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pictureButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		strokeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		openButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

	}

	public void mousePressed(MouseEvent e) {

		// 다시 클릭됐을경우 좌표 초기화
		firstPointer.setLocation(0, 0);
		secondPointer.setLocation(0, 0);

		firstPointer.setLocation(e.getX(), e.getY());

	}

	public void mouseReleased(MouseEvent e) {

		if (shapeString != "펜") {
			secondPointer.setLocation(e.getX(), e.getY());
			updatePaint();
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().getClass().toString().contains("JButton")) {
			if (e.getActionCommand() == "색지정") {
				RectPanel.colorch();
			} else if (e.getActionCommand() == "펜크기") {
				String str1 = pensizeTbox.getText();
				stroke = (float) Integer.parseInt(str1);
			} else if (e.getActionCommand() == "전체지우기") {
				Graphics2D g = bufferedImage.createGraphics();
				setImageBackground(bufferedImage);
			} else {
				shapeString = e.getActionCommand();
				DrawingBoard.chcursor(shapeString);
			}
		}

		// else if (e.getSource().equals(colorComboBox)) {
		// colors = (Color) colorComboBox.getSelectedItem();
		// }

		// else if (e.getSource().equals(strokeComboBox)) {
		// stroke = (float) strokeComboBox.getSelectedItem();
		// }
		else if (e.getSource().equals(fillComboBox)) {
			if (fillComboBox.getSelectedItem() == "채우기") {
				fill = true;
			} else
				fill = false;
		}

	}

	static JColorChooser colorChooser = new JColorChooser();

	public Dimension getPreferredSize() {
		return new Dimension(1100, 1100);// 오픈 사이즈
	}

	public final static int CAP_ROUND = 1;

	public static void colorch() {

		ActionListener okListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				colors = colorChooser.getColor();
			}
		};
		final JDialog dialog = JColorChooser.createDialog(null, "test", true, colorChooser, okListener, null);
		dialog.show();
	}

	public void updatePaint() {

		width = Math.abs(secondPointer.x - firstPointer.x);
		height = Math.abs(secondPointer.y - firstPointer.y);

		minPointx = Math.min(firstPointer.x, secondPointer.x);
		minPointy = Math.min(firstPointer.y, secondPointer.y);

		Graphics2D g = bufferedImage.createGraphics();

		// draw on paintImage using Graphics
		switch (shapeString) {
		case ("선"):
			g.setColor(colors);
			g.setStroke(new BasicStroke(stroke));
			g.drawLine(firstPointer.x, firstPointer.y, secondPointer.x, secondPointer.y);

			break;

		case ("네모"):
			g.setColor(colors);
			g.setStroke(new BasicStroke(stroke));
			g.setColor(colors);
			if (fill == false)
				g.drawRect(minPointx, minPointy, width, height);
			else
				g.fillRect(minPointx, minPointy, width, height);

			break;

		case ("원"):
			g.setColor(colors);
			g.setStroke(new BasicStroke(stroke, CAP_ROUND, 1));
			if (fill == false)
				g.drawOval(minPointx, minPointy, width, height);
			else
				g.fillOval(minPointx, minPointy, width, height);

			break;

		case ("펜"):
			g.setColor(colors);
			g.setStroke(new BasicStroke(stroke, CAP_ROUND, 1));
			g.drawLine(firstPointer.x, firstPointer.y, secondPointer.x, secondPointer.y);
			break;

		case ("지우개"):
			g.setColor(Color.white);
			g.setStroke(new BasicStroke(stroke, CAP_ROUND, 1));
			g.drawLine(firstPointer.x, firstPointer.y, secondPointer.x, secondPointer.y);
			break;

		// case ("전체지우기"):
		// setImageBackground(bufferedImage);
		// shapeString ="";
		// break;
		case ("그림"):
			JFileChooser jFileChooser = new JFileChooser();
			;

			int rVal = jFileChooser.showOpenDialog(null);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jFileChooser.getSelectedFile();
				try {
					Image img = ImageIO.read(new File(selectedFile.getAbsolutePath()));
					if (firstPointer.x <= secondPointer.x) {
						g.drawImage(img, firstPointer.x, firstPointer.y, Math.abs(secondPointer.x - firstPointer.x),
								Math.abs(secondPointer.y - firstPointer.y), this);
					} else {
						g.drawImage(img, secondPointer.x, secondPointer.y, Math.abs(secondPointer.x - firstPointer.x),
								Math.abs(secondPointer.y - firstPointer.y), this);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {

			}
			break;

		default:
			break;

		}

		g.dispose();

		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bufferedImage, 0, 0, null);

	}

	public void setImageBackground(BufferedImage bi) {
		this.bufferedImage = bi;
		Graphics2D g = bufferedImage.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, 1100, 1100);// 바닥 캔버스 사이즈
		g.dispose();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

		width = Math.abs(secondPointer.x - firstPointer.x);
		height = Math.abs(secondPointer.y - firstPointer.y);

		minPointx = Math.min(firstPointer.x, secondPointer.x);
		minPointy = Math.min(firstPointer.y, secondPointer.y);

		if (shapeString == "펜" | shapeString == "지우개") {
			if (secondPointer.x != 0 && secondPointer.y != 0) {
				firstPointer.x = secondPointer.x;
				firstPointer.y = secondPointer.y;
			}
			secondPointer.setLocation(e.getX(), e.getY());
			updatePaint();
		} else if (shapeString == "선") {

			Graphics g = getGraphics();

			g.drawLine(firstPointer.x, firstPointer.y, secondPointer.x, secondPointer.y);
			secondPointer.setLocation(e.getX(), e.getY());
			repaint();
			g.dispose();
		} else if (shapeString == "네모") {

			Graphics g = getGraphics();
			g.setColor(Color.BLACK);
			g.setXORMode(getBackground());

			g.drawRect(minPointx, minPointy, width, height);
			secondPointer.setLocation(e.getX(), e.getY());
			repaint();
			g.dispose();
		} else if (shapeString == "원") {

			Graphics g = getGraphics();
			g.setColor(Color.BLACK);
			g.setXORMode(getBackground());

			g.drawOval(minPointx, minPointy, width, height);
			secondPointer.setLocation(e.getX(), e.getY());

			g.dispose();
			repaint();
		} else if (shapeString == "그림") {
			Graphics g = getGraphics();
			g.setColor(Color.BLACK);
			g.setXORMode(getBackground());

			g.drawRect(minPointx, minPointy, width, height);
			secondPointer.setLocation(e.getX(), e.getY());

			g.dispose();
			repaint();
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}// Class dotButton

class OpenL implements ActionListener {

	RectPanel rectPanel;
	BufferedImage bufferedImage;
	JFileChooser jFileChooser = new JFileChooser();;

	OpenL(RectPanel rectPanel, BufferedImage bufferedImage) {
		this.rectPanel = rectPanel;
		this.bufferedImage = bufferedImage;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG", "jpeg", "jpg", "png", "bmp", "gif");
		jFileChooser.addChoosableFileFilter(filter);

		int rVal = jFileChooser.showOpenDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jFileChooser.getSelectedFile();
			try {
				rectPanel.bufferedImage = ImageIO.read(new File(selectedFile.getAbsolutePath()));
				rectPanel.repaint();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {

		}

	}

}// class OpenL

class SaveL implements ActionListener {

	RectPanel rectPanel;
	BufferedImage bufferedImage;
	JFileChooser jFileChooser;

	SaveL(RectPanel rectPanel, BufferedImage bufferedImage) {
		this.rectPanel = rectPanel;
		this.bufferedImage = bufferedImage;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		jFileChooser = new JFileChooser();
		jFileChooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
		int rVal = jFileChooser.showSaveDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			File file = jFileChooser.getSelectedFile();
			try {
				ImageIO.write(bufferedImage, "png", new File(file.getAbsolutePath()));
				System.out.println("saved Correctly " + file.getAbsolutePath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Failed to save image");
			}
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			System.out.println("No file choosen");
		}

	}

}// class SaveL
