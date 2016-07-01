package hello;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.Future;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import hello.MidiInputService.MidiInputDevice;
import jportmidi.JPortMidiException;

@Lazy
@Component
public class AppPrincipalFrame 
extends JFrame 
implements /*CommandLineRunner,*/ HandleMidiInputListener {

	private JLabel statusLabel = new JLabel("");
	private JComboBox<MidiInputDevice> comboBoxMidiInput = new JComboBox<MidiInputDevice>();

	@Autowired
	private ApplicationContext context;

	@Autowired
	private MidiInputService midiInputService;


	private MidiInputDevice lastMidiInputDevice = MidiInputDevice.DEVICE_FAKE_SELECT;

	public void startUp() {
		init();
		midiInputService.addHandleMidiInputListener(this);
	}

	private void setMidiInputDevice(MidiInputDevice input) {
		if (input == null) {
			return;
		}
		if (!lastMidiInputDevice.equals(input)) {
			try {
				if (midiInputService.isOpenInput()) {
					midiInputService.closeInput();
				}
			} catch (JPortMidiException e) {
				e.printStackTrace();
				showError(e.getMessage());
			}

			if (!input.equals(MidiInputDevice.DEVICE_FAKE_SELECT)) {
				try {
					//TODO: testar como o KORG se comporta ao estar ativado em outro software (ex.: FL Studio)
					midiInputService.openInput(input);
				} catch (JPortMidiException e) {
					e.printStackTrace();
					showError(e.getMessage());
				}
			}
			lastMidiInputDevice = input;
		}
	}

	private void showError(String msg) {
		JDialog dialog = new JOptionPane(msg, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION).createDialog("Erro"); 
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
		dialog.dispose();
	}

//	@Override
//	public void run(String... arg0) throws Exception {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
	public void init() {
	
				try {
					//AppPrincipalFrame frame = new AppPrincipalFrame();
					this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					//frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
					this.setSize(200, 200);

					JPanel panelControles = new JPanel();

					//combobox
					resetAndUpdateInputDevices();
					comboBoxMidiInput.addActionListener (new ActionListener () {
						public void actionPerformed(ActionEvent e) {
							setMidiInputDevice((MidiInputDevice)comboBoxMidiInput.getSelectedItem());
						}
					});
					panelControles.add(comboBoxMidiInput);

					//button
					//TODO: internationalization
					JButton resetAndUpdateButton = new JButton("Atualizar e Resetar");
					resetAndUpdateButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							resetAndUpdateInputDevices();
						}          
					});
					panelControles.add(resetAndUpdateButton);

					//memo
					JTextArea memo = new JTextArea(5, 15);
					memo.setText(String.format("Servidor:\n%s:%s", InetAddress.getLocalHost().getHostName(), System.getProperties().getProperty("server.port")));
					memo.setEditable(false);
					panelControles.add(memo);

					this.add(panelControles);


					JPanel statusPanel = new JPanel();
					statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
					this.add(statusPanel, BorderLayout.SOUTH);
					statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
					statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));

					statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
					statusPanel.add(statusLabel);


					
					this.setVisible(true);




					this.addWindowListener(new java.awt.event.WindowAdapter() {
						public void windowClosing(WindowEvent winEvt) {
							if (midiInputService.isOpenInput()) {
								try {
									midiInputService.closeInput();
								} catch (JPortMidiException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							SpringApplication.exit(context, new ExitCodeGenerator() {
								@Override
								public int getExitCode() {
									return 0;
								}					    	    
							});
							System.exit(0);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//		});
//	}

	private void resetAndUpdateInputDevices() {
		try {
			if (midiInputService.isOpenInput()) {
				midiInputService.closeInput();
			}
			lastMidiInputDevice = MidiInputDevice.DEVICE_FAKE_SELECT;
			comboBoxMidiInput.removeAllItems();
			midiInputService.refreshDeviceLists();
			List<MidiInputDevice> list = midiInputService.listMidiInput();
			list.add(0, MidiInputDevice.DEVICE_FAKE_SELECT);

			for (MidiInputDevice device : list) {
				comboBoxMidiInput.addItem(device);
			}
		} catch (JPortMidiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public void handleMidiInputNote(MidiInput note) {
		statusLabel.setText(String.format("Nota: %s %s", note.getNote(), note.getOn() ? " ON" : "OFF"));
	}
}