package com.ar.pbpoints.service;

import com.ar.pbpoints.domain.Team;
import com.ar.pbpoints.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class BracketServiceIT {

    @Autowired
    private EventCategoryService eventCategoryService;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void testTeams() {
        List<Team> teams = teamRepository.findAll();
        Map<Integer, List<Team>> map = eventCategoryService.armarTeams(teams);
        for (Integer key: map.keySet()) {
            System.out.println("Grupo : " + (key + 1));
            System.out.println("Teams : " + map.get(key));
        }
    }
}
