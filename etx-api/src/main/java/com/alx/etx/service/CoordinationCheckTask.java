package com.alx.etx.service;

import com.alx.etx.data.Coordination;
import com.alx.etx.model.CoordinationEntity;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CoordinationCheckTask {
    @Autowired
    private Logger logger;

    @Autowired
    private CoordinationService coordinationService;

    @Scheduled(fixedRate = 1000)
    private void endCoordinationTask() {
        logger.info("Checking for timed out unfinished coordinations.");
        coordinationService.getNonConsistents()
                .filter(Coordination::isTimedOut)
                .flatMap(coordination -> coordinationService.end(coordination.getId()))
                .subscribe(coordination -> logger.info("Ending timed out coordination " + coordination.getId()));
    }
}
