package br.com.rb.api.controller;

import br.com.rb.api.application.dto.answer.CreateAnswerDTO;
import br.com.rb.api.application.dto.answer.DetailsAnswerDTO;
import br.com.rb.api.application.dto.answer.UpdateAnswerDTO;
import br.com.rb.api.application.service.AnswerService;
import br.com.rb.api.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public ResponseEntity<DetailsAnswerDTO> create(@RequestBody @Valid CreateAnswerDTO dto,
                                                   @AuthenticationPrincipal User authenticatedUser,
                                                   UriComponentsBuilder uriBuilder){
        DetailsAnswerDTO newAnswer = answerService.create(dto, authenticatedUser.getId());
        var uri = uriBuilder.path("/answers/{id}").buildAndExpand(newAnswer.id()).toUri();
        return ResponseEntity.created(uri).body(newAnswer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @answerService.isAuthor(#id, principal.id)")
    public ResponseEntity<DetailsAnswerDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateAnswerDTO dto){
        DetailsAnswerDTO updatedAnswer = answerService.update(id, dto);
        return ResponseEntity.ok(updatedAnswer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @answerService.isAuthor(#id, principal.id)")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        answerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}