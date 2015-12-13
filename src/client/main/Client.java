package client.main;

import javax.swing.SwingUtilities;

import client.gui.ClientFrame;

public class Client {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ClientFrame();
			}
		});
	}

}
