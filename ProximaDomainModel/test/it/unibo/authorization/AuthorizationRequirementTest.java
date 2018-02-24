package it.unibo.authorization;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import it.unibo.ctxBaseDebug.MainCtxBaseDebug;
import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;

public class AuthorizationRequirementTest {
	private static final String PATIENT_IDENTIFIER = "id_p1";
	private static final String PATIENT_DATA = "john, doe, a_negative";
	private static final String PATIENT2_IDENTIFIER = "id_p2";
	private static final String PATIENT2_DATA = "jane, doe, ab_positive";
	private static final String DOCTOR_IDENTIFIER = "id_d1";
	private static final String DOCTOR2_IDENTIFIER = "id_d2";
	private static final double HIGHER_DISTANCE = 200;
	private static final double LOWER_DISTANCE = 100;
	
	private QActor patient;
	private QActor patient2;
	private QActor doctor;
	private QActor control;
	
	@Before
	public void setUp() throws Exception  {
    	MainCtxBaseDebug.initTheContext();
    	//We create the actors, but we must also be sure that 
    	//each actor has terminated its initialization phase
   		while (control == null || patient == null || patient2 == null || doctor == null) {
   			//wait for creation
  	 		Thread.sleep(100);  
 			control  = QActorUtils.getQActor("qcontrol_ctrl");
 			patient = QActorUtils.getQActor("qpatient_ctrl");
 			patient2 = QActorUtils.getQActor("qpatient2_ctrl");
 			doctor = QActorUtils.getQActor("qdoctor_ctrl");
  		};
  		setData();
  		Thread.sleep(2000);
	}

 	@Test
	public void firstDataTest() {  
 		System.out.println("[TEST] First data test");
		try {
			setDistances(LOWER_DISTANCE, HIGHER_DISTANCE);
			setDoctorIdentifier(DOCTOR_IDENTIFIER);
			raiseButtonClick(doctor);
			assertTrue("First data test", patient != null);
			assertTrue("First data test", patient2 != null);
			assertTrue("First data test", doctor != null);
	 		assertTrue("First data test", control != null);
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
			setDoctorIdentifier(DOCTOR_IDENTIFIER);
			raiseButtonClick(doctor);
			assertTrue("Second data test", patient != null);
			assertTrue("Second data test", patient2 != null);
			assertTrue("Second data test", doctor != null);
	 		assertTrue("Second data test", control != null);
 			assertTrue("Second data test", PATIENT2_DATA.equals(getData()));
	 		Thread.sleep(2000);
 		} catch (final Exception e) {
			fail("actorTest " + e.getMessage());
		}		
	}
 	
 	@Test
 	public void firstAuthorizationTest() {
 		System.out.println("[TEST] First authorization test");
		try {
			setDistances(LOWER_DISTANCE, HIGHER_DISTANCE);
			setDoctorIdentifier(DOCTOR_IDENTIFIER);
			raiseButtonClick(doctor);
			assertTrue("Second authorization test", patient != null);
			assertTrue("Second authorization test", patient2 != null);
			assertTrue("Second authorization test", doctor != null);
	 		assertTrue("Second authorization test", control != null);
 			assertTrue("Second authorization test", PATIENT_DATA.equals(getData()));
	 		Thread.sleep(2000);
 		} catch (final Exception e) {
			fail("actorTest " + e.getMessage());
		}
 	}
 	
 	@Test
 	public void secondAuthorizationTest() {
 		System.out.println("[TEST] Second authorization test");
		try {
			setDistances(LOWER_DISTANCE, HIGHER_DISTANCE);
			setDoctorIdentifier(DOCTOR2_IDENTIFIER);
			raiseButtonClick(doctor);
			assertTrue("Second authorization test", patient != null);
			assertTrue("Second authorization test", patient2 != null);
			assertTrue("Second authorization test", doctor != null);
	 		assertTrue("Second authorization test", control != null);
 			assertTrue("Second authorization test", isDoctorUnauthorized());
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
		
		doctor.solveGoal("retractall(distance(_,_))");
		doctor.solveGoal("retractall(dataResponse(_, _, _, _, _))");
		doctor.solveGoal("retractall(unauthorized)");
		
		control.solveGoal("retractall(data(_,_,_,_))");
		control.addRule("data(" + PATIENT_IDENTIFIER + ", " + PATIENT_DATA + ")");
		control.addRule("data(" + PATIENT2_IDENTIFIER + ", " + PATIENT2_DATA + ")");
		control.solveGoal("retractall(doctor(_))");
		control.addRule("doctor(" + DOCTOR_IDENTIFIER + ")");
	}
 	
 	private void setDistances(double patientDistance, double patient2Distance) throws InterruptedException {
 		doctor.addRule("distance(qpatient, " + patientDistance + ")");
		doctor.addRule("distance(qpatient2, " + patient2Distance + ")");
 	}
 	
 	private void setDoctorIdentifier(String id) {
 		doctor.removeRule("identifier(_)");
		doctor.addRule("identifier(" + id + ")");
 	}
 	
 	private String getData() throws NoSolutionException {
 		final SolveInfo solveInfo = doctor.solveGoal("dataResponse(_, _, NAME, SURNAME, BLOOD_GROUP)");
  		return solveInfo.getVarValue("NAME") + ", " + solveInfo.getVarValue("SURNAME") + ", " + 
 				solveInfo.getVarValue("BLOOD_GROUP");
 	}
 	
 	private boolean isDoctorUnauthorized() throws NoSolutionException {
 		final SolveInfo solveInfo = doctor.solveGoal("unauthorized");
 		return solveInfo.isSuccess();
 	}
 	
 	private void raiseButtonClick(QActor actor) throws InterruptedException {
		QActorUtils.emitEventAfterTime(actor, "emitter", "local_start", "start", 100);
  		Thread.sleep(500); 	//give time to handle the event		 		
  	}
}
