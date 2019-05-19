package it.unibo.ai.didattica.competition.tablut.teampallo.domain;

import java.util.Arrays;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.exceptions.ActionException;
import it.unibo.ai.didattica.competition.tablut.exceptions.BoardException;
import it.unibo.ai.didattica.competition.tablut.exceptions.CitadelException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ClimbingCitadelException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ClimbingException;
import it.unibo.ai.didattica.competition.tablut.exceptions.DiagonalException;
import it.unibo.ai.didattica.competition.tablut.exceptions.OccupitedException;
import it.unibo.ai.didattica.competition.tablut.exceptions.PawnException;
import it.unibo.ai.didattica.competition.tablut.exceptions.StopException;
import it.unibo.ai.didattica.competition.tablut.exceptions.ThroneException;

public class MyGameModernTablutRules implements MyRules {

	private static MyGameModernTablutRules instance = null;
	private List<String> citadels;

	private MyGameModernTablutRules() {
		this.citadels = Arrays.asList("a4", "a5", "a6", "b5", "d1", "e1", "f1", "e2", "i4", "i5", "i6", "h5", "d9",
				"e9", "f9", "e8");
	}

	public static MyGameModernTablutRules getInstance() {
		if (MyGameModernTablutRules.instance == null)
			MyGameModernTablutRules.instance = new MyGameModernTablutRules();
		return instance;
	}

	@Override
	public State movePawn(State state, Action a) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public State checkMove(State state, Action a)
			throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException,
			ThroneException, OccupitedException, CitadelException, ClimbingCitadelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Action> getNextMovesFromState(State state) {
		// TODO Auto-generated method stub
		return null;
	}

}
