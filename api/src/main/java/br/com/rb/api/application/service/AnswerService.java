package br.com.rb.api.application.service;

import br.com.rb.api.application.dto.answer.CreateAnswerDTO;
import br.com.rb.api.application.dto.answer.DetailsAnswerDTO;
import br.com.rb.api.application.dto.answer.UpdateAnswerDTO;
import br.com.rb.api.application.mapper.AnswerMapper;
import br.com.rb.api.domain.model.Answer;
import br.com.rb.api.domain.model.Topic;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.domain.repository.AnswerRepository;
import br.com.rb.api.domain.repository.TopicRepository;
import br.com.rb.api.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerMapper answerMapper;

    @Transactional
    public DetailsAnswerDTO create(CreateAnswerDTO dto, Long userId){
        Topic topic = topicRepository.findById(dto.topicId())
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado com o ID: " + dto.topicId()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + userId));

        Answer answer = new Answer();
        answer.setText(dto.text());
        answer.setTopic(topic);
        answer.setUser(user);

        answer = answerRepository.save(answer);

        return answerMapper.toDto(answer);
    }

    @Transactional
    public DetailsAnswerDTO update(Long id, UpdateAnswerDTO dto){
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada com o ID: " + id));

        answer.setText(dto.text());
        Answer updatedAnswer = answerRepository.save(answer);
        return answerMapper.toDto(updatedAnswer);
    }

    @Transactional
    public void delete(Long id){
        if (!answerRepository.existsById(id)) {
            throw new EntityNotFoundException("Resposta não encontrada com o ID: " + id);
        }
        answerRepository.deleteById(id);
    }
}