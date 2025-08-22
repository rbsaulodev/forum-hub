package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.answer.CreateAnswerDTO;
import br.com.rb.api.application.dto.answer.DetailsAnswerDTO;
import br.com.rb.api.application.dto.answer.UpdateAnswerDTO;
import br.com.rb.api.application.service.AnswerService;
import br.com.rb.api.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public ResponseEntity<DetailsAnswerDTO> create(@RequestBody @Valid CreateAnswerDTO dto,
                                                   @AuthenticationPrincipal User authenticatedUser,
                                                   UriComponentsBuilder uriBuilder){
        Long userId = authenticatedUser.getId();
        DetailsAnswerDTO newAnswer = answerService.create(dto, userId);

        var uri = uriBuilder.path("/answers/{id}").buildAndExpand(newAnswer.id()).toUri();
        return ResponseEntity.created(uri).body(newAnswer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetailsAnswerDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateAnswerDTO dto){
        DetailsAnswerDTO updatedAnswer = answerService.update(id, dto);
        return ResponseEntity.ok(updatedAnswer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        answerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}