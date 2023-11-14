package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import model.Analizer;
import view.CodeView;
import view.ConsoleView;
import view.OptionsView;
import view.TablesView;

public class PrincipalView extends JFrame {

	private static final long serialVersionUID = 1L;
	private OptionsView panelOptions;
	private CodeView panelCode;
	private ConsoleView panelConsole;
	private JTextPane textAreaCode;
	private JTextArea textAreaConsole;
	private JLabel lblArchivo;
	private JLabel lblPlay;
	private JLabel lblTablas;
	private ArrayList<String> idList;
	private ArrayList<String> lexemeList;
	private ArrayList<String> tokenList;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrincipalView frame = new PrincipalView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PrincipalView() {
		getContentPane().setBackground(new Color(24, 24, 24));
		setTitle("AGA IDE");
		setIconImage(Toolkit.getDefaultToolkit().getImage(PrincipalView.class.getResource("/img/logo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		JPanel panelContainer = new JPanel();
		panelContainer.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelContainer.setBackground(new Color(24, 24, 24));
		getContentPane().add(panelContainer);
		GridBagLayout gbl_panelContainer = new GridBagLayout();
		gbl_panelContainer.columnWeights = new double[] { 0.0, 99.0 };
		gbl_panelContainer.columnWidths = new int[] { 60, 0 };
		gbl_panelContainer.rowWeights = new double[] { 1.0 };
		gbl_panelContainer.rowHeights = new int[] { 0 };
		panelContainer.setLayout(gbl_panelContainer);

		panelOptions = new OptionsView();
		GridBagConstraints gbc_panelOptions = new GridBagConstraints();
		gbc_panelOptions.fill = GridBagConstraints.BOTH;
		gbc_panelOptions.gridx = 0;
		gbc_panelOptions.gridy = 0;
		gbc_panelOptions.weightx = 0.2;
		panelContainer.add(panelOptions, gbc_panelOptions);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerSize(10);
		splitPane.setUI(new BasicSplitPaneUI() {
			@Override
			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {
					private static final long serialVersionUID = 1L;

					@Override
					public void paint(Graphics g) {
						g.setColor(new Color(24, 24, 24));
						g.fillRect(0, 0, getWidth(), getHeight());
						super.paint(g);
					}

					@Override
					public void setBorder(Border border) {
					}
				};
			}
		});
		splitPane.setForeground(new Color(24, 24, 24));
		splitPane.setBackground(new Color(24, 24, 24));
		splitPane.setBorder(null);
		splitPane.setResizeWeight(0.8);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 1;
		gbc_splitPane.gridy = 0;
		gbc_splitPane.weightx = 0.8;
		panelContainer.add(splitPane, gbc_splitPane);

		idList = new ArrayList<String>();
		lexemeList = new ArrayList<String>();
		tokenList = new ArrayList<String>();

		panelCode = new CodeView();
		panelCode.getTextPane().setText("INICIO{\r\n\tENTERO a1, a2=20, a3;\r\n\tFLOTANTE b21, b22, b23=2.5;\r\n\tFLOTANTE a1;\r\n\tLEER (a1);\r\n\tLEER (b21);\r\n\ta3=SUM(a1,a2);\r\n\ta2=RES(a1,SUM(30,a3));\r\n\tb22=MUL(b21,DIV(b23,5.0));\r\n\tIMPRIMIR (a2);\r\n\tIMPRIMIR (b22);\r\n}FIN");
		splitPane.setTopComponent(panelCode);

		panelConsole = new ConsoleView();
		splitPane.setBottomComponent(panelConsole);

		textAreaCode = panelCode.getTextPane();
		textAreaConsole = panelConsole.getTextArea();

		lblArchivo = panelOptions.getLblArchivo();
		lblArchivo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception look) {
					look.printStackTrace();
				}

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Desktop"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
				fileChooser.setDialogTitle("OPEN A TXT FILE");

				int result = fileChooser.showOpenDialog(null);

				if (result == JFileChooser.APPROVE_OPTION) {
					try {
						File selectedFile = fileChooser.getSelectedFile();
						String content = "";
						Scanner scanner = new Scanner(selectedFile);
						while (scanner.hasNext()) {
							content += scanner.nextLine() + "\r";
						}
						scanner.close();
						textAreaCode.setText(content);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}
		});

		lblPlay = panelOptions.getLblPlay();
		lblPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Analizer analizer = new Analizer(textAreaCode.getText());
				analizer.analizar();
				idList = analizer.getIds();
				lexemeList = analizer.getLexemes();
				tokenList = analizer.getTokens();
				textAreaConsole.setText("Itz AGA_Shell\nCopyrigth (c) 2023.\nAll Rights Reserved.\n");
				if(analizer.isAccepted()) {
					textAreaConsole.append("The Analysis Was Successfully Without Errors.\n");
				}else {
					for (String error : analizer.getErrors()) {					
						textAreaConsole.append(error);
					}
				}
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception look) {
					look.printStackTrace();
				}
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Desktop"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
				fileChooser.setFileFilter(filter);
				fileChooser.setDialogTitle("SAVE A TXT FILE");
				int result = fileChooser.showSaveDialog(null);

				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    String filePath = selectedFile.getAbsolutePath();
			        if (!filePath.endsWith(".txt")) {
			            filePath += ".txt";
			        }
				    if (selectedFile.exists()) {
				        int response = JOptionPane.showConfirmDialog(null, "El archivo ya existe. ¿Desea sobrescribirlo?", "Confirmar sobrescritura", JOptionPane.YES_NO_OPTION);
				        if (response == JOptionPane.YES_OPTION) {
				            try {
								BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
						        for (int i=0; i<tokenList.size();i++) {
				                    writer.write(lexemeList.get(i) + " | " + tokenList.get(i));
				                    writer.newLine();
				                }
					            writer.close();
				                textAreaConsole.append("\n\nFile Was Saved Succesfully.\n");
				            } catch (IOException e2) {
				                e2.printStackTrace();
				                textAreaConsole.append("\n\nError: Unable to save the file.\n");
				            }
				        }
				    } else {
				    	try {
							BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
					        for (String token : lexemeList) {
			                    writer.write(token);
			                }
				            writer.close();
			                textAreaConsole.append("\n\nFile Was Saved Succesfully.\n");
			            } catch (IOException e2) {
			                e2.printStackTrace();
			                textAreaConsole.append("\n\nError: Unable to save the file.\n");
			            }
				    }
				}
			}
		});

		lblTablas = panelOptions.getLblTablas();
		lblTablas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFrame frameTables = new JFrame();
				frameTables.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frameTables.setResizable(false);
				TablesView panelTables = new TablesView(idList, lexemeList, tokenList);
				frameTables.getContentPane().add(panelTables);
				frameTables.setVisible(true);
			}
		});
	}
	
}
