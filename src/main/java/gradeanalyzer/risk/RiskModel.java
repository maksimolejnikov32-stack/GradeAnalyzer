package gradeanalyzer.risk;

import gradeanalyzer.model.GradeJournal;
import gradeanalyzer.model.Student;

public class RiskModel {
    public static final double THRESHOLD_AVG = 3.0;
    public static final double THRESHOLD_TREND = -0.5;

    public boolean predictAtRisk(Student s) {
        var grades = s.allGrades();
        if (grades.isEmpty()) return true;
        
        double sum = 0;
        for (double g : grades) sum += g;
        double avg = sum / grades.size();

        double first = s.semesterAverage(0);
        double last = s.semesterAverage(s.semesterCount() - 1);
        double trend = (Double.isNaN(first) || Double.isNaN(last)) ? 0 : last - first;

        return avg < THRESHOLD_AVG || trend < THRESHOLD_TREND;
    }

    public double[] evaluate(GradeJournal journal) {
        int tp = 0, fp = 0, fn = 0, tn = 0;
        for (Student s : journal.getStudents()) {
            if (s.getLabeledAtRisk() == null) continue;
            boolean actual = s.getLabeledAtRisk();
            boolean predicted = predictAtRisk(s);
            if (predicted && actual) tp++;
            else if (predicted) fp++;
            else if (actual) fn++;
            else tn++;
        }
        double precision = (tp + fp == 0) ? 0 : (double) tp / (tp + fp);
        double recall = (tp + fn == 0) ? 0 : (double) tp / (tp + fn);
        double f1 = (precision + recall == 0) ? 0 : 2 * precision * recall / (precision + recall);
        return new double[]{precision, recall, f1};
    }
}