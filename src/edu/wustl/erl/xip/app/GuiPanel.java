/**
 * Copyright (c) 2009 Washington University in St. Louis. All Rights Reserved.
 */

package edu.wustl.erl.xip.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * @author Lawrence Tarbox 
 */
public class GuiPanel extends JPanel  implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	JLabel jLabel0 = null;	
	JLabel jLabel01 = null;

	JButton jBtnReset = null;
	JButton jBtnModify = null;
	JButton jBtnClear = null;
	JButton jBtnDone = null;	
	
	public GuiPanel(){
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		Color xipColor = new Color(51, 51, 102);
		Color labelColor = new Color(0, 153, 153);
		
		setLayout(null);		
				
		JPanel jTitle = new JPanel();
		jTitle.setBackground(xipColor);
		
		jTitle.setBounds(new Rectangle(0, 0, 350, 200));
		jTitle.setLayout(null);
		
		jLabel0 = new JLabel();
		jLabel0.setBounds(new Rectangle(104, 13, 100, 50));
		jLabel0.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 30));
		jLabel0.setText("XIP");
		jLabel0.setBackground(xipColor);
		jLabel0.setForeground(Color.WHITE);
		jTitle.add(jLabel0, null);
		
		jLabel01 = new JLabel();
		jLabel01.setBounds(new Rectangle(30, 53, 300, 50));
		jLabel01.setFont(new Font("Arial", Font.BOLD, 20));
		jLabel01.setText("RSNA 2011 Tutorial");
		jLabel01.setBackground(xipColor);
		jLabel01.setForeground(labelColor);
		jTitle.add(jLabel01, null);	
		
		add(jTitle);
		
		TitledBorder border;
    	
		border = BorderFactory.createTitledBorder("Tool");
		border.setTitleColor(Color.WHITE);
		
		JPanel jTool = new JPanel();
		jTool.setBorder(border);
		jTool.setBounds(new Rectangle(0, 620, 330, 130));
		jTool.setLayout(null);
		jTool.setBackground(xipColor);
   	
    	jBtnReset = new JButton();
    	jBtnReset.setBounds(new Rectangle(7, 25, 100, 30));
    	jBtnReset.setText("Reset");
    	jBtnReset.setBackground(xipColor);
    	jBtnReset.setForeground(Color.BLACK);
		jBtnReset.setEnabled(false);
		jTool.add(jBtnReset, null);
		
		jBtnModify = new JButton();
		jBtnModify.setBounds(new Rectangle(117, 25, 100, 30));
		jBtnModify.setText("Modify");
		jBtnModify.setBackground(xipColor);
		jBtnModify.setForeground(Color.WHITE);
		jBtnModify.setEnabled(false);
		jTool.add(jBtnModify, null);
		
		jBtnClear = new JButton();
		jBtnClear.setBounds(new Rectangle(224, 25, 100, 30));
		jBtnClear.setText("Clear");
		jBtnClear.setBackground(xipColor);
		jBtnClear.setForeground(Color.WHITE);
		jBtnClear.setEnabled(false);
		jTool.add(jBtnClear, null);
		
		add(jTool);
		
		jBtnDone = new JButton();
		jBtnDone.setBounds(new Rectangle(224, 863, 95, 30));
		jBtnDone.setText("Done");
		jBtnDone.setBackground(xipColor);
		jBtnDone.setForeground(Color.WHITE);
		jBtnDone.setEnabled(false);
		add(jBtnDone, null);
		
		setBackground(xipColor);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//processed by the parent panel		
	}
	
	int adjustForResolution(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int height = (int)screenSize.getHeight();
		int preferredHeight = 200;
		if (height < 768 && height >= 600 ){
			preferredHeight = 100;
		}else if(height < 1024 && height >= 768 ){
			preferredHeight = 100;
		}else if (height >= 1024 && height < 1200){
			preferredHeight = 200;
		}else if(height > 1200 && height <= 1440){
			preferredHeight = 200;
		}
		return preferredHeight;		
	}

	public JButton getResetBtn(){
		return jBtnReset;
	}
	public JButton getModifyBtn(){
		return jBtnModify;
	}
	public JButton getClearBtn(){
		return jBtnClear;
	}
	public JButton getDoneBtn(){
		return jBtnDone;
	}
}
