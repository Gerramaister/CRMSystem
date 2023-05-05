package com.yrgo.services.diary;

import java.util.*;

import com.yrgo.domain.Action;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiaryManagementServiceMockImpl implements DiaryManagementService {
	
	private Set<Action>allActions= new HashSet<Action>();

	public DiaryManagementServiceMockImpl(){

	}

	@Override
	public void recordAction(Action action) {
		allActions.add(action);

	}

	//Hint: 
	//Create a list<Action>
	//In the for each loop going through the list use this condition: "if(action.getOwningUser().equals(requiredUser) && !action.isComplete())" to add a new action to the list. 
	public List<Action> getAllIncompleteActions(String requiredUser) {
		List<Action> list = new ArrayList<Action>();
		for(Action action: allActions) {
			if (action.getOwningUser().equals(requiredUser) && !action.isComplete()) {
				list.add(action);
			}
		}
		return list;
	}

}
