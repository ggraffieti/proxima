identifiers(DOCTOR_ID, PATIENT_ID) :- identifier(DOCTOR_ID), patientid(PATIENT_ID), retract(identifier(DOCTOR_ID)),
									  retract(patientid(PATIENT_ID)).


numDistances(P, Count) :- findall(1, P, L), length(L, Count).

findNearest(ID, D) :- numDistances(distance(X,Y), 1), distance(ID, D), !.

findNearest(ID, D) :- distance(ID1, D1), distance(ID2, D2), eval(lt, D1, D2), retract(distance(ID2, D2)),
					  findNearest(ID, D), !.
					  
findNearest(ID, D) :- distance(ID1, D1), distance(ID2, D2), eval(gt, D1, D2), retract(distance(ID1, D1)),
					  findNearest(ID, D), !.
					  
findNearest(ID, D) :- distance(ID1, D1), retract(distance(ID1,D1)), findNearest(ID, D).