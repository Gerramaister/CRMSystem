package com.yrgo.services.calls;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import com.yrgo.services.diary.DiaryManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class CallHandlingServiceImpl implements CallHandlingService{

    private CustomerManagementService cMService;
    private DiaryManagementService dMService;

    public CallHandlingServiceImpl(CustomerManagementService cMService, DiaryManagementService dMService){
        this.cMService=cMService;
        this.dMService=dMService;
    }
    @Override
    public void recordCall(String customerId, Call newCall, Collection<Action> actions) throws CustomerNotFoundException {
        cMService.recordCall(customerId, newCall);
        //dMService.recordAction(actions.iterator().next());
        for(Action action: actions){
            dMService.recordAction(action);
        }
    }
}
