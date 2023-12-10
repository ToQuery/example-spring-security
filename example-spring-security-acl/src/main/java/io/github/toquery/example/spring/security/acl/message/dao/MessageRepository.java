package io.github.toquery.example.spring.security.acl.message.dao;


import io.github.toquery.example.spring.security.acl.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {


}
