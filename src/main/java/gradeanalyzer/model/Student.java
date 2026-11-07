package gradeanalyzer.model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private final String id;
    private final String name;
    private final List<List<Double>> gradesBySemester;
    private final List<String> disciplines;
    private Boolean labeledAtRisk;

    public Student(String id, String name, List<String> disciplines, int semesters) {
        this.id = id;
        this.name = name;
        this.disciplines = new ArrayList<>(disciplines);
        this.gradesBySemester = new ArrayList<>();
        for (int i = 0; i < semesters; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < disciplines.size(); j++) row.add(null);
            gradesBySemester.add(row);
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<List<Double>> getGradesBySemester() { return gradesBySemester; }
    public int semesterCount() { return gradesBySemester.size(); }
    
    public void setGrade(int semester, int disciplineIdx, Double value) {
        gradesBySemester.get(semester).set(disciplineIdx, value);
    }

    public Double getGrade(int semester, int disciplineIdx) {
        return gradesBySemester.get(semester).get(disciplineIdx);
    }

    public Boolean getLabeledAtRisk() { return labeledAtRisk; }
    public void setLabeledAtRisk(Boolean labeledAtRisk) { this.labeledAtRisk = labeledAtRisk; }

    public List<Double> allGrades() {
        List<Double> result = new ArrayList<>();
        for (List<Double> sem : gradesBySemester)
            for (Double g : sem)
                if (g != null) result.add(g);
        return result;
    }

    public double semesterAverage(int semester) {
        List<Double> row = gradesBySemester.get(semester);
        double sum = 0; int n = 0;
        for (Double g : row) if (g != null) { sum += g; n++; }
        return n == 0 ? Double.NaN : sum / n;
    }

    @Override
    public String toString() { return id + " (" + name + ")"; }
}