package gradeanalyzer.data;

import gradeanalyzer.model.GradeJournal;
import gradeanalyzer.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {
    private final DataValidator validator = new DataValidator();

    public GradeJournal load(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        if (lines.isEmpty()) throw new IOException("Пустой CSV");

        String header = lines.get(0);
        String[] cols = header.split(",", -1);

        List<String> disciplines = new ArrayList<>();
        List<Integer> semesters = new ArrayList<>();
        
        for (int c = 2; c < cols.length; c++) {
            String col = cols[c].trim();
            int us = col.indexOf('_');
            if (us <= 0) continue; 
            
            int sem = Integer.parseInt(col.substring(1, us));
            String disc = col.substring(us + 1);
            semesters.add(sem);
            if (!disciplines.contains(disc)) disciplines.add(disc);
        }
        
        int maxSem = semesters.stream().mapToInt(Integer::intValue).max().orElse(1);
        GradeJournal journal = new GradeJournal(disciplines, maxSem);

        for (int r = 1; r < lines.size(); r++) {
            String line = lines.get(r);
            if (line.isBlank()) continue;
            String[] cells = line.split(",", -1);
            
            String id = cells[0].trim();
            String name = cells[1].trim();
            Student s = new Student(id, name, disciplines, maxSem);
            
            for (int c = 2; c < cells.length; c++) {
                String colName = cols[c].trim();
                if (colName.equals("atRisk")) continue; 
                
                int us = colName.indexOf('_');
                int sem = Integer.parseInt(colName.substring(1, us));
                String discName = colName.substring(us + 1);
                int discIdx = disciplines.indexOf(discName);
                
                Double v = validator.validate(id, sem, discName, cells[c]);
                s.setGrade(sem - 1, discIdx, v);
            }
            journal.addStudent(s);
        }
        return journal;
    }
}