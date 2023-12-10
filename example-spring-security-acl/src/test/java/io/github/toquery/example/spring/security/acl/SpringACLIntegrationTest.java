package io.github.toquery.example.spring.security.acl;

import io.github.toquery.example.spring.security.acl.message.entity.Message;
import io.github.toquery.example.spring.security.acl.message.service.MessageService;
import jakarta.annotation.Resource;
import net.bytebuddy.utility.RandomString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleSpringSecurityAclApplication.class)
public class SpringACLIntegrationTest  {


    @Resource
    private MessageService messageService;

    /**
     * 使用用户zhangsan获取所有的消息时，仅能够获取到张三拥有read权限的消息1
     */
    @Test
    @WithMockUser(username = "zhangsan")
    public void findAllByUser() {
        List<Message> messages = this.messageService.findAll();
        assertNotNull(messages);
        assertEquals(1, messages.size());
//        assertEquals(1, messages.get(0).getId());
    }

    /**
     * 角色ADMIN获取全部的消息
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void findAllByRole() {
        List<Message> messages = this.messageService.findAll();
        assertNotNull(messages);
        assertEquals(3, messages.size());
    }

    /**
     * 张三只能获取到自己的消息
     * 获取其它2个消息时发生权限异常
     */
    @Test
    @WithMockUser(username = "zhangsan")
    public void findByIdByUser() {
        assertNotNull(this.messageService.findById(1L));
        assertThrows(AccessDeniedException.class, () -> this.messageService.findById(2L));
        assertThrows(AccessDeniedException.class, () -> this.messageService.findById(3L));
    }

    /**
     * ADMIN能获取所有的消息
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void findByIdByRole() {
        assertNotNull(this.messageService.findById(1L));
        assertNotNull(this.messageService.findById(2L));
        assertNotNull(this.messageService.findById(3L));
    }

    /**
     * 李四拥有2号消息的读权限，但并不拥有写权限，当发生写操作时发生权限异常
     */
    @Test
    @WithMockUser(username = "lisi")
    public void saveWithUserAndCatchException() {
        Message message = this.messageService.findById(2L);
        message.setContent(RandomString.make());
        assertThrows(AccessDeniedException.class, () -> this.messageService.save(message));
    }

    /**
     * 张三拥有1号消息的读写权限
     */
    @Test
    @WithMockUser(username = "zhangsan")
    public void saveWithUser() {
        Message message = this.messageService.findById(1L);
        message.setContent(RandomString.make());
        this.messageService.save(message);
    }

    /**
     * 管理员拥有1号消息的读写权限
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void saveWithRole() {
        Message message = this.messageService.findById(3L);
        message.setContent(RandomString.make());
        this.messageService.save(message);
    }
}
