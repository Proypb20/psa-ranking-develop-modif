package com.psa.ranking.service;

import com.psa.ranking.domain.Event;
import com.psa.ranking.domain.EventCategory;
import com.psa.ranking.domain.Game;
import com.psa.ranking.repository.EventRepository;
import com.psa.ranking.repository.GameRepository;
import com.psa.ranking.repository.EventCategoryRepository;
import com.psa.ranking.service.dto.EventDTO;
import com.psa.ranking.service.mapper.EventMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Service Implementation for managing {@link Event}.
 */
@Service
@Transactional
public class EventService {

    private final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    
    private final EventCategoryRepository eventCategoryRepository;
    
    private final GameRepository gameRepository;

    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository, EventCategoryRepository eventCategoryRepository, GameRepository gameRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.gameRepository = gameRepository;
        this.eventMapper = eventMapper;
    }

    /**
     * Save a event.
     *
     * @param eventDTO the entity to save.
     * @return the persisted entity.
     */
    public EventDTO save(EventDTO eventDTO) {
        log.debug("Request to save Event : {}", eventDTO);
        Event event = eventMapper.toEntity(eventDTO);
        event = eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    /**
     * Get all the events.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        return eventRepository.findAll(pageable)
            .map(eventMapper::toDto);
    }


    /**
     * Get one event by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventDTO> findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        return eventRepository.findById(id)
            .map(eventMapper::toDto);
    }

    /**
     * Delete the event by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.deleteById(id);
    }
    
    /**
     * A partir de todos los los equipos que van a participar en un
     * evento-categoria, se genera el fixture para generar los games
     * 
     * @param eventCategory
     * @throws ParserConfigurationException 
     * @throws TransformerConfigurationException 
     */
    public void generarXML(Event event) throws ParserConfigurationException, TransformerConfigurationException {
        if (event.getEndInscriptionDate().isAfter(LocalDate.now())) {
            throw new NoResultException("La fecha de Inscripcion aun no ha finalizado");
        }
        log.info("*** Generando XML para el evento {}", event);
        
        try
        {
	        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
	        Document document = documentBuilder.newDocument();
	        
	        Element root = document.createElement("PBPOINTS");
	        document.appendChild(root);
	        
	        Element eventId = document.createElement("EVENT_ID");
	        eventId.appendChild(document.createTextNode(event.getId().toString()));
	        root.appendChild(eventId);
	        
	        Element ownerId = document.createElement("OWNER_ID");
	        ownerId.appendChild(document.createTextNode(event.getTournament().getOwner().getId().toString()));
	        root.appendChild(ownerId);
	        
	        Element hash = document.createElement("HASH");
	        hash.appendChild(document.createTextNode(event.getTournament().getOwner().getLangKey()));
	        root.appendChild(hash);
	        
	        Element setup = document.createElement("SETUP");
	        root.appendChild(setup);
	        
	        Element categorys = document.createElement("CATEGORY");
	        setup.appendChild(categorys);
	        
	        List<EventCategory> eventCategories = eventCategoryRepository
	                .findByEvent(event); 
	        log.debug(eventCategories.toString());
	        if (eventCategories.isEmpty())
	        {
	        	 throw new NoResultException("No hay Categorias en el evento");
	        }
	        log.debug("Categorias a incluir: {}", eventCategories);
	        for (EventCategory eventCategory : eventCategories) {
	        	Element name = document.createElement("NAME");
	            name.appendChild(document.createTextNode(eventCategory.getCategory().getName()));
	            categorys.appendChild(name);
	            
	            Element timeType = document.createElement("TIME_TYPE");
	            timeType.appendChild(document.createTextNode(eventCategory.getCategory().getGameTimeType().name()));
	            categorys.appendChild(timeType);
	            
	            Element time = document.createElement("TIME");
	            time.appendChild(document.createTextNode(eventCategory.getCategory().getGameTime().toString()));
	            categorys.appendChild(time);
	            
	            Element waitType = document.createElement("WAIT_TYPE");
	            waitType.appendChild(document.createTextNode(eventCategory.getCategory().getStopTimeType().name()));
	            categorys.appendChild(waitType);
	            
	            Element wait = document.createElement("WAIT");
	            wait.appendChild(document.createTextNode(eventCategory.getCategory().getStopTime().toString()));
	            categorys.appendChild(wait);
	            
	            Element waitSpType = document.createElement("WAIT_SP_TYPE");
	            waitSpType.appendChild(document.createTextNode("MINUTES"));
	            categorys.appendChild(waitSpType);
	            
	            Element waitSp = document.createElement("WAIT_SP");
	            waitSp.appendChild(document.createTextNode("1"));
	            categorys.appendChild(waitSp);
	            
	            Element points = document.createElement("POINTS");
	            points.appendChild(document.createTextNode(eventCategory.getCategory().getTotalPoints().toString()));
	            categorys.appendChild(points);
	            
	            Element dif = document.createElement("DIF");
	            dif.appendChild(document.createTextNode(eventCategory.getCategory().getDifPoints().toString()));
	            categorys.appendChild(dif);
			}
	        Element fixture = document.createElement("FIXTURE");
	        root.appendChild(fixture);
	        
	        Element categoryf = document.createElement("CATEGORY");
	        fixture.appendChild(categoryf);
	        
	        for (EventCategory eventCategory : eventCategories) {
	        	
	        	Element name = document.createElement("NAME");
	        	name.appendChild(document.createTextNode(eventCategory.getCategory().getName()));
	            categoryf.appendChild(name);
	            
	            Element gamesxml = document.createElement("GAMES");
		        categoryf.appendChild(gamesxml);
		        
		        List<Game> games = gameRepository
		        		.findByEventCategory(eventCategory); 
		        
		        log.debug(games.toString());
		        
		        for (Game gameloop : games)
		        {
		            Element gamexml = document.createElement("GAME");
			        gamesxml.appendChild(gamexml);
			        
			        Element gameid = document.createElement("ID");
		        	gameid.appendChild(document.createTextNode(gameloop.getId().toString()));
		            gamexml.appendChild(gameid);
			        
		            Element spid = document.createElement("SD_ID");
		        	spid.appendChild(document.createTextNode(gameloop.getSplitDeckNum().toString()));
		            gamexml.appendChild(spid);
		            
		            Element clasif = document.createElement("CLASIF");
		        	clasif.appendChild(document.createTextNode("1"));
		            gamexml.appendChild(clasif);
		            
		            Element teama = document.createElement("TEAM_A");
		        	teama.appendChild(document.createTextNode(gameloop.getTeamA().getName()));
		            gamexml.appendChild(teama);
		            
		            Element teamb = document.createElement("TEAM_B");
		        	teamb.appendChild(document.createTextNode(gameloop.getTeamB().getName()));
		            gamexml.appendChild(teamb);
		        }
	        }
	        
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource domSource = new DOMSource(document);
	        String xmlFilePath = "C:\\PBPoints\\PBPOINTS_" + event.getName().replace(" ", "").toUpperCase() + ".pbp";
			StreamResult streamResult = new StreamResult(new File(xmlFilePath));
            
			transformer.transform(domSource, streamResult);

		    } 
            catch (ParserConfigurationException pce) {
		        pce.printStackTrace();
		    } 
            catch (TransformerException tfe) {
		        tfe.printStackTrace();
		    }
    }

    public void generaXML(Long eventId) throws NoResultException, ParserConfigurationException, TransformerConfigurationException{
    	
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            this.generarXML(event.get());
        } else {
            throw new NoResultException("No se encontró un evento para generar XML");
        }
    }
}
