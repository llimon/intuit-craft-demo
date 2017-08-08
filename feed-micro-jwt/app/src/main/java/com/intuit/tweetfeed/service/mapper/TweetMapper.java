package com.intuit.tweetfeed.service.mapper;

import com.intuit.tweetfeed.domain.*;
import com.intuit.tweetfeed.service.dto.TweetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Tweet and its DTO TweetDTO.
 */
@Mapper(componentModel = "spring", uses = {TweetAuthorMapper.class, })
public interface TweetMapper extends EntityMapper <TweetDTO, Tweet> {

    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.screenname", target = "createdByScreenname")
    TweetDTO toDto(Tweet tweet); 

    @Mapping(source = "createdById", target = "createdBy")
    Tweet toEntity(TweetDTO tweetDTO); 
    default Tweet fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tweet tweet = new Tweet();
        tweet.setId(id);
        return tweet;
    }
}
