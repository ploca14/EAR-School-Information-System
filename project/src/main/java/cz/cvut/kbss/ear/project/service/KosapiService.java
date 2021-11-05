package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.kosapi.oauth2.TokenManager;
import cz.cvut.kbss.ear.project.kosapi.util.AtomConverter;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.enums.CourseCompletionType;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public Course getCourse(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        String courseUrl = "/courses/" + code;
        String response = restTemplate.exchange(resourceServerURL + courseUrl, HttpMethod.GET, request, String.class).getBody();

        Course course = new Course();

        try {
            List<HashMap<String, String>> parsedResponse = AtomConverter.getAtomContent(response);
            for (HashMap<String, String> responseValues : parsedResponse){
                course.setDescription(responseValues.get("description"));
                course.setCode(responseValues.get("code"));
                course.setCredits(Integer.parseInt(responseValues.get("credits")));
                course.setName(responseValues.get("name"));
                course.setCompletionType(convertCompletionType(responseValues.get("completion")));
            }

        } catch (IOException | SAXException | JDOMException e){
            return null;
        }
        return course;
    }

    private CourseCompletionType convertCompletionType(String kosapiCompletionType){
        switch(kosapiCompletionType){
            case "CLFD_CREDIT":
                return CourseCompletionType.KZ;
            case "CREDIT_EXAM":
                return CourseCompletionType.ZAK;
            case "CREDIT":
                return CourseCompletionType.Z;
            case "DEFENCE":
                return CourseCompletionType.O;
            case "EXAM":
                return CourseCompletionType.ZK;
            default:
                return CourseCompletionType.U;

        }
    }

    List<Course> getCoursesInSemester(String semesterCode){
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        String courseUrl = "/courses?sem=" + semesterCode + "&limit=1000"; // TODO offset
        String response = restTemplate.exchange(resourceServerURL + courseUrl, HttpMethod.GET, request, String.class).getBody();

        ArrayList<Course> result = new ArrayList<>();

        try {
            List<HashMap<String, String>> parsedResponse = AtomConverter.getAtomContent(response);
            for (HashMap<String, String> responseValues : parsedResponse){
                Course course = new Course();
                course.setDescription(responseValues.get("description"));
                course.setCode(responseValues.get("code"));
                course.setCredits(responseValues.get("credits") != null ? Integer.parseInt(responseValues.get("credits")) : 0);
                course.setName(responseValues.get("name"));
                course.setCompletionType(convertCompletionType(responseValues.get("completion")));
                result.add(course);
            }

        } catch (IOException | SAXException | JDOMException e){
            return null;
        }
        return result;
    }

    /**
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
