package com.intuit.tweetfeed.web.rest;

import com.intuit.tweetfeed.TweetfeedApp;

import com.intuit.tweetfeed.domain.Tweet;
import com.intuit.tweetfeed.domain.TweetAuthor;
import com.intuit.tweetfeed.repository.TweetRepository;
import com.intuit.tweetfeed.service.dto.TweetDTO;
import com.intuit.tweetfeed.service.mapper.TweetMapper;
import com.intuit.tweetfeed.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.intuit.tweetfeed.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TweetResource REST controller.
 *
 * @see TweetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TweetfeedApp.class)
public class TweetResourceIntTest {

    private static final String DEFAULT_TWEET_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TWEET_TEXT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LANG = "AAAAA";
    private static final String UPDATED_LANG = "BBBBB";

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TweetMapper tweetMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTweetMockMvc;

    private Tweet tweet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TweetResource tweetResource = new TweetResource(tweetRepository, tweetMapper);
        this.restTweetMockMvc = MockMvcBuilders.standaloneSetup(tweetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tweet createEntity(EntityManager em) {
        Tweet tweet = new Tweet()
            .tweetText(DEFAULT_TWEET_TEXT)
            .createdAt(DEFAULT_CREATED_AT)
            .lang(DEFAULT_LANG);
        // Add required entity
        TweetAuthor createdBy = TweetAuthorResourceIntTest.createEntity(em);
        em.persist(createdBy);
        em.flush();
        tweet.setCreatedBy(createdBy);
        return tweet;
    }

    @Before
    public void initTest() {
        tweet = createEntity(em);
    }

    @Test
    @Transactional
    public void createTweet() throws Exception {
        int databaseSizeBeforeCreate = tweetRepository.findAll().size();

        // Create the Tweet
        TweetDTO tweetDTO = tweetMapper.toDto(tweet);
        restTweetMockMvc.perform(post("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetDTO)))
            .andExpect(status().isCreated());

        // Validate the Tweet in the database
        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeCreate + 1);
        Tweet testTweet = tweetList.get(tweetList.size() - 1);
        assertThat(testTweet.getTweetText()).isEqualTo(DEFAULT_TWEET_TEXT);
        assertThat(testTweet.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTweet.getLang()).isEqualTo(DEFAULT_LANG);
    }

    @Test
    @Transactional
    public void createTweetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tweetRepository.findAll().size();

        // Create the Tweet with an existing ID
        tweet.setId(1L);
        TweetDTO tweetDTO = tweetMapper.toDto(tweet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTweetMockMvc.perform(post("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTweetTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = tweetRepository.findAll().size();
        // set the field null
        tweet.setTweetText(null);

        // Create the Tweet, which fails.
        TweetDTO tweetDTO = tweetMapper.toDto(tweet);

        restTweetMockMvc.perform(post("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetDTO)))
            .andExpect(status().isBadRequest());

        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = tweetRepository.findAll().size();
        // set the field null
        tweet.setCreatedAt(null);

        // Create the Tweet, which fails.
        TweetDTO tweetDTO = tweetMapper.toDto(tweet);

        restTweetMockMvc.perform(post("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetDTO)))
            .andExpect(status().isBadRequest());

        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLangIsRequired() throws Exception {
        int databaseSizeBeforeTest = tweetRepository.findAll().size();
        // set the field null
        tweet.setLang(null);

        // Create the Tweet, which fails.
        TweetDTO tweetDTO = tweetMapper.toDto(tweet);

        restTweetMockMvc.perform(post("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetDTO)))
            .andExpect(status().isBadRequest());

        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTweets() throws Exception {
        // Initialize the database
        tweetRepository.saveAndFlush(tweet);

        // Get all the tweetList
        restTweetMockMvc.perform(get("/api/tweets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tweet.getId().intValue())))
            .andExpect(jsonPath("$.[*].tweetText").value(hasItem(DEFAULT_TWEET_TEXT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())));
    }

    @Test
    @Transactional
    public void getTweet() throws Exception {
        // Initialize the database
        tweetRepository.saveAndFlush(tweet);

        // Get the tweet
        restTweetMockMvc.perform(get("/api/tweets/{id}", tweet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tweet.getId().intValue()))
            .andExpect(jsonPath("$.tweetText").value(DEFAULT_TWEET_TEXT.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTweet() throws Exception {
        // Get the tweet
        restTweetMockMvc.perform(get("/api/tweets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTweet() throws Exception {
        // Initialize the database
        tweetRepository.saveAndFlush(tweet);
        int databaseSizeBeforeUpdate = tweetRepository.findAll().size();

        // Update the tweet
        Tweet updatedTweet = tweetRepository.findOne(tweet.getId());
        updatedTweet
            .tweetText(UPDATED_TWEET_TEXT)
            .createdAt(UPDATED_CREATED_AT)
            .lang(UPDATED_LANG);
        TweetDTO tweetDTO = tweetMapper.toDto(updatedTweet);

        restTweetMockMvc.perform(put("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetDTO)))
            .andExpect(status().isOk());

        // Validate the Tweet in the database
        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeUpdate);
        Tweet testTweet = tweetList.get(tweetList.size() - 1);
        assertThat(testTweet.getTweetText()).isEqualTo(UPDATED_TWEET_TEXT);
        assertThat(testTweet.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTweet.getLang()).isEqualTo(UPDATED_LANG);
    }

    @Test
    @Transactional
    public void updateNonExistingTweet() throws Exception {
        int databaseSizeBeforeUpdate = tweetRepository.findAll().size();

        // Create the Tweet
        TweetDTO tweetDTO = tweetMapper.toDto(tweet);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTweetMockMvc.perform(put("/api/tweets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetDTO)))
            .andExpect(status().isCreated());

        // Validate the Tweet in the database
        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTweet() throws Exception {
        // Initialize the database
        tweetRepository.saveAndFlush(tweet);
        int databaseSizeBeforeDelete = tweetRepository.findAll().size();

        // Get the tweet
        restTweetMockMvc.perform(delete("/api/tweets/{id}", tweet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tweet> tweetList = tweetRepository.findAll();
        assertThat(tweetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tweet.class);
        Tweet tweet1 = new Tweet();
        tweet1.setId(1L);
        Tweet tweet2 = new Tweet();
        tweet2.setId(tweet1.getId());
        assertThat(tweet1).isEqualTo(tweet2);
        tweet2.setId(2L);
        assertThat(tweet1).isNotEqualTo(tweet2);
        tweet1.setId(null);
        assertThat(tweet1).isNotEqualTo(tweet2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TweetDTO.class);
        TweetDTO tweetDTO1 = new TweetDTO();
        tweetDTO1.setId(1L);
        TweetDTO tweetDTO2 = new TweetDTO();
        assertThat(tweetDTO1).isNotEqualTo(tweetDTO2);
        tweetDTO2.setId(tweetDTO1.getId());
        assertThat(tweetDTO1).isEqualTo(tweetDTO2);
        tweetDTO2.setId(2L);
        assertThat(tweetDTO1).isNotEqualTo(tweetDTO2);
        tweetDTO1.setId(null);
        assertThat(tweetDTO1).isNotEqualTo(tweetDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tweetMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tweetMapper.fromId(null)).isNull();
    }
}
