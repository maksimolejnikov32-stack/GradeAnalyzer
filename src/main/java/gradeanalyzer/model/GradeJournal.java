package gradeanalyzer.model;

import java.util.ArrayList;
import java.util.List;

public class GradeJournal {
    private final List<String> disciplines;
    private final int semesters;
    private final List<Student> students;

    public GradeJournal(List<String> disciplines, int semesters) {
        this.disciplines = new ArrayList<>(disciplines);
        this.semesters = semesters;
        this.students = new ArrayList<>();
    }

    public List<String> getDisciplines() { return disciplines; }
    public int getSemesters() { return semesters; }
    public List<Student> getStudents() { return students; }
    public void addStudent(Student s) { students.add(s); }
    
    public int disciplineIndex(String name) { return disciplines.indexOf(name); }
}