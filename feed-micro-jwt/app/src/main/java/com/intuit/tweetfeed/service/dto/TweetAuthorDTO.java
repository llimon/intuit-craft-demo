package com.intuit.tweetfeed.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TweetAuthor entity.
 */
public class TweetAuthorDTO implements Serializable {

    private Long id;

    @NotNull
    private String screenname;

    @NotNull
    private String name;

    @NotNull
    private ZonedDateTime createdAt;

    @Size(max = 1024)
    private String profileImageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScreenname() {
        return screenname;
    }

    public void setScreenname(String screenname) {
        this.screenname = screenname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TweetAuthorDTO tweetAuthorDTO = (TweetAuthorDTO) o;
        if(tweetAuthorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tweetAuthorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TweetAuthorDTO{" +
            "id=" + getId() +
            ", screenname='" + getScreenname() + "'" +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            "}";
    }
}
