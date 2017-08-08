package com.intuit.tweetfeed.service.mapper;

import com.intuit.tweetfeed.domain.*;
import com.intuit.tweetfeed.service.dto.TweetAuthorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TweetAuthor and its DTO TweetAuthorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TweetAuthorMapper extends EntityMapper <TweetAuthorDTO, TweetAuthor> {
    
    @Mapping(target = "tweets", ignore = true)
    TweetAuthor toEntity(TweetAuthorDTO tweetAuthorDTO); 
    default TweetAuthor fromId(Long id) {
        if (id == null) {
            return null;
        }
        TweetAuthor tweetAuthor = new TweetAuthor();
        tweetAuthor.setId(id);
        return tweetAuthor;
    }
}
