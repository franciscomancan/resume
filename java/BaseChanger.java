import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

///////////////////////////////////////////////////////////////////////////////////
/**
*	BaseChanger: an interactive applet that takes numeric
*	strings, representing any of the four primary number
*	bases used in computer science, and converts the value
*	to the other respective numeric values
*	(i.e. binary converts to --> oct,dec & hex bases).
*
*	This is the front end or GUI element of the application,
*	implemented in the javax (swing) library.
*	All logic for conversions are taken care of by the
*	NumberSystems object.
*
*	@author anthony francis
*	@version 5.17.1998
*
*	@see NumberSystems.java
*/

public class BaseChanger extends JApplet {

		/** consts. kept separate */
	private static final int TOT_CONVERSIONS = 3;
	private static final int TEXT_INPUT_LENGTH = 20;
	private static final int WIDTH = 425, HEIGHT = 350;
	private static final String INIT_MSSG = "(input numeric string here)";
	private static final String INFO[] = {
			"This app. can be used to convert between common number systems.",
			"Follow three simple steps:                           ",
			"   1) enter a numeric value in the text field",
			"   2) select the number base of which it is a part",
			"   3) press enter or press the \"Display Conversions\" button for results",
			"                                                                                                         " };

		/** style constants */
	private static final Font verdana = Font.getFont("Verdana");
	private static final Color backgroundColor = new Color(195,63,22); //brick
	private static final Color textColor = Color.white;
	private static final PrintStream o = System.out;
	private static final String windowsClassName =
	    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

		//	all gui handles are first established
	JTextField input = new JTextField(INIT_MSSG,TEXT_INPUT_LENGTH);
	JButton cButton = new JButton("Display Conversions"),
				clButton = new JButton("Clear"),
				eButton = new JButton("Exit");
	ButtonGroup bases = new ButtonGroup();
	JRadioButton
		binRadio = new JRadioButton("binary input"),
		octRadio = new JRadioButton("octal input"),
		decRadio = new JRadioButton("decimal input"),
		hexRadio = new JRadioButton("hex input");
	JLabel[] output = new JLabel[TOT_CONVERSIONS];
	JLabel[] info = new JLabel[INFO.length];

		/* separate panels used for layout purposes */
	Panel infoPanel, ioPanel, radioPanel, buttonPanel;

		/* object that does the work */
	private NumberSystems engine;
	private String[] results = new String[TOT_CONVERSIONS];

	/**************************************************
	/** The attributes of all elements in the
	*	gui displayed are then set here in the
	*	constructor (same as init)
	*/
	public BaseChanger () {
		engine = new NumberSystems();
		engine.doConversions();
		results = engine.getConversions();

			/* main contentPane */
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout() /*new BoxLayout(pane,BoxLayout.Y_AXIS)*/ );
		pane.setSize(WIDTH,HEIGHT);
		pane.setBackground(backgroundColor);


			/** Lesson here: when using box layout in this manner,
			*	the nested panel must be instantiated in a separate
			*	statement.  Runtime errors occurred when instantiating
			*	the panel with the layout in the constuctor.
			*/
		infoPanel = new Panel();
		infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.Y_AXIS));
		for(int i=0; i<info.length; i++) {
			info[i] = new JLabel(INFO[i]);
			info[i].setForeground(textColor);
			infoPanel.add(info[i]);
		}
		pane.add("North",infoPanel);

			/* text field */
		input.setFont(verdana);
			/* Lesson: this is the only way found to force field dimensions */
		input.setMaximumSize(new Dimension(700,25));
			/** listener defined here to
			*	take action when ENTER key
			*	on keyboard is pressed*/
		input.addKeyListener(new TextFieldListener());
		ioPanel = new Panel();
		ioPanel.setLayout(new BoxLayout(ioPanel,BoxLayout.Y_AXIS));
		ioPanel.add(input);

			/* insert radio buttons into group (allows only
			*	one selection at a time) and pane */
		bases.add(binRadio); bases.add(octRadio);
		bases.add(decRadio); bases.add(hexRadio);
		Enumeration e = bases.getElements();
		JRadioButton tmp;
		while(e.hasMoreElements()) {
			tmp = (JRadioButton)e.nextElement();
			tmp.setFont(verdana);
			tmp.setBackground(backgroundColor);
			tmp.setForeground(textColor);
			ioPanel.add(tmp);
		}
		pane.add(ioPanel);

			/* init output labels */
		for(int i=0; i<TOT_CONVERSIONS; i++) {
				output[i] = new JLabel(results[i]);
				output[i].setForeground(textColor);
				ioPanel.add(output[i]);
		}

			/* add primary button with action listener */
		buttonPanel = new Panel();
		cButton.addActionListener(new MainButtonListener());
		buttonPanel.add(cButton);
		clButton.addActionListener(new ClearListener());
		buttonPanel.add(clButton);
		eButton.addActionListener(new ExitButtonListener());
		buttonPanel.add(eButton);
		pane.add("South",buttonPanel);

	}

	/**************************************************/
	/** runConvertProcedure: occurs when enter key
	*	or the "conversion" button in applet is pressed.
	*/
	private void runConvertProcedure() {
			try {
				engine.setNum(input.getText());
				if(binRadio.isSelected()) {
					if(input.getText().equals(INIT_MSSG)) { throw new NumberFormatException(); }
					displayResults(NumberSystems.BINARY_BASE);
				}
				else if(octRadio.isSelected()) {
					displayResults(NumberSystems.OCTAL_BASE);
				}
				else if(decRadio.isSelected()) {
					displayResults(NumberSystems.DECIMAL_BASE);
				}
				else if(hexRadio.isSelected()) {
					displayResults(NumberSystems.HEX_BASE);
				}
				else {
					input.setText("Error: number base from which converting must be set below");
				}
			} catch(NumberFormatException ne)
				{ input.setText("Input exception: try again, make sure correct base selected"); }
	}

	/**************************************************/
	/** displayResults: a shorthand method used
	*	to minimize the repetition of code
	*/
	private void displayResults(int b) {
		engine.setBase(b);
		engine.doConversions();
		results = engine.getConversions();
		for(int i=0; i<TOT_CONVERSIONS; i++) {
			output[i].setText(results[i]);
		}
	}

	/**************************************************/

	/** Listener defined to detect when the
	*	"Display conversions" button is pressed
	*	on the panel of the applet itself.
	*/
	class MainButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			runConvertProcedure();
		}
	}

	/**************************************************
	/** Listener defined to detect when ENTER key
	*	is pressed (keyboard).
	*/
	class TextFieldListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER)
				runConvertProcedure();
		}
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
	}

	/**************************************************
	/** Listener defined to detect when the user
	*	would like to exit the app, aside from
	*	window actions.
	*/
	class ExitButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	class ClearListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			input.setText("");
		}
	}

	/**************************************************
	/**	The applet can also be run as
	*	a stand-alone frame within this
	*	main method.
	*/
	public static void main(String[] argv) {
		BaseChanger app = new BaseChanger();
		String titlebar = new String("Base Changer");
		JFrame frame = new JFrame(titlebar);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		try {
			UIManager.setLookAndFeel(windowsClassName);
			SwingUtilities.updateComponentTreeUI(frame);
			frame.pack();
		} catch(Exception eo) { eo.printStackTrace(); }

		frame.getContentPane().add(app,BorderLayout.CENTER);
		frame.setSize(WIDTH,HEIGHT);
		frame.setResizable(false);
		frame.setVisible(true);
	}


} //end BaseChanger
