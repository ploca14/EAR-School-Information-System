package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.SemesterDao;
import cz.cvut.kbss.ear.project.exception.SemesterException;
import cz.cvut.kbss.ear.project.model.Semester;
import cz.cvut.kbss.ear.project.model.enums.SemesterState;
import cz.cvut.kbss.ear.project.model.enums.SemesterType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SemesterService {

    private final SemesterDao semesterDao;

    public SemesterService(SemesterDao semesterDao) {
        this.semesterDao = semesterDao;
    }

    @Transactional
    public void addNewSemester(String code, String year, SemesterType semesterType){
        if (!isCodeUnique(code)){
            throw new SemesterException("Semester with this code already exists");
        }

        if (existsSemesterWithSameType(semesterType, year)){
            throw new SemesterException("Semester with type: " + semesterType.toString() + "already exists in year: " + year);
        }

        Semester semester = new Semester();
        semester.setCode(code);
        semester.setState(SemesterState.PREPARATION);
        semester.setYear(year);
        semester.setType(semesterType);

        semesterDao.persist(semester);
    }

    @Transactional
    public void makeSemesterCurrent(Semester newSemester){
        if (newSemester.getState() == SemesterState.CURRENT){
            throw new SemesterException("Semester: " + newSemester.getCode() + " is already CURRENT.");
        }

        if (newSemester.getState() == SemesterState.ARCHIVED){
            throw new SemesterException("Semester: " + newSemester.getCode() + " is ARCHIVED." +
                    " Cannot make ARCHIVED semester CURRENT");
        }

        List<Semester> semesters = semesterDao.findByState(SemesterState.CURRENT);
        if (semesters != null && semesters.size() != 0){
            Semester currentSemester = semesters.get(0);
            currentSemester.setState(SemesterState.ARCHIVED);
            semesterDao.update(currentSemester);
        }

        newSemester.setState(SemesterState.CURRENT);
        semesterDao.update(newSemester);
    }

    private boolean isCodeUnique(String code){
        return semesterDao.findByCode(code) == null;
    }

    private boolean existsSemesterWithSameType(SemesterType semesterType, String year){
        for (Semester semester : semesterDao.findByYear(year)){
            if (semester.getType() == semesterType) return true;
        }

        return false;
    }
}
