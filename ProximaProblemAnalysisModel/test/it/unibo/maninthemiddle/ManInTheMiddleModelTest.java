package it.unibo.maninthemiddle;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import it.unibo.ctxMITMDebug.MainCtxMITMDebug;
import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;

public class ManInTheMiddleModelTest {
	private static final String PATIENT_IDENTIFIER = "id_p1";
	private static final String PATIENT_DATA = "john, doe, a_negative";
	private static final String PATIENT2_IDENTIFIER = "id_p2";
	private static final String PATIENT2_DATA = "jane, doe, ab_positive";
	private static final String RESCUER_IDENTIFIER = "id_d1";
	
	private QActor patient;
	private QActor patient2;
	private QActor rescuer;
	private QActor manInTheMiddle;
	private QActor dataControl;
	private QActor data;
	
	@Before
	public void setUp() throws Exception  {
    	MainCtxMITMDebug.initTheContext();
    	//We create the actors, but we must also be sure that 
    	//each actor has terminated its initialization phase
   		while (data == null || dataControl == null || manInTheMiddle == null || patient == null || 
   				patient2 == null || rescuer == null) {
   			//wait for creation
  	 		Thread.sleep(100);  
  	 		data = QActorUtils.getQActor("qdata_ctrl");
 			dataControl  = QActorUtils.getQActor("qdatacontrol_ctrl");
 			manInTheMiddle = QActorUtils.getQActor("qmaninthemiddle_ctrl");
 			patient = QActorUtils.getQActor("qpatient_ctrl");
 			patient2 = QActorUtils.getQActor("qpatient2_ctrl");
 			rescuer = QActorUtils.getQActor("qrescuer_ctrl");
  		};
  		setData();
  		Thread.sleep(2000);
	}

 	@Test
	public void firstDataTest() {  
 		System.out.println("[TEST] First data test");
		try {
			setConnection(true);
			setDataFalsification(false);
			setNearestPatient(patient);
			setRescuerIdentifier(RESCUER_IDENTIFIER);
			raiseButtonClick(rescuer);
			assertTrue("First data test", patient != null);
			assertTrue("First data test", patient2 != null);
			assertTrue("First data test", rescuer != null);
	 		assertTrue("First data test", dataControl != null);
	 		assertTrue("First data test", data != null);
 			assertTrue("First data test", PATIENT_DATA.equals(getData()));
	 		Thread.sleep(2000);
 		} catch (final Exception e) {
			fail("actorTest " + e.getMessage());
		}		
	}
 	
 	@Test
	public void secondDataTest() {  
 		System.out.println("[TEST] Second data test");
		try {
			setConnection(true);
			setDataFalsification(false);
			setNearestPatient(patient2);
			setRescuerIdentifier(RESCUER_IDENTIFIER);
			raiseButtonClick(rescuer);
			assertTrue("Second data test", patient != null);
			assertTrue("Second data test", patient2 != null);
			assertTrue("Second data test", rescuer != null);
	 		assertTrue("Second data test", dataControl != null);
	 		assertTrue("Second data test", data != null);
 			assertTrue("Second data test", PATIENT2_DATA.equals(getData()));
	 		Thread.sleep(2000);
 		} catch (final Exception e) {
			fail("actorTest " + e.getMessage());
		}		
	}
 	
 	@Test
	public void firstConnectionTest() {  
 		System.out.println("[TEST] First connection test");
		try {
			setConnection(true);
			setDataFalsification(false);
			setNearestPatient(patient);
			setRescuerIdentifier(RESCUER_IDENTIFIER);
			raiseButtonClick(rescuer);
			assertTrue("First connection test", patient != null);
			assertTrue("First connection test", patient2 != null);
			assertTrue("First connection test", rescuer != null);
	 		assertTrue("First connection test", dataControl != null);
	 		assertTrue("First connection test", data != null);
 			assertTrue("First connection test", PATIENT_DATA.equals(getData()));
	 		Thread.sleep(2000);
 		} catch (final Exception e) {
			fail("actorTest " + e.getMessage());
		}		
	}
 	
 	@Rule
 	public final ExpectedException exception = ExpectedException.none();
 	
 	@Test
	public void secondConnectionTest() throws NoSolutionException {  
 		System.out.println("[TEST] Second connection test");
		try {
			setConnection(false);
			setDataFalsification(false);
			setNearestPatient(patient2);
			setRescuerIdentifier(RESCUER_IDENTIFIER);
			raiseButtonClick(rescuer);
			assertTrue("Second connection test", patient != null);
			assertTrue("Second connection test", patient2 != null);
			assertTrue("Second connection test", rescuer != null);
	 		assertTrue("Second connection test", dataControl != null);
	 		assertTrue("Second connection test", data != null);
	 		exception.expect(NoSolutionException.class);
	 		getData();
	 		Thread.sleep(2000);
 		} catch (final InterruptedException e) {
			fail("actorTest " + e.getMessage());
		}		
	}
 	
 	private void setData() throws InterruptedException {
		patient.removeRule("identifier(_)");
		patient.addRule("identifier(" + PATIENT_IDENTIFIER + ")");
		patient2.removeRule("identifier(_)");
		patient2.addRule("identifier(" + PATIENT2_IDENTIFIER + ")");
		
		rescuer.solveGoal("retractall(connection(_))");
		rescuer.solveGoal("retractall(dataResponse(_, _, _, _, _))");
		rescuer.solveGoal("retractall(unauthorized)");
		
		manInTheMiddle.solveGoal("retractall(falsifyData(_))");
		
		data.solveGoal("retractall(data(_,_,_,_))");
		data.addRule("data(" + PATIENT_IDENTIFIER + ", " + PATIENT_DATA + ")");
		data.addRule("data(" + PATIENT2_IDENTIFIER + ", " + PATIENT2_DATA + ")");
		
		dataControl.solveGoal("retractall(rescuer(_))");
		dataControl.addRule("rescuer(" + RESCUER_IDENTIFIER + ")");
	}
 	
 	private void setConnection(boolean connectionState) {
 		rescuer.addRule("connection(" + connectionState + ")");
 	}
 	
 	private void setDataFalsification(boolean falsification) {
 		manInTheMiddle.addRule("falsifyData(" + falsification + ")");
 	}
 	
 	private void setNearestPatient(QActor nearestPatient) throws InterruptedException {
 		rescuer.solveGoal("retractall(nearest(_))");
 		if (nearestPatient.equals(patient)) {
 			rescuer.addRule("nearest(qpatient)");
 		} else {
 			rescuer.addRule("nearest(qpatient2)");
 		}
 	}
 	
 	private void setRescuerIdentifier(String id) {
 		rescuer.removeRule("identifier(_)");
		rescuer.addRule("identifier(" + id + ")");
 	}
 	
 	private String getData() throws NoSolutionException {
 		final SolveInfo solveInfo = rescuer.solveGoal("dataResponse(_, NAME, SURNAME, BLOOD_GROUP)");
  		return solveInfo.getVarValue("NAME") + ", " + solveInfo.getVarValue("SURNAME") + ", " + 
 				solveInfo.getVarValue("BLOOD_GROUP");
 	}
 	
 	private void raiseButtonClick(QActor actor) throws InterruptedException {
		QActorUtils.emitEventAfterTime(actor, "emitter", "local_start", "start", 100);
  		Thread.sleep(500); 	//give time to handle the event		 		
  	}
}
