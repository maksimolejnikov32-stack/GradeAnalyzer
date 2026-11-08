package gradeanalyzer.data;

import java.util.logging.Logger;

public class DataValidator {
    private static final Logger LOG = Logger.getLogger(DataValidator.class.getName());
    
    public Double validate(String studentId, int semester, String discipline, String raw) {
        if (raw == null || raw.isBlank()) {
            LOG.warning("Пропуск: student=" + studentId + " sem=" + semester + " disc=" + discipline);
            return null;
        }
        try {
            double v = Double.parseDouble(raw.trim());
            if (v < 2.0 || v > 5.0) {
                LOG.warning("Выброс (вне 2..5): student=" + studentId + " value=" + v);
                return null;
            }
            return v;
        } catch (NumberFormatException e) {
            LOG.warning("Нечисловое значение: student=" + studentId + " value='" + raw + "'");
            return null;
        }
    }
}