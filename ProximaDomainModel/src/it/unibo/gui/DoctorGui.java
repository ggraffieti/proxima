package it.unibo.gui;

import it.unibo.is.interfaces.IActivityBase;
import it.unibo.is.interfaces.IBasicEnvAwt;
import it.unibo.qactors.akka.QActor;
import it.unibo.system.SituatedPlainObject;

public class DoctorGui extends SituatedPlainObject{
	private IActivityBase cmdHandler;
	private IBasicEnvAwt envAwt;
	
	public DoctorGui(IBasicEnvAwt env, QActor qa) {
		super(env);
		//env is declared of type IBasicUniboEnv in SituatedPlainObject
		//that does not provide any addPanel method. Thus, we memorize it
		envAwt = env;
		init(qa);
	}
	
	protected void init(QActor qa){
		cmdHandler = new CmdHandler(envAwt, qa);
		envAwt.addCmdPanel("commandPanel",
				new String[]{"Start Discovery" }, cmdHandler );
	}

	public void showData(String name, String surname, String bloodGroup) {
		println("Name: " + name + "\nSurname: " + surname + "\nBlood Group: " + bloodGroup);
	}
}
