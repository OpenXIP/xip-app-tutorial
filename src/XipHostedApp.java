/*
Copyright (c) 2013, Washington University in St.Louis.
All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.nema.dicom.wg23.ArrayOfObjectDescriptor;
import org.nema.dicom.wg23.ArrayOfObjectLocator;
import org.nema.dicom.wg23.ArrayOfUUID;
import org.nema.dicom.wg23.AvailableData;
import org.nema.dicom.wg23.ObjectDescriptor;
import org.nema.dicom.wg23.ObjectLocator;
import org.nema.dicom.wg23.Patient;
import org.nema.dicom.wg23.Series;
import org.nema.dicom.wg23.State;
import org.nema.dicom.wg23.Study;
import org.nema.dicom.wg23.Uuid;

import edu.wustl.xipApplication.application.ApplicationDataManager;
import edu.wustl.xipApplication.application.ApplicationDataManagerFactory;
import edu.wustl.xipApplication.application.ApplicationTerminator;
import edu.wustl.xipApplication.application.WG23Application;
import edu.wustl.xipApplication.applicationGUI.ExceptionDialog;
import edu.wustl.xipApplication.wg23.ApplicationImpl;
import edu.wustl.xipApplication.wg23.OutputAvailableEvent;
import edu.wustl.xipApplication.wg23.OutputAvailableListener;
import edu.wustl.xipApplication.wg23.WG23DataModel;
import edu.wustl.xipApplication.wg23.WG23DataModelImpl;
import edu.wustl.xipApplication.wg23.WG23Listener;

/**
 * @author Lawrence Tarbox
 */
public class XipHostedApp extends WG23Application implements WG23Listener, OutputAvailableListener{
	XipHostedAppFrame frame = null;			
	
	State appCurrentState;
	ApplicationDataManager dataMgr;
	

	public XipHostedApp(URL hostURL, URL appURL) {
		super(hostURL, appURL);				
		final XipHostedApp mainApp = this;

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
			    public void run() {
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						XipHostedAppFrame frame = new XipHostedAppFrame(mainApp);
				    	mainApp.setFrame(frame);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedLookAndFeelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		/*Notify Host application was launched*/							
		dataMgr = ApplicationDataManagerFactory.getInstance();
		ApplicationImpl appImpl = new ApplicationImpl();
		appImpl.addWG23Listener(this);
		setAndDeployApplicationService(appImpl);		
		getClientToHost().notifyStateChanged(State.IDLE);		
		
	}
	
	public void setFrame(XipHostedAppFrame frameIn)
	{
		frame = frameIn;
	}
		
	public static void main(String[] args) {
		try {
			/*args = new String[4];
			args[0] = "--hostURL";
			args[1] = "http://localhost:8090/HostInterface";
			args[2] = "--applicationURL";
			args[3] = "http://localhost:8060/ApplicationInterface";*/	
			System.out.println("Number of parameters: " + args.length);
			for (int i = 0; i < args.length; i++){
				System.out.println(i + ". " + args[i]);
			}
			URL hostURL = null;
			URL applicationURL = null;
			for (int i = 0; i < args.length; i++){
				if (args[i].equalsIgnoreCase("--hostURL")){
					hostURL = new URL(args[i + 1]);
				}else if(args[i].equalsIgnoreCase("--applicationURL")){
					applicationURL = new URL(args[i + 1]);
				}					
			}									
			new XipHostedApp(hostURL, applicationURL);										
		} catch (MalformedURLException e) {			
			e.printStackTrace();
		} catch (NullPointerException e){
			new ExceptionDialog("List of parameters is not valid!", 
					"Ensure: --hostURL url1 --applicationURL url2",
					"Launch Application Dialog");
			System.exit(0);
		}
	}
	
	public String getSceneGraphInput(List<ObjectLocator> objLocs){
		String input = new String();
		int size = objLocs.size();
		for (int i = 0; i < size; i++){
			if(i == 0){
				String filePath;				
				filePath = new File(objLocs.get(i).getUri()).getPath();
				// input = input + "\"" + nols.get(i).getURI() + "\"" + ", ";					
				filePath = filePath.substring(6 , filePath.length());
				input = "[" + "\"" + filePath + "\"" + ", ";								
			} else if(i < size -1){
				String filePath = new File(objLocs.get(i).getUri()).getPath();
				//input = input + "\"" + nols.get(i).getURI() + "\"" + ", ";
				filePath = filePath.substring(6 , filePath.length());
				input = input + "\"" + filePath + "\"" + ", ";
			}else if(i == size -1){
				String filePath = new File(objLocs.get(i).getUri()).getPath();
				//input = input + "\"" + nols.get(i).getURI() + "\"" + ", ";
				filePath = filePath.substring(6 , filePath.length());
				input = input + "\"" + filePath + "\"" + "]";
			}				
		}
		return input;
	}
	
	@Override
	public boolean bringToFront() {
		// Schedule a job for the event-dispatching thread:
		// bringing to front.
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
				frame.setAlwaysOnTop(true);
				frame.setAlwaysOnTop(false);
		    }
		});
		return true;
		//return false;
	}

	@Override
	public void notifyDataAvailable(AvailableData availableData,
			boolean lastData) {
		ArrayOfUUID arrayUUIDs = new ArrayOfUUID();

		//Extract UUIDs for all dicom objects, only support the first patient/study/series
		List<Patient> patients = availableData.getPatients().getPatient();	
		if (patients.size() <= 0)
			return;
		
		int iPatient = 0;
		Patient patient = patients.get(iPatient);	

		List<Study> studies = patient.getStudies().getStudy();		
		if (studies.size() <= 0)
			return;
		
		int iStudy = 0;
		Study study = studies.get(iStudy);				
		List<Series> listOfSeries = study.getSeries().getSeries();
		
		if (listOfSeries.size() <= 0)
			return;
		
		int iSeries = 0;
		Series series = listOfSeries.get(iSeries);		
		
		List<Uuid> listUUIDs = arrayUUIDs.getUuid();
		ArrayOfObjectDescriptor descriptors = series.getObjectDescriptors();
		List<ObjectDescriptor> listDescriptors = descriptors.getObjectDescriptor();
		for(int m = 0;  m < listDescriptors.size(); m++){
			ObjectDescriptor desc = listDescriptors.get(m);
			listUUIDs.add(desc.getUuid());
		}
		
		ArrayOfObjectLocator objLocs = getClientToHost().getDataAsFile(arrayUUIDs, true);
		List<ObjectLocator> listObjLocs = objLocs.getObjectLocator();

		// Schedule a job for the event-dispatching thread:
		// loading inputs.
		final String inputs = getSceneGraphInput(listObjLocs);
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	frame.setSceneGraphInputs(inputs);
		    }
		});
		
	}

	@Override
	public boolean setState(State newState) {
		if(State.valueOf(newState.toString()).equals(State.CANCELED)){
			getClientToHost().notifyStateChanged(State.CANCELED);
			//getClientToHost().notifyStateChanged(State.IDLE);
		}else if(State.valueOf(newState.toString()).equals(State.EXIT)){
			getClientToHost().notifyStateChanged(State.EXIT);						
			//terminating endpoint and existing system is accomplished through ApplicationTerminator
			//and ApplicationScheduler. ApplicationSechduler is present to alow termination delay if needed (posible future use)
			ApplicationTerminator terminator = new ApplicationTerminator(getEndPoint());
			Thread t = new Thread(terminator);
			t.start();	
		}else{
			getClientToHost().notifyStateChanged(newState);
		}
		return true;
	}
	
	@Override
	public State getState() {
		return appCurrentState;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void outputAvailable(OutputAvailableEvent e) {
		List<File> output = (List<File>)e.getSource();
		WG23DataModel wg23DM = new WG23DataModelImpl(output);		
		dataMgr.setOutputData(wg23DM);
		AvailableData availableData = wg23DM.getAvailableData();		
		getClientToHost().notifyDataAvailable(availableData, true);	
	}
}
