package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.topic.CreateTopicDTO;
import br.com.rb.api.application.dto.topic.TopicDetailsDTO;
import br.com.rb.api.application.dto.topic.UpdateTopicDTO;
import br.com.rb.api.application.service.TopicService;
import br.com.rb.api.domain.model.Topic;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/topics")
public class TopicController {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    public ResponseEntity<TopicDetailsDTO> create(@RequestBody @Valid CreateTopicDTO dto, UriComponentsBuilder uriBuilder) {
        Topic newTopic = topicService.create(dto);
        TopicDetailsDTO detailsDto = new TopicDetailsDTO(newTopic);

        var uri = uriBuilder.path("/topics/{id}").buildAndExpand(newTopic.getId()).toUri();

        return ResponseEntity.created(uri).body(detailsDto);
    }

    @GetMapping
    public ResponseEntity<Page<TopicDetailsDTO>> list(@PageableDefault(size = 10, sort = {"creationDate"}) Pageable pageable) {
        Page<TopicDetailsDTO> page = topicService.findAll(pageable).map(TopicDetailsDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDetailsDTO> detail(@PathVariable Long id) {
        Topic topic = topicService.findById(id);
        return ResponseEntity.ok(new TopicDetailsDTO(topic));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDetailsDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateTopicDTO dto, @RequestParam Long userId) {
        Topic updatedTopic = topicService.update(id, dto, userId);
        return ResponseEntity.ok(new TopicDetailsDTO(updatedTopic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long userId) {
        topicService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
