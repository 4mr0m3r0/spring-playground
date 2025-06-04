package org.playground.spring.common;

import org.springframework.stereotype.Component;

@Component
public class BaseballCoach  implements Coach {
    public BaseballCoach() {
        System.out.println("Baseball Coach constructor");
    }
    @Override
    public String getDailyWorkout() {
        return "Spend 30 minutes in batting practice";
    }
}
