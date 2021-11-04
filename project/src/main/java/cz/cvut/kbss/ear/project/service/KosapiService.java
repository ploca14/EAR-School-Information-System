package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.kosapi.oauth2.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KosapiService {

    public static final String AUTHORIZATION = "Authorization";

    @Value("${resource.server.url}")
    private String resourceServerURL;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private RestTemplate restTemplate;

    private String token;

    public KosapiService(TokenManager tokenManager, RestTemplate restTemplate) {
        this.tokenManager = tokenManager;
        this.restTemplate = restTemplate;
        this.token = tokenManager.getAccessToken();
    }

    public String getToken() {
        return token;
    }

    /**
    List<KosCourse> getKosCoursesInSemester(String semesterCode){
        //return Arrays.stream(restTemplate.getForObject(coursesEndpoint, KosCourse[].class)).collect(Collectors.toList());
    }

    List<KosStudent> getStudentsInKosCourse(KosCourse kosCourse){

    }

    List<KosTeacher> getTeachersInKosCourse(KosCourse kosCourse){

    }

    List<KosParallel> getParallelsInKosCourse(){

    }

    List<KosTeacher> getTeachersInParallel(){

    }

    List<KosStudent> getStudentsInParallel(){

    }**/
}
