package com.istic.jhipstervideogen.web.rest;

import com.istic.jhipstervideogen.Application;
import com.istic.jhipstervideogen.domain.Videogen;
import com.istic.jhipstervideogen.repository.VideogenRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VideogenResource REST controller.
 *
 * @see VideogenResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VideogenResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private VideogenRepository videogenRepository;

    private MockMvc restVideogenMockMvc;

    private Videogen videogen;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VideogenResource videogenResource = new VideogenResource();
        ReflectionTestUtils.setField(videogenResource, "videogenRepository", videogenRepository);
        this.restVideogenMockMvc = MockMvcBuilders.standaloneSetup(videogenResource).build();
    }

    @Before
    public void initTest() {
        videogen = new Videogen();
        videogen.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createVideogen() throws Exception {
        int databaseSizeBeforeCreate = videogenRepository.findAll().size();

        // Create the Videogen
        restVideogenMockMvc.perform(post("/api/videogens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(videogen)))
                .andExpect(status().isCreated());

        // Validate the Videogen in the database
        List<Videogen> videogens = videogenRepository.findAll();
        assertThat(videogens).hasSize(databaseSizeBeforeCreate + 1);
        Videogen testVideogen = videogens.get(videogens.size() - 1);
        assertThat(testVideogen.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllVideogens() throws Exception {
        // Initialize the database
        videogenRepository.saveAndFlush(videogen);

        // Get all the videogens
        restVideogenMockMvc.perform(get("/api/videogens"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(videogen.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getVideogen() throws Exception {
        // Initialize the database
        videogenRepository.saveAndFlush(videogen);

        // Get the videogen
        restVideogenMockMvc.perform(get("/api/videogens/{id}", videogen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(videogen.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVideogen() throws Exception {
        // Get the videogen
        restVideogenMockMvc.perform(get("/api/videogens/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideogen() throws Exception {
        // Initialize the database
        videogenRepository.saveAndFlush(videogen);

		int databaseSizeBeforeUpdate = videogenRepository.findAll().size();

        // Update the videogen
        videogen.setName(UPDATED_NAME);
        restVideogenMockMvc.perform(put("/api/videogens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(videogen)))
                .andExpect(status().isOk());

        // Validate the Videogen in the database
        List<Videogen> videogens = videogenRepository.findAll();
        assertThat(videogens).hasSize(databaseSizeBeforeUpdate);
        Videogen testVideogen = videogens.get(videogens.size() - 1);
        assertThat(testVideogen.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteVideogen() throws Exception {
        // Initialize the database
        videogenRepository.saveAndFlush(videogen);

		int databaseSizeBeforeDelete = videogenRepository.findAll().size();

        // Get the videogen
        restVideogenMockMvc.perform(delete("/api/videogens/{id}", videogen.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Videogen> videogens = videogenRepository.findAll();
        assertThat(videogens).hasSize(databaseSizeBeforeDelete - 1);
    }
}
