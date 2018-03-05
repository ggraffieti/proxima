falsifiedData(SENDER, CHANGED_DATA) :- falsify(true), rescuerSender(SENDER), dataResponse(DATA), 
	text_concat(DATA, a, CHANGED_DATA), retract(rescuerSender(SENDER)), retract(dataResponse(DATA)), 
	retract(falsify(true)).
	
falsifiedData(SENDER, DATA) :- falsify(false), rescuerSender(SENDER), dataResponse(DATA), 
	retract(rescuerSender(SENDER)), retract(dataResponse(DATA)), retract(falsify(false)).