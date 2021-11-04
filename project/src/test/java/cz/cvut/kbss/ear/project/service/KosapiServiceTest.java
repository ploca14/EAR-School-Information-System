package cz.cvut.kbss.ear.project.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class KosapiServiceTest {

    @Autowired
    KosapiService kosapiService;

    @Test
    public void tryit(){
        System.out.println(kosapiService.getToken());
    }
}
