package io.github.toquery.example.spring.security.acl.message.service;

import io.github.toquery.example.spring.security.acl.message.dao.MessageRepository;
import io.github.toquery.example.spring.security.acl.message.entity.Message;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;


    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Message> findAll() {
        return this.messageRepository.findAll();
    }

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    public Message findById(Long id) {
        return this.messageRepository.findById(id).orElse(null);
    }

    @PreAuthorize("hasPermission(#message, 'WRITE')")
    public Message save(@Param("message") Message message) {
        return this.messageRepository.save(message);
    }
}
