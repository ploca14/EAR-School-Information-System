package cz.cvut.kbss.ear.project.rest;

import cz.cvut.kbss.ear.project.kosapi.entities.KosCourse;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.enums.CourseCompletionType;
import cz.cvut.kbss.ear.project.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest extends BaseControllerTestRunner{

    @Mock
    private CourseService courseService;

    @Mock
    private CourseInSemesterService courseInSemesterService;

    @Mock
    private SemesterService semesterService;

    @Mock
    private ParallelService parallelService;

    @Mock
    private KosapiService kosapiService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    public void setup(){
        super.setUp(courseController);
    }

    @Test
    public void createCourse_createCustomCourse_courseCreated() throws Exception {
        final Course toCreate = new Course();
        toCreate.setName("Custom course");
        toCreate.setCredits(4);
        toCreate.setDescription("Custom course desc");
        toCreate.setCompletionType(CourseCompletionType.KZ);
        toCreate.setCode("CUSTOMCODE");

        mockMvc.perform(post("/api/courses").content(toJson(toCreate)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        final ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        verify(courseService).persist(captor.capture());
        assertEquals(toCreate, captor.getValue());
    }

    @Test
    public void createCourseFromKos_createEAR_courseCreated() throws Exception {
        final String code = "B6B36EAR";
        KosCourse kosCourseMock = mock(KosCourse.class);
        Course courseMock = mock(Course.class);
        when(kosapiService.getCourse(code)).thenReturn(kosCourseMock);
        when(courseService.createNewCourse(kosCourseMock)).thenReturn(courseMock);

        mockMvc.perform(post("/api/courses/kos").content(toJson(code)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        final ArgumentCaptor<KosCourse> captor = ArgumentCaptor.forClass(KosCourse.class);
        verify(courseService).createNewCourse(captor.capture());
        assertEquals(kosCourseMock, captor.getValue());
    }
}
