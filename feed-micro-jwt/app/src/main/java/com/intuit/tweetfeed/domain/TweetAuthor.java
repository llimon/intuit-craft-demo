package com.intuit.tweetfeed.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * TweetAuthor entity
 */
@ApiModel(description = "TweetAuthor entity")
@Entity
@Table(name = "tweet_author")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TweetAuthor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "screenname", nullable = false)
    private String screenname;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Size(max = 1024)
    @Column(name = "profile_image_url", length = 1024)
    private String profileImageUrl;

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tweet> tweets = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScreenname() {
        return screenname;
    }

    public TweetAuthor screenname(String screenname) {
        this.screenname = screenname;
        return this;
    }

    public void setScreenname(String screenname) {
        this.screenname = screenname;
    }

    public String getName() {
        return name;
    }

    public TweetAuthor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public TweetAuthor createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public TweetAuthor profileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return this;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Set<Tweet> getTweets() {
        return tweets;
    }

    public TweetAuthor tweets(Set<Tweet> tweets) {
        this.tweets = tweets;
        return this;
    }

    public TweetAuthor addTweet(Tweet tweet) {
        this.tweets.add(tweet);
        tweet.setCreatedBy(this);
        return this;
    }

    public TweetAuthor removeTweet(Tweet tweet) {
        this.tweets.remove(tweet);
        tweet.setCreatedBy(null);
        return this;
    }

    public void setTweets(Set<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TweetAuthor tweetAuthor = (TweetAuthor) o;
        if (tweetAuthor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tweetAuthor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TweetAuthor{" +
            "id=" + getId() +
            ", screenname='" + getScreenname() + "'" +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            "}";
    }
}
