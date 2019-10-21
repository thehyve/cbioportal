package org.cbioportal.service.impl;

import java.util.List;

import org.cbioportal.model.TypeOfCancer;
import org.cbioportal.persistence.CancerTypeRepository;
import org.cbioportal.service.ServerStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServerStatusServiceImpl implements  ServerStatusService {
    
    public static final String MESSAGE_RUNNING = "Server running";
    public static final String MESSAGE_DOWN = "Server down";

    @Autowired
    private CancerTypeRepository cancerTypeRepository;

    @Override
    public String getServerStatus() {
        List<TypeOfCancer> allCancerTypes = cancerTypeRepository.getAllCancerTypes("SUMMARY", null, null, null, null);
        if (allCancerTypes.size() > 0) {
            return MESSAGE_RUNNING;
        }
        return MESSAGE_DOWN;
        
    }

}
