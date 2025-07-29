package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.CreateTopicDTO;
import br.com.rb.api.application.dto.TopicDetailsDTO;
import br.com.rb.api.application.service.TopicService;
import br.com.rb.api.domain.model.Topic;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topics")
public class TopicController {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    public ResponseEntity<TopicDetailsDTO> create(@RequestBody @Valid CreateTopicDTO dto, UriComponentsBuilder uriBuilder) {
        Topic newTopic = topicService.createTopic(dto);
        var uri = uriBuilder.path("/topics/{id}").buildAndExpand(newTopic.getId()).toUri();

        var detailsDto = new TopicDetailsDTO(newTopic);
        return ResponseEntity.created(uri).body(detailsDto);
    }
}
