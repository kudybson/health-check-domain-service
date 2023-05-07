package pl.akh.services;

import pl.akh.model.rq.RateRQ;

public interface RatingService {

    void addRatingToDoctor(RateRQ rateRQ) throws Exception;
}
