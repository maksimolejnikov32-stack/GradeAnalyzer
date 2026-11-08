package gradeanalyzer.data;

import gradeanalyzer.model.GradeJournal;
import gradeanalyzer.model.Student;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class SyntheticDataGenerator {
    public static final List<String> DISCIPLINES = 
        List.of("Math", "Physics", "Programming", "Databases", "English");

    public GradeJournal generate(int studentCount, int semesters, long seed) {
        Random rnd = new Random(seed);
        GradeJournal journal = new GradeJournal(DISCIPLINES, semesters);

        for (int i = 0; i < studentCount; i++) {
            String id = String.format("S%03d", i + 1);
            String name = "Student_" + (i + 1);
            Student s = new Student(id, name, DISCIPLINES, semesters);

            double base = 2.5 + rnd.nextDouble() * 2.3;
            boolean risky = rnd.nextDouble() < 0.25;
            if (risky) base = 2.0 + rnd.nextDouble() * 1.2;

            for (int sem = 0; sem < semesters; sem++) {
                double trend = risky ? -0.25 * sem : 0.05 * sem;
                for (int d = 0; d < DISCIPLINES.size(); d++) {
                    if (rnd.nextDouble() < 0.03) continue; 
                    
                    double mean = base + trend + (rnd.nextDouble() - 0.5) * 1.2;
                    int grade = (int) Math.round(mean);
                    grade = Math.max(2, Math.min(5, grade));
                    s.setGrade(sem, d, (double) grade);
                }
            }
            journal.addStudent(s);
        }
        return journal;
    }

    public void exportToCsv(GradeJournal journal, Path path, boolean withLabel) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(path)) {
            StringBuilder h = new StringBuilder("id,name");
            for (int sem = 1; sem <= journal.getSemesters(); sem++)
                for (String d : journal.getDisciplines())
                    h.append(",S").append(sem).append('_').append(d);
            if (withLabel) h.append(",atRisk");
            w.write(h.toString()); w.newLine();

            for (Student s : journal.getStudents()) {
                StringBuilder line = new StringBuilder();
                line.append(s.getId()).append(',').append(s.getName());
                for (int sem = 0; sem < journal.getSemesters(); sem++)
                    for (int d = 0; d < journal.getDisciplines().size(); d++) {
                        line.append(',');
                        Double v = s.getGrade(sem, d);
                        if (v != null) line.append(v.intValue());
                    }
                if (withLabel) {
                    line.append(',');
                    line.append(Boolean.TRUE.equals(s.getLabeledAtRisk()) ? 1 : 0);
                }
                w.write(line.toString()); w.newLine();
            }
        }
    }
    
    public void labelRisk(GradeJournal journal) {
        for (Student s : journal.getStudents()) {
            double sum = 0; int n = 0;
            for (Double g : s.allGrades()) { sum += g; n++; }
            double avg = n == 0 ? 0 : sum / n;
            s.setLabeledAtRisk(avg < 3.0);
        }
    }
}