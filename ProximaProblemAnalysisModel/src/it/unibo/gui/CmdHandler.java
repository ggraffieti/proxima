package it.unibo.gui;

import it.unibo.is.interfaces.IActivityBase;
import it.unibo.is.interfaces.IBasicEnvAwt;
import it.unibo.qactors.akka.QActor;
import it.unibo.system.SituatedPlainObject;

public class CmdHandler extends SituatedPlainObject implements IActivityBase{
	private final QActor qa;
	
	public CmdHandler(IBasicEnvAwt env, QActor qa) {
		super(env);
		this.qa = qa;
	}
	
	@Override
	public void execAction(String cmd) {
		qa.emit("local_start", "start");
	}
}
