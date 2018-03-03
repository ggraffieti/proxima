package it.unibo.base;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import it.unibo.ctxBaseDebug.MainCtxBaseDebug;
import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;

public class BaseRequirementTest {
	private static final String PATIENT_IDENTIFIER = "id_p1";
	private static final String PATIENT_DATA = "john, doe, a_negative";
	private static final String PATIENT2_IDENTIFIER = "id_p2";
	private static final String PATIENT2_DATA = "jane, doe, ab_positive";
	private static final double HIGHER_DISTANCE = 200;
	private static final double LOWER_DISTANCE = 100;
	
	private QActor patient;
	private QActor patient2;
	private QActor rescuer;
	private QActor datacontrol;
	
	@Before
	public void setUp() throws Exception {
    	MainCtxBaseDebug.initTheContext();
    	//We create the actors, but we must also be sure that 
    	//each actor has terminated its initialization phase
   		while (datacontrol == null || patient == null || patient2 == null || rescuer == null) {
   			//wait for creation
  	 		Thread.sleep(100);  
 			datacontrol  = QActorUtils.getQActor("qdatacontrol_ctrl");
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
			setDistances(LOWER_DISTANCE, HIGHER_DISTANCE);
			raiseButtonClick(rescuer);
			assertTrue("First data test", patient != null);
			assertTrue("First data test", patient2 != null);
			assertTrue("First data test", rescuer != null);
	 		assertTrue("First data test", datacontrol != null);
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
			setDistances(HIGHER_DISTANCE, LOWER_DISTANCE);
			raiseButtonClick(rescuer);
			assertTrue("Second data test", patient != null);
			assertTrue("Second data test", patient2 != null);
			assertTrue("Second data test", rescuer != null);
	 		assertTrue("Second data test", datacontrol != null);
 			assertTrue("Second data test", PATIENT2_DATA.equals(getData()));
	 		Thread.sleep(2000);
 		} catch (final Exception e) {
			fail("actorTest " + e.getMessage());
		}		
	}
 	
 	private void setData() throws InterruptedException {
		patient.removeRule("identifier(_)");
		patient.addRule("identifier(" + PATIENT_IDENTIFIER + ")");
		patient2.removeRule("identifier(_)");
		patient2.addRule("identifier(" + PATIENT2_IDENTIFIER + ")");
		
		rescuer.solveGoal("retractall(distance(_,_))");
		rescuer.solveGoal("retractall(dataResponse(_, _, _, _))");
		
		datacontrol.solveGoal("retractall(data(_,_,_,_))");
		datacontrol.addRule("data(" + PATIENT_IDENTIFIER + ", " + PATIENT_DATA + ")");
		datacontrol.addRule("data(" + PATIENT2_IDENTIFIER + ", " + PATIENT2_DATA + ")");
	}
 	
 	private void setDistances(double patientDistance, double patient2Distance) throws InterruptedException {
 		rescuer.addRule("distance(qpatient, " + patientDistance + ")");
		rescuer.addRule("distance(qpatient2, " + patient2Distance + ")");
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
