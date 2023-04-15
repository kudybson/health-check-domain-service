package pl.akh.services;

import pl.akh.model.rq.rating.RateRQ;

import java.util.UUID;

public interface RatingService {
    void addRatingToDoctor(UUID doctorUUID, RateRQ rateRQ);
}
