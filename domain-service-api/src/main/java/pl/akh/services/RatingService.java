package pl.akh.services;

import pl.akh.model.rq.RateRQ;

import java.util.UUID;

public interface RatingService {
    void addRatingToDoctor(UUID doctorUUID, RateRQ rateRQ);
}
