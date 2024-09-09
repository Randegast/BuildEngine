package buildengine.core;

import buildengine.BuildEngine;
import buildengine.graphics.Draw;
import buildengine.input.Input;
import buildengine.io.AssetManager;
import buildengine.math.vector.Vector2f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * All information and methods concerning the display, resolution and canvas.
 */
public class Display extends JFrame {

	public static final Dimension RESOLUTION_720p = new Dimension(1280, 720);
	public static final Dimension RESOLUTION_1080p = new Dimension(1920, 1080);
	public static Dimension RESOLUTION_CUSTOM(int w, int h) {
		return new Dimension(w, h);
	}

	public static final Dimension MINIMUM_RESOLUTION = new Dimension(178, 100);

	public static final Color BACKGROUND_COLOR = Color.BLACK;

	@Serial
	private static final long serialVersionUID = 256L;

	private Canvas canvas;
	private Dimension size;
	private boolean borderless;
	private Runnable closeEvent, resizeEvent;

	public Display(String title, Dimension size, boolean borderless) {
		this.size = size;
		this.borderless = borderless;

		setTitle(title);
		initCanvas();
		initFrame();

		addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {}

			@Override
			public void focusLost(FocusEvent e) {
				Input.reset();
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(closeEvent != null)
					closeEvent.run();
				exit();
			}
		});
	}

	private void initFrame() {
		setSize(size);
		setMinimumSize(MINIMUM_RESOLUTION);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(true);
		setLocationRelativeTo(null);
		setUndecorated(borderless);
		
		// IconImage
		List<Image> icon = new ArrayList<>();
		icon.add(AssetManager.getImage("src/buildengine/logo20x20.png"));
		setIconImages(icon);

		add(canvas);
		pack();
	}

	private void initCanvas() {
		canvas = new Canvas();
		canvas.setPreferredSize(size);
		canvas.setBackground(BACKGROUND_COLOR);
		canvas.setFocusable(false);
	}

	// Resizing
	@Override
	public void validate() {
		super.validate();
		resize();
	}

	public void resize() {
		size = canvas.getSize();
		canvas.setSize(size);
		Draw.resize(size);
		if(resizeEvent != null)
			resizeEvent.run();
	}

	public void exit() {
		if(BuildEngine.getEngine() != null)
			BuildEngine.getEngine().stop();
		else
			System.exit(0);
	}

	public void setResizeEvent(Runnable resizeEvent) {
		this.resizeEvent = resizeEvent;
	}

	public void setCloseEvent(Runnable closeEvent) {
		this.closeEvent = closeEvent;
	}

	// Getters and setters

	public Canvas getCanvas() {
		return canvas;
	}

	public int getWidth() {
		return size.width;
	}

	public int getHeight() {
		return size.height;
	}

	public boolean isBorderless() {
		return borderless;
	}

	public void setBorderless(boolean borderless) {
		this.borderless = borderless;
	}
	
	public Vector2f getCenter() {
		return new Vector2f((float) size.getWidth() / 2, (float) size.getHeight() / 2);
	}
}
