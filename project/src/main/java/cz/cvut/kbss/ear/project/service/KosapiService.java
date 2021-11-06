package cz.cvut.kbss.ear.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.cvut.kbss.ear.project.exception.KosapiException;
import cz.cvut.kbss.ear.project.kosapi.entities.KosCourseInstance;
import cz.cvut.kbss.ear.project.kosapi.entities.KosTeacher;
import cz.cvut.kbss.ear.project.kosapi.entities.TeacherLink;
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
import java.util.*;

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

    public KosCourse getCourseInSemester(String courseCode, String semesterCode) {
        HttpEntity<Void> request = getHttpRequestEntity();
        String courseUrl = "/courses/" + courseCode + "?sem=" + semesterCode;
        String response = restTemplate.exchange(resourceServerURL + courseUrl, HttpMethod.GET, request, String.class).getBody();

        ObjectMapper xmlMapper = new XmlMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            Entry<KosCourse> atomEntry = xmlMapper.readValue(response, new TypeReference<Entry<KosCourse>>() {});
            KosCourse kosCourse = atomEntry.getContent();
            return kosCourse;
        } catch (JsonProcessingException e) {
            throw new KosapiException("Failed to parse course: " + courseCode
                    + "\nError message:" + e.getMessage());
        }
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

    public List<KosCourse> getAllCoursesInSemester(String semesterCode){
        try {
            ArrayList<KosCourse> kosCourses = getAllCourseEntriesInSemester(semesterCode);
            return kosCourses;
        } catch (JsonProcessingException e) {
            throw new KosapiException("Failed to parse courses from semester: " + semesterCode
                    + "\nError message:" + e.getMessage());
        }
    }

    private ArrayList<KosCourse> getAllCourseEntriesInSemester(String semesterCode) throws JsonProcessingException {
        int offset = 0;
        ArrayList<KosCourse> result = new ArrayList<>();

        while (true){
            HttpEntity<Void> request = getHttpRequestEntity();
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

    public List<KosTeacher> getTeachersInCourse(String courseCode, String semesterCode){
        KosCourse kosCourse = getCourseInSemester(courseCode, semesterCode);
        KosCourseInstance instance = kosCourse.getInstance();
        TreeSet<TeacherLink> teachersLinks = new TreeSet<TeacherLink>(Arrays.asList(instance.getInstructors()));
        teachersLinks.addAll(Arrays.asList(instance.getLecturers()));
        ArrayList<KosTeacher> kosTeachers = new ArrayList<>();

        for (TeacherLink teacherLink : teachersLinks){
            try {
                kosTeachers.add(getKosTeacherFromTeacherLink(teacherLink));
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // TODO logging later
            }
        }

        return kosTeachers;
    }


    private KosTeacher getKosTeacherFromTeacherLink(TeacherLink teacherLink) throws JsonProcessingException {
        HttpEntity<Void> request = getHttpRequestEntity();
        String response = restTemplate.exchange(resourceServerURL + "/" + teacherLink.getUrl(), HttpMethod.GET, request, String.class).getBody();
        ObjectMapper xmlMapper = new XmlMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Entry<KosTeacher> atomEntry = xmlMapper.readValue(response, new TypeReference<Entry<KosTeacher>>() {
        });
        return atomEntry.getContent();
    }

    private HttpEntity<Void> getHttpRequestEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    /**
    List<KosStudent> getStudentsInKosCourse(KosCourse kosCourse){

    }

    List<KosParallel> getParallelsInKosCourse(){

    }

    List<KosTeacher> getTeachersInParallel(){

    }

    List<KosStudent> getStudentsInParallel(){

    }**/
}