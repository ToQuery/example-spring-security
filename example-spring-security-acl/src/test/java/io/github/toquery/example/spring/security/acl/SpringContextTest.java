package io.github.toquery.example.spring.security.acl;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleSpringSecurityAclApplication.class)
public class SpringContextTest {

    @Test
    public void loadContext() {
    }
}
