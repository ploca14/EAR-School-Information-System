package cz.cvut.kbss.ear.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.cvut.kbss.ear.project.exception.KosapiException;
import cz.cvut.kbss.ear.project.kosapi.wrappers.Entry;
import cz.cvut.kbss.ear.project.kosapi.entities.KosCourse;
import cz.cvut.kbss.ear.project.kosapi.wrappers.WrappedEntries;
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

        ObjectMapper xmlMapper = new XmlMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Course course = new Course();
        try {
            Entry<KosCourse> atomEntry = xmlMapper.readValue(response, new TypeReference<Entry<KosCourse>>() {});
            KosCourse kosCourse = atomEntry.getContent();
            if (kosCourse == null){
                throw new KosapiException("Failed to fetch course: " + code);
            }
            course.setDescription("TODO");
            course.setCode(kosCourse.getCode());
            course.setCredits(kosCourse.getCredits() != null ? Integer.parseInt(kosCourse.getCredits()) : 0);
            course.setName(kosCourse.getName());
            course.setCompletionType(convertCompletionType(kosCourse.getCompletion()));
        } catch (JsonProcessingException e) {
            throw new KosapiException("Failed to parse course: " + code
                    + "\nError message:" + e.getMessage());
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

    public List<Course> getCoursesInSemester(String semesterCode){
        ArrayList<Course> result = new ArrayList<>();
        try {
            ArrayList<KosCourse> kosCourses = getAllCourseEntriesInSemester(semesterCode);
            for (KosCourse kosCourse : kosCourses){
                Course course = new Course();
                course.setDescription("TODO");
                course.setCode(kosCourse.getCode());
                course.setCredits(kosCourse.getCredits() != null ? Integer.parseInt(kosCourse.getCredits()) : 0);
                course.setName(kosCourse.getName());
                course.setCompletionType(convertCompletionType(kosCourse.getCompletion()));
                result.add(course);
            }
        } catch (JsonProcessingException e) {
            throw new KosapiException("Failed to parse courses from semester: " + semesterCode
                    + "\nError message:" + e.getMessage());
        }
        if (result.size() == 0){
            throw new KosapiException("Failed to fetch courses from semester: " + semesterCode
                    + "0 courses parsed");
        }
        return result;
    }

    private ArrayList<KosCourse> getAllCourseEntriesInSemester(String semesterCode) throws JsonProcessingException {
        int offset = 0;
        ArrayList<KosCourse> result = new ArrayList<>();

        while (true){
            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTHORIZATION, "Bearer " + token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            String courseUrl = "/courses?sem=" + semesterCode + "&limit=1000&offset=" + offset; // TODO offset
            String response = restTemplate.exchange(resourceServerURL + courseUrl, HttpMethod.GET, request, String.class).getBody();

            ObjectMapper xmlMapper = new XmlMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            WrappedEntries<KosCourse> entries = xmlMapper.readValue(response, new TypeReference<WrappedEntries<KosCourse>>() {});
            if (entries.getContentList().size() != 0){
                result.addAll(entries.getContentList());
                offset += 1000;
            }

            else{
                break;
            }
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
