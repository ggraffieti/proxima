findNearest(ID, D) :- numDistances(1), distance(ID, D), !.
		findNearest(ID, D) :- distance(ID1, D1), distance(ID2, D2), eval(lt, D1, D2), retract(distance(ID2, D2)),
							  numDistances(X), eval(minus, X, 1, Y), replaceRule(numDistances(X), numDistances(Y)),
							  findNearest(ID, D), !.
		findNearest(ID, D) :- distance(ID1, D1), distance(ID2, D2), eval(gt, D1, D2), retract(distance(ID1, D1)),
							  numDistances(X), eval(minus, X, 1, Y), replaceRule(numDistances(X), numDistances(Y)),
							  findNearest(ID, D), !.
		findNearest(ID, D) :- distance(ID1, D1), retract(distance(ID1,D1)), numDistances(X), eval(minus, X, 1, Y), 
							  replaceRule(numDistances(X), numDistances(Y)), findNearest(ID, D).