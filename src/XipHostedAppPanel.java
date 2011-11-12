/**
 * Copyright (c) 2009 Washington University in St. Louis. All Rights Reserved.
 */

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.nema.dicom.wg23.ArrayOfObjectLocator;
import org.nema.dicom.wg23.ObjectLocator;
import org.nema.dicom.wg23.Uuid;

import edu.wustl.erl.xip.app.GuiPanel;
import edu.wustl.xipApplication.recist.recistGUI.UnderDevelopmentDialog;
import edu.wustl.xipApplication.wg23.OutputAvailableEvent;
import edu.wustl.xipApplication.wg23.OutputAvailableListener;

/**
 * @author Lawrence Tarbox
 *
 */


public class XipHostedAppPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4680059836530067596L;

	final int WIDTH = 350;

	final File ivFile = new File("../resources/App.iv");

	ivCanvas mivCanvas;
    GuiPanel guiPanel = new GuiPanel(); 
    String	outDir;
    String	annotatorID;
    
    public XipHostedAppPanel() { 
    	setLayout(null);
    	
		mivCanvas = new ivCanvas();
		add(mivCanvas);
		add(guiPanel);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		mivCanvas.setBounds(0, 0, screenSize.width - WIDTH, screenSize.height);
		guiPanel.setBounds(screenSize.width - WIDTH, 0, WIDTH, screenSize.height);
		
		mivCanvas.initialize();
		loadLibrary();		
	    runSceneGraph(ivFile);
	    guiPanel.getDoneBtn().addActionListener(this);
	    
	    guiPanel.getResetBtn().addActionListener(this);
	    guiPanel.getModifyBtn().addActionListener(this);
	    guiPanel.getClearBtn().addActionListener(this);

	    guiPanel.getDoneBtn().setEnabled(true);
	    guiPanel.getResetBtn().setEnabled(true);
    }
    
	public static void main(String[] args){
		JFrame frame = new JFrame();
		XipHostedAppPanel panel = new XipHostedAppPanel();
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);	
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		frame.setBounds(0, 0, screenSize.width, screenSize.height);

	}
   
	public void loadLibrary() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("../resources/ivJava.ini"));
			String line = "";
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					int index = line.indexOf("LoadLibrary=");
					if (index >= 0) {
						String Library = line.substring(index + 12);
						Library = Library.replace(';', ',');
						System.out.println("Loading XIP Libraries(tm) : ");
						System.out.println(Library);
						if (!mivCanvas.loadLibraries(Library))
							System.out.println("Not all XIP Libraries(tm could be loaded");
						break;
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	public void runSceneGraph(File ivFile){	   
	    String filePth;
	    if(ivFile.exists()) {
	    	filePth = ivFile.getAbsolutePath();
	    } else {
	    	return;
	    }	              	          
		if (null != mivCanvas && filePth.length() != 0) {
			try {
				mivCanvas.loadGraphOpenGL(filePth);
				mivCanvas.repaint();	    
			} catch (Exception e) {
				  e.printStackTrace();
			}
		}
	}
	
	public ivCanvas getIvCanvas(){
		return mivCanvas;
	}
	   
	public GuiPanel getGuiPanel(){
		return guiPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == guiPanel.getResetBtn()){
			getIvCanvas().set("MprExaminer0.viewOrientation", "");
			getIvCanvas().set("MprExaminer0.viewAll", "");
			getIvCanvas().set("MprExaminer1.viewOrientation", "");
			getIvCanvas().set("MprExaminer1.viewAll", "");
			getIvCanvas().set("MprExaminer2.viewOrientation", "");
			getIvCanvas().set("MprExaminer2.viewAll", "");
			getIvCanvas().set("VolExaminer.viewOrientation", "");
			getIvCanvas().set("VolExaminer.viewAll", "");
			getIvCanvas().processQueue();
			
			System.out.println("Reset");
		}
		if(e.getSource() == guiPanel.getModifyBtn()){
			System.out.println("Modify");
			new UnderDevelopmentDialog(guiPanel.getModifyBtn().getLocationOnScreen());
		}
		if(e.getSource() == guiPanel.getClearBtn()){
			//getIvCanvas().set("VR_Texture.whichChild", "0");
			//getIvCanvas().processQueue();
			
			System.out.println("Clear");
			new UnderDevelopmentDialog(guiPanel.getModifyBtn().getLocationOnScreen());
		}

		if(e.getSource() == guiPanel.getDoneBtn()){
		}

	}
	
	OutputAvailableListener listener; 
	public void addOutputAvailableListener(OutputAvailableListener l){
		 this.listener = l;
	}
		
	void notifyDataAvailable(List<File> outputFiles){
		OutputAvailableEvent event = new OutputAvailableEvent(outputFiles);
		listener.outputAvailable(event);
	}
	
	void setOutputDir(String outDir)
	{
		this.outDir = outDir;
	}

	ArrayOfObjectLocator createDataAsFile(List<File> serializedFiles) {
		ArrayOfObjectLocator arrayObjLoc = new ArrayOfObjectLocator();
		List<ObjectLocator> listObjectLocs = arrayObjLoc.getObjectLocator();

		for (int i = 0; i < serializedFiles.size(); i++){
			Uuid objDescUUID = new Uuid();
			objDescUUID.setUuid(UUID.randomUUID().toString());
	
			ObjectLocator objLoc = new ObjectLocator();				
			objLoc.setUuid(objDescUUID);				
			try {
				objLoc.setUri(serializedFiles.get(i).toURI().toURL().toExternalForm());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
			listObjectLocs.add(objLoc);
		}
		
		return arrayObjLoc;
	}
}	