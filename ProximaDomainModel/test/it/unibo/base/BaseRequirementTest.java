package it.unibo.base;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import it.unibo.ctxDebug.MainCtxDebug;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.QActorUtils;
import it.unibo.qactors.akka.QActor;

public class BaseRequirementTest {
	private QActorContext ctx;
	private QActor patient = null;
	private QActor patient2 = null;
	private QActor doctor = null;
	private QActor control = null;
	
	@Before
	public void setUp() throws Exception  {
    	ctx = MainCtxDebug.initTheContext();
    	//We create the actors, but we must also be sure that 
    	//each actor has terminated its initialization phase
   		while( control == null || patient == null || patient2 == null || doctor == null){
   			//wait for creation
  	 		Thread.sleep(100);  
 			control  = QActorUtils.getQActor("qcontrol_ctrl");
 			patient = QActorUtils.getQActor("qpatient_ctrl");
 			patient2 = QActorUtils.getQActor("qpatient2_ctrl");
 			doctor = QActorUtils.getQActor("qdoctor_ctrl");
  		};
	}
	
	@After
	 public void terminate(){
	  	System.out.println("====== terminate  " + ctx );
  	 }

 	@Test
	public void aTest() {  
 		System.out.println("====== aTest ==============="  );
		try {
	 		assertTrue("aTest button", control != null );
 			//initial
	 		Thread.sleep(2000);
 		} catch (Exception e) {
			fail("actorTest " + e.getMessage() );
		}		
	}
}
