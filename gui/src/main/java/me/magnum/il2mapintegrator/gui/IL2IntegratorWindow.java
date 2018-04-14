package me.magnum.il2mapintegrator.gui;

import java.awt.AWTException;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import me.magnum.il2mapintegrator.core.Logger;

public class IL2IntegratorWindow extends JFrame {
	public interface LaunchActionListener {
		void onLaunchIL2();
		void onLaunchPWCG();
	}

	private LaunchActionListener actionListener;

	private Label ipLabel;
	private Button il2Button;
	private Button pwcgButton;

	private MenuItem ipMenuItem;

	public IL2IntegratorWindow(LaunchActionListener actionListener) {
		this.actionListener = actionListener;
		this.buildWindow();
		this.setupTray();
	}

	private void buildWindow() {
		GridBagLayout layout = new GridBagLayout();

		JPanel mainPanel = new JPanel(layout);
		mainPanel.setPreferredSize(new Dimension(300, 130));

		Label ipInfoLabel = new Label("IP Address:", Label.CENTER);
		GridBagConstraints ipInfoLabelConstrains = new GridBagConstraints();
		ipInfoLabelConstrains.weightx = 1f;
		ipInfoLabelConstrains.gridwidth = 2;
		ipInfoLabelConstrains.fill = GridBagConstraints.HORIZONTAL;

		this.ipLabel = new Label("Testing", Label.CENTER);
		this.ipLabel.setFont(new Font("Arial", Font.BOLD, 26));
		GridBagConstraints ipConstraints = new GridBagConstraints();
		ipConstraints.weightx = 1f;
		ipConstraints.gridy = 1;
		ipConstraints.gridwidth = 2;
		ipConstraints.insets = new Insets(0, 0, 10, 0);
		ipConstraints.fill = GridBagConstraints.BOTH;

		Font buttonFont = new Font("Arial", Font.PLAIN, 14);
		this.il2Button = new Button("Launch IL-2");
		this.il2Button.setFont(buttonFont);
		this.il2Button.addActionListener(a -> this.actionListener.onLaunchIL2());
		GridBagConstraints il2ButtonConstraints = new GridBagConstraints();
		il2ButtonConstraints.weightx = 0.5f;
		il2ButtonConstraints.weighty = 1f;
		il2ButtonConstraints.gridx = 0;
		il2ButtonConstraints.gridy = 2;
		il2ButtonConstraints.fill = GridBagConstraints.BOTH;

		this.pwcgButton = new Button("Launch PWCG");
		this.pwcgButton.setFont(buttonFont);
		this.pwcgButton.addActionListener(a -> this.actionListener.onLaunchPWCG());
		GridBagConstraints pwcgButtonConstraints = new GridBagConstraints();
		pwcgButtonConstraints.weightx = 0.5f;
		pwcgButtonConstraints.weighty = 1f;
		pwcgButtonConstraints.gridx = 1;
		pwcgButtonConstraints.gridy = 2;
		pwcgButtonConstraints.fill = GridBagConstraints.BOTH;

		mainPanel.add(ipInfoLabel, ipInfoLabelConstrains);
		mainPanel.add(this.ipLabel, ipConstraints);
		mainPanel.add(this.il2Button, il2ButtonConstraints);
		mainPanel.add(this.pwcgButton, pwcgButtonConstraints);

		List<Image> icons = new ArrayList<>();
		try {
			icons.add(ImageIO.read(ClassLoader.getSystemResource("icon24.png")));
			icons.add(ImageIO.read(ClassLoader.getSystemResource("icon16.png")));
		} catch (Exception e) {
			Logger.e(e);
		}

		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("IL-2 Integrator");
		this.setIconImages(icons);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void setupTray() {
		if (!SystemTray.isSupported())
			return;

		SystemTray tray = SystemTray.getSystemTray();

		PopupMenu trayMenu = new PopupMenu();
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(e -> System.exit(0));

		this.ipMenuItem = new MenuItem("");
		this.ipMenuItem.setEnabled(false);

		trayMenu.add(this.ipMenuItem);
		trayMenu.addSeparator();
		trayMenu.add(exitItem);

		Image trayImage;
		try {
			trayImage = ImageIO.read(ClassLoader.getSystemResource("tray.png"));
			TrayIcon trayIcon = new TrayIcon(trayImage, "IL-2 Integrator", trayMenu);
			trayIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						setVisible(true);
						setState(Frame.NORMAL);
						tray.remove(trayIcon);
					}
				}
			});

			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowIconified(WindowEvent e) {
					try {
						tray.add(trayIcon);
						setVisible(false);
					} catch (AWTException ex) {
						Logger.e(ex);
					}
				}
			});
		} catch (Exception e) {
			Logger.e(e);
		}
	}

	public void setIp(String ip) {
		this.ipLabel.setText(ip);
		if (this.ipMenuItem != null)
			this.ipMenuItem.setLabel(ip);
	}

	public void setLaunchIL2Enabled(boolean enabled) {
		this.il2Button.setEnabled(enabled);
	}

	public void setLaunchPWCGEnabled(boolean enabled) {
		this.pwcgButton.setEnabled(enabled);
	}

	public void showErrorMessage(String error) {
		JOptionPane.showMessageDialog(this.rootPane, error, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
