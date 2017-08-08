package com.intuit.tweetfeed.repository;

import com.intuit.tweetfeed.domain.Tweet;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tweet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TweetRepository extends JpaRepository<Tweet,Long> {
    
}
