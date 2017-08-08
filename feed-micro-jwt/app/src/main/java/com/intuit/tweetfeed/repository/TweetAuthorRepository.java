package com.intuit.tweetfeed.repository;

import com.intuit.tweetfeed.domain.TweetAuthor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TweetAuthor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TweetAuthorRepository extends JpaRepository<TweetAuthor,Long> {
    
}
