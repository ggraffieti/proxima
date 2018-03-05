authorized(SENDER, RESCUER_ID, PATIENT_ID) :- request(SENDER, RESCUER_ID, PATIENT_ID), rescuer(RESCUER_ID).
		
response(SENDER, DATA) :- senderRequestData(SENDER, PATIENT_ID), actorOpDone(encryptData(_), DATA), 
						  retract(actorOpDone(encryptData(_), _)).