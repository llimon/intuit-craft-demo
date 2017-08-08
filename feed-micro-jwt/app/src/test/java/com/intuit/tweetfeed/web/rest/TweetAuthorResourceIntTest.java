package com.intuit.tweetfeed.web.rest;

import com.intuit.tweetfeed.TweetfeedApp;

import com.intuit.tweetfeed.domain.TweetAuthor;
import com.intuit.tweetfeed.repository.TweetAuthorRepository;
import com.intuit.tweetfeed.service.dto.TweetAuthorDTO;
import com.intuit.tweetfeed.service.mapper.TweetAuthorMapper;
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
 * Test class for the TweetAuthorResource REST controller.
 *
 * @see TweetAuthorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TweetfeedApp.class)
public class TweetAuthorResourceIntTest {

    private static final String DEFAULT_SCREENNAME = "AAAAAAAAAA";
    private static final String UPDATED_SCREENNAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PROFILE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE_URL = "BBBBBBBBBB";

    @Autowired
    private TweetAuthorRepository tweetAuthorRepository;

    @Autowired
    private TweetAuthorMapper tweetAuthorMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTweetAuthorMockMvc;

    private TweetAuthor tweetAuthor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TweetAuthorResource tweetAuthorResource = new TweetAuthorResource(tweetAuthorRepository, tweetAuthorMapper);
        this.restTweetAuthorMockMvc = MockMvcBuilders.standaloneSetup(tweetAuthorResource)
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
    public static TweetAuthor createEntity(EntityManager em) {
        TweetAuthor tweetAuthor = new TweetAuthor()
            .screenname(DEFAULT_SCREENNAME)
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL);
        return tweetAuthor;
    }

    @Before
    public void initTest() {
        tweetAuthor = createEntity(em);
    }

    @Test
    @Transactional
    public void createTweetAuthor() throws Exception {
        int databaseSizeBeforeCreate = tweetAuthorRepository.findAll().size();

        // Create the TweetAuthor
        TweetAuthorDTO tweetAuthorDTO = tweetAuthorMapper.toDto(tweetAuthor);
        restTweetAuthorMockMvc.perform(post("/api/tweet-authors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetAuthorDTO)))
            .andExpect(status().isCreated());

        // Validate the TweetAuthor in the database
        List<TweetAuthor> tweetAuthorList = tweetAuthorRepository.findAll();
        assertThat(tweetAuthorList).hasSize(databaseSizeBeforeCreate + 1);
        TweetAuthor testTweetAuthor = tweetAuthorList.get(tweetAuthorList.size() - 1);
        assertThat(testTweetAuthor.getScreenname()).isEqualTo(DEFAULT_SCREENNAME);
        assertThat(testTweetAuthor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTweetAuthor.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTweetAuthor.getProfileImageUrl()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    public void createTweetAuthorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tweetAuthorRepository.findAll().size();

        // Create the TweetAuthor with an existing ID
        tweetAuthor.setId(1L);
        TweetAuthorDTO tweetAuthorDTO = tweetAuthorMapper.toDto(tweetAuthor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTweetAuthorMockMvc.perform(post("/api/tweet-authors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetAuthorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TweetAuthor> tweetAuthorList = tweetAuthorRepository.findAll();
        assertThat(tweetAuthorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkScreennameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tweetAuthorRepository.findAll().size();
        // set the field null
        tweetAuthor.setScreenname(null);

        // Create the TweetAuthor, which fails.
        TweetAuthorDTO tweetAuthorDTO = tweetAuthorMapper.toDto(tweetAuthor);

        restTweetAuthorMockMvc.perform(post("/api/tweet-authors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetAuthorDTO)))
            .andExpect(status().isBadRequest());

        List<TweetAuthor> tweetAuthorList = tweetAuthorRepository.findAll();
        assertThat(tweetAuthorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tweetAuthorRepository.findAll().size();
        // set the field null
        tweetAuthor.setName(null);

        // Create the TweetAuthor, which fails.
        TweetAuthorDTO tweetAuthorDTO = tweetAuthorMapper.toDto(tweetAuthor);

        restTweetAuthorMockMvc.perform(post("/api/tweet-authors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetAuthorDTO)))
            .andExpect(status().isBadRequest());

        List<TweetAuthor> tweetAuthorList = tweetAuthorRepository.findAll();
        assertThat(tweetAuthorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = tweetAuthorRepository.findAll().size();
        // set the field null
        tweetAuthor.setCreatedAt(null);

        // Create the TweetAuthor, which fails.
        TweetAuthorDTO tweetAuthorDTO = tweetAuthorMapper.toDto(tweetAuthor);

        restTweetAuthorMockMvc.perform(post("/api/tweet-authors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetAuthorDTO)))
            .andExpect(status().isBadRequest());

        List<TweetAuthor> tweetAuthorList = tweetAuthorRepository.findAll();
        assertThat(tweetAuthorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTweetAuthors() throws Exception {
        // Initialize the database
        tweetAuthorRepository.saveAndFlush(tweetAuthor);

        // Get all the tweetAuthorList
        restTweetAuthorMockMvc.perform(get("/api/tweet-authors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tweetAuthor.getId().intValue())))
            .andExpect(jsonPath("$.[*].screenname").value(hasItem(DEFAULT_SCREENNAME.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL.toString())));
    }

    @Test
    @Transactional
    public void getTweetAuthor() throws Exception {
        // Initialize the database
        tweetAuthorRepository.saveAndFlush(tweetAuthor);

        // Get the tweetAuthor
        restTweetAuthorMockMvc.perform(get("/api/tweet-authors/{id}", tweetAuthor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tweetAuthor.getId().intValue()))
            .andExpect(jsonPath("$.screenname").value(DEFAULT_SCREENNAME.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.profileImageUrl").value(DEFAULT_PROFILE_IMAGE_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTweetAuthor() throws Exception {
        // Get the tweetAuthor
        restTweetAuthorMockMvc.perform(get("/api/tweet-authors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTweetAuthor() throws Exception {
        // Initialize the database
        tweetAuthorRepository.saveAndFlush(tweetAuthor);
        int databaseSizeBeforeUpdate = tweetAuthorRepository.findAll().size();

        // Update the tweetAuthor
        TweetAuthor updatedTweetAuthor = tweetAuthorRepository.findOne(tweetAuthor.getId());
        updatedTweetAuthor
            .screenname(UPDATED_SCREENNAME)
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL);
        TweetAuthorDTO tweetAuthorDTO = tweetAuthorMapper.toDto(updatedTweetAuthor);

        restTweetAuthorMockMvc.perform(put("/api/tweet-authors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetAuthorDTO)))
            .andExpect(status().isOk());

        // Validate the TweetAuthor in the database
        List<TweetAuthor> tweetAuthorList = tweetAuthorRepository.findAll();
        assertThat(tweetAuthorList).hasSize(databaseSizeBeforeUpdate);
        TweetAuthor testTweetAuthor = tweetAuthorList.get(tweetAuthorList.size() - 1);
        assertThat(testTweetAuthor.getScreenname()).isEqualTo(UPDATED_SCREENNAME);
        assertThat(testTweetAuthor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTweetAuthor.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTweetAuthor.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingTweetAuthor() throws Exception {
        int databaseSizeBeforeUpdate = tweetAuthorRepository.findAll().size();

        // Create the TweetAuthor
        TweetAuthorDTO tweetAuthorDTO = tweetAuthorMapper.toDto(tweetAuthor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTweetAuthorMockMvc.perform(put("/api/tweet-authors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tweetAuthorDTO)))
            .andExpect(status().isCreated());

        // Validate the TweetAuthor in the database
        List<TweetAuthor> tweetAuthorList = tweetAuthorRepository.findAll();
        assertThat(tweetAuthorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTweetAuthor() throws Exception {
        // Initialize the database
        tweetAuthorRepository.saveAndFlush(tweetAuthor);
        int databaseSizeBeforeDelete = tweetAuthorRepository.findAll().size();

        // Get the tweetAuthor
        restTweetAuthorMockMvc.perform(delete("/api/tweet-authors/{id}", tweetAuthor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TweetAuthor> tweetAuthorList = tweetAuthorRepository.findAll();
        assertThat(tweetAuthorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TweetAuthor.class);
        TweetAuthor tweetAuthor1 = new TweetAuthor();
        tweetAuthor1.setId(1L);
        TweetAuthor tweetAuthor2 = new TweetAuthor();
        tweetAuthor2.setId(tweetAuthor1.getId());
        assertThat(tweetAuthor1).isEqualTo(tweetAuthor2);
        tweetAuthor2.setId(2L);
        assertThat(tweetAuthor1).isNotEqualTo(tweetAuthor2);
        tweetAuthor1.setId(null);
        assertThat(tweetAuthor1).isNotEqualTo(tweetAuthor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TweetAuthorDTO.class);
        TweetAuthorDTO tweetAuthorDTO1 = new TweetAuthorDTO();
        tweetAuthorDTO1.setId(1L);
        TweetAuthorDTO tweetAuthorDTO2 = new TweetAuthorDTO();
        assertThat(tweetAuthorDTO1).isNotEqualTo(tweetAuthorDTO2);
        tweetAuthorDTO2.setId(tweetAuthorDTO1.getId());
        assertThat(tweetAuthorDTO1).isEqualTo(tweetAuthorDTO2);
        tweetAuthorDTO2.setId(2L);
        assertThat(tweetAuthorDTO1).isNotEqualTo(tweetAuthorDTO2);
        tweetAuthorDTO1.setId(null);
        assertThat(tweetAuthorDTO1).isNotEqualTo(tweetAuthorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tweetAuthorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tweetAuthorMapper.fromId(null)).isNull();
    }
}
