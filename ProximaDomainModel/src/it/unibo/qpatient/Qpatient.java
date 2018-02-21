/* Generated by AN DISI Unibo */ 
/*
This code is generated only ONCE
*/
package it.unibo.qpatient;
import it.unibo.identification.Identifier;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.QActorContext;

public class Qpatient extends AbstractQpatient { 
	private Identifier identifier;
	
	public Qpatient(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
		super(actorId, myCtx, outEnvView);
	}
	
	public void createIdentifier(final String id) {
		identifier = new Identifier(id);
	}
	
	public String getIdentifier() {
		return identifier.getIdentifier();
	}
}
