package com.intuit.tweetfeed.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Tweet entity
 */
@ApiModel(description = "Tweet entity")
@Entity
@Table(name = "tweet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tweet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 140)
    @Column(name = "tweet_text", length = 140, nullable = false)
    private String tweetText;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Size(max = 5)
    @Column(name = "lang", length = 5, nullable = false)
    private String lang;

    @ManyToOne(optional = false)
    @NotNull
    private TweetAuthor createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTweetText() {
        return tweetText;
    }

    public Tweet tweetText(String tweetText) {
        this.tweetText = tweetText;
        return this;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Tweet createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLang() {
        return lang;
    }

    public Tweet lang(String lang) {
        this.lang = lang;
        return this;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public TweetAuthor getCreatedBy() {
        return createdBy;
    }

    public Tweet createdBy(TweetAuthor tweetAuthor) {
        this.createdBy = tweetAuthor;
        return this;
    }

    public void setCreatedBy(TweetAuthor tweetAuthor) {
        this.createdBy = tweetAuthor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tweet tweet = (Tweet) o;
        if (tweet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tweet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tweet{" +
            "id=" + getId() +
            ", tweetText='" + getTweetText() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lang='" + getLang() + "'" +
            "}";
    }
}
