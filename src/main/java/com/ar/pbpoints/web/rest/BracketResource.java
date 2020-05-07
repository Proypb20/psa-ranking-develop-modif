package com.ar.pbpoints.web.rest;

import com.ar.pbpoints.domain.Bracket;
import com.ar.pbpoints.repository.BracketRepository;
import com.ar.pbpoints.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ar.pbpoints.domain.Bracket}.
 */
@RestController
@RequestMapping("/api")
public class BracketResource {

    private final Logger log = LoggerFactory.getLogger(BracketResource.class);

    private final BracketRepository bracketRepository;

    public BracketResource(BracketRepository bracketRepository) {
        this.bracketRepository = bracketRepository;
    }

    /**
     * {@code GET  /brackets} : get all the brackets.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of brackets in body.
     */
    @GetMapping("/brackets")
    public List<Bracket> getAllBrackets() {
        log.debug("REST request to get all Brackets");
        return bracketRepository.findAll();
    }

    /**
     * {@code GET  /brackets/:id} : get the "id" bracket.
     *
     * @param id the id of the bracket to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bracket, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/brackets/{id}")
    public ResponseEntity<Bracket> getBracket(@PathVariable Long id) {
        log.debug("REST request to get Bracket : {}", id);
        Optional<Bracket> bracket = bracketRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bracket);
    }
}
