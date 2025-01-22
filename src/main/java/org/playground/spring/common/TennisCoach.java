package org.playground.spring.common;

import org.springframework.stereotype.Component;

@Component
public class TennisCoach implements Coach {
    public TennisCoach() {
        System.out.println("Tennis Coach constructor");
    }
    @Override
    public String getDailyWorkout() {
        return "Practice your backhand volley";
    }
}
