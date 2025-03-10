package org.playground.spring.common;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//@Primary
@Lazy
public class TrackCoach implements Coach {
    public TrackCoach() {
        System.out.println("Track Coach constructor");
    }
    @Override
    public String getDailyWorkout() {
        return "Run a hard 5k!";
    }
}
