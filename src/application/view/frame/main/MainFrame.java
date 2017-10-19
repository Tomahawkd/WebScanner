package application.view.frame.main;

import application.view.frame.alert.AlertPanelController;
import application.view.frame.decoder.DecoderPanelController;
import application.view.frame.extension.ExtensionPanelController;
import application.view.frame.intruder.IntruderPanelController;
import application.view.frame.menu.MenuBarController;
import application.view.frame.proxy.ProxyPanelController;
import application.view.frame.repeater.RepeaterPanelController;
import application.view.frame.scanner.ScannerPanelController;
import application.view.frame.spider.SpiderPanelController;
import application.view.frame.target.TargetPanelController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Ghost on 18/07/2017.
 */

class MainFrame {

	private JFrame frmWebScanner;
	private JMenuBar menuBar;

	/**
	 * Initialize the contents of the frame.
	 */

	MainFrame() {

		/*
		 * Frame
		 */

		frmWebScanner = new JFrame();
		frmWebScanner.setTitle("Web Scanner");
		frmWebScanner.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ExitTipFrameController.getInstance();
			}
		});

		frmWebScanner.setBounds(0, 0, 1280, 720);
		frmWebScanner.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		menuBar = MenuBarController.getInstance();
		frmWebScanner.setJMenuBar(menuBar);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(6, 6, 690, 444);
		frmWebScanner.getContentPane().add(tabbedPane);


		/*
		 *	Child Tabs
		 */

		//Target
		JTabbedPane targetPanel = TargetPanelController.getInstance();
		tabbedPane.addTab("Target", null, targetPanel, "Target information");

		//Proxy
		JTabbedPane proxyPanel = ProxyPanelController.getInstance();
		tabbedPane.addTab("Proxy", null, proxyPanel, "Intercepter proxy control");

		//Spider
		JTabbedPane spiderPanel = SpiderPanelController.getInstance();
		tabbedPane.addTab("Spider", null, spiderPanel, "Spider control and options");

		//Scanner
		JTabbedPane scannerPanel = ScannerPanelController.getInstance();
		tabbedPane.addTab("Scanner", null, scannerPanel, "Vulnerability Scanner");

		//Intruder
		JTabbedPane intruderPanel = IntruderPanelController.getInstance();
		tabbedPane.addTab("Intruder", null, intruderPanel, null);

		//Repeater
		JPanel repeaterPanel = RepeaterPanelController.getInstance();
		tabbedPane.addTab("Repeater", null, repeaterPanel, null);

		//Decoder
		JScrollPane decoderPanel = DecoderPanelController.getInstance();
		tabbedPane.addTab("Decoder", null, decoderPanel, null);

		//Extension
		JTabbedPane extensionPanel = ExtensionPanelController.getInstance();
		tabbedPane.addTab("Extension", null, extensionPanel, null);

		//Alerts
		JPanel alertPanel = AlertPanelController.getInstance();
		tabbedPane.addTab("Alert", null, alertPanel, null);

		frmWebScanner.setVisible(true);
	}

}
