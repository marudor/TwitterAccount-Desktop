package de.marudor;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TwitterCreation {

	private JFrame frame;
	private TwitterInstance twitter;
	private JTextField textField;
	private JLabel infoLabel;
	private final JLabel statLabel = new JLabel("Statistic");
	private List<String> namelist;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TwitterCreation window = new TwitterCreation();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TwitterCreation() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			InputStream is = new URL("http://twitter.marudor.de/names.txt").openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			namelist = new ArrayList<String>();
			String line;
		while ((line = br.readLine()) != null)
			namelist.add(line);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 145);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel captchaLabel = new JLabel("New label");
		captchaLabel.setBounds(0, 0, 300, 57);
		frame.getContentPane().add(captchaLabel);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() != KeyEvent.VK_ENTER)
					return;
				String captcha = textField.getText();
				Account acc = twitter.createAccount(captcha);
				if (acc == null) {
					infoLabel.setText("Wrong Captcha");
				}
				else {
					acc.upload();
					infoLabel.setText("Success");
				}
				textField.setText("");
				
				newSession();
			}
		});
		textField.setBounds(0, 57, 300, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		infoLabel = new JLabel("Solve Captcha, press Enter");
		infoLabel.setBounds(0, 85, 300, 18);
		frame.getContentPane().add(infoLabel);
		statLabel.setBounds(0, 103, 300, 19);
		frame.getContentPane().add(statLabel);
		
		
		newSession();
	}
	
	
	public void newSession() {
		twitter = new TwitterInstance(namelist);
		JLabel captchaLabel = (JLabel)frame.getContentPane().getComponent(0);
		captchaLabel.setIcon(new ImageIcon(twitter.getCaptchaImage()));
		statLabel.setText(twitter.getStatistic());
	}
}
