package com.istic.jhipstervideogen.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.istic.jhipstervideogen.domain.Videogen;
import com.istic.jhipstervideogen.repository.VideogenRepository;

import app.videoGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * REST controller for managing Videogen.
 */
@RestController
@RequestMapping("/api")
public class VideogenResource {

    private final Logger log = LoggerFactory.getLogger(VideogenResource.class);

    @Inject
    private VideogenRepository videogenRepository;

    /**
     * POST  /videogens -> Create a new videogen.
     */
    @RequestMapping(value = "/videogens",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Videogen videogen) throws URISyntaxException {
        log.debug("REST request to save Videogen : {}", videogen);
        if (videogen.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new videogen cannot already have an ID").build();
        }
        videogenRepository.save(videogen);
        return ResponseEntity.created(new URI("/api/videogens/" + videogen.getId())).build();
    }

    /**
     * PUT  /videogens -> Updates an existing videogen.
     */
    @RequestMapping(value = "/videogens",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Videogen videogen) throws URISyntaxException {
        log.debug("REST request to update Videogen : {}", videogen);
        if (videogen.getId() == null) {
            return create(videogen);
        }
        videogenRepository.save(videogen);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /videogens -> get all the videogens.
     */
    @RequestMapping(value = "/videogens",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Videogen> getAll() {
        log.debug("REST request to get all Videogens");
        return videogenRepository.findAll();
    }

    /**
     * GET  /videogens/:id -> get the "id" videogen.
     */
    @RequestMapping(value = "/videogens/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Videogen> get(@PathVariable Long id) {
        log.debug("REST request to get Videogen : {}", id);
        return Optional.ofNullable(videogenRepository.findOne(id))
            .map(videogen -> new ResponseEntity<>(
                videogen,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /videogens/:id -> delete the "id" videogen.
     */
    @RequestMapping(value = "/videogens/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Videogen : {}", id);
        videogenRepository.delete(id);
    }
    
    /**
     * Generate a random video set of videos, no need of video gen here.
     */
    @RequestMapping(value = "/generateRandom",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String generateRandom() {   
    	
        Random rand = new Random();
    	
    	//Open the folder which contain the videos
    	File folder = new File("/Users/yoannlt/Documents/jhipster/src/main/webapp/assets/videos");
        File[] listOfFiles = folder.listFiles();
        
        // Nomde video max
        int nbmax = listOfFiles.length - 1;
       
        // Prepare the result as a JSON format
        String result = "{\"videos\" : ["; 
        
    	// Number of videos load
    	int nombreVideos = 0;
    	while(nombreVideos<nbmax){
            // Generate a random number of videos to return
    		int nombreAleatoire = rand.nextInt(nbmax - 1 + 1) + 1;
    		result+= "{\"name\" : \""+listOfFiles[nombreAleatoire].getName()+"\"}";
    		nombreVideos++;
            
    		if(nbmax > nombreVideos){
            	result+=","; 
            }
    	}
        
        // end of the JSON
        result += "]}";
        
    	// return the result as a JSON string formatted
    	return result;
    }
    
    @RequestMapping(value = "/getVignettes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String getVignettes() {
    	File folder = new File("/Users/yoannlt/Documents/jhipster/src/main/webapp/assets/vignettes");
        File[] listOfFiles = folder.listFiles();
        
       int i = 0;
       int nbFichier = listOfFiles.length;
        
        // Prepare the result as a JSON format
        String result = "{\"vignettes\" : ["; 
        for(File vignette : listOfFiles){
        	result+= "{\"name\" : \""+vignette.getName()+"\"}";
        	i++;
        	if(i < nbFichier){
            	result+=","; 
            }
        }
        
        // end of the JSON
        result += "]}";
        
		return result;   
    }
    
    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }
}