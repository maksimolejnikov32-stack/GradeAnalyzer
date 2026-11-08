package gradeanalyzer.stats;

import gradeanalyzer.model.GradeJournal;
import gradeanalyzer.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticsCalculator {

    public double mean(List<Double> values) {
        if (values.isEmpty())
            return Double.NaN;
        double s = 0;
        for (double v : values)
            s += v;
        return s / values.size();
    }

    public double median(List<Double> values) {
        if (values.isEmpty())
            return Double.NaN;
        double[] arr = values.stream().mapToDouble(Double::doubleValue).toArray();
        Arrays.sort(arr);
        int n = arr.length;
        return n % 2 == 1 ? arr[n / 2] : (arr[n / 2 - 1] + arr[n / 2]) / 2.0;
    }

    public double stdev(List<Double> values) {
        int n = values.size();
        if (n < 2)
            return 0.0;
        double m = mean(values);
        double ss = 0;
        for (double v : values)
            ss += (v - m) * (v - m);
        return Math.sqrt(ss / (n - 1));
    }

    public double correlation(double[] x, double[] y) {
        List<double[]> pairs = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            if (!Double.isNaN(x[i]) && !Double.isNaN(y[i]))
                pairs.add(new double[] { x[i], y[i] });
        }
        int n = pairs.size();
        if (n < 2)
            return Double.NaN;
        double mx = 0, my = 0;
        for (double[] p : pairs) {
            mx += p[0];
            my += p[1];
        }
        mx /= n;
        my /= n;
        double cov = 0, sx = 0, sy = 0;
        for (double[] p : pairs) {
            double dx = p[0] - mx, dy = p[1] - my;
            cov += dx * dy;
            sx += dx * dx;
            sy += dy * dy;
        }
        if (sx == 0 || sy == 0)
            return Double.NaN;
        return cov / Math.sqrt(sx * sy);
    }

    public List<Double> studentAverages(GradeJournal journal) {
        List<Double> res = new ArrayList<>();
        for (Student s : journal.getStudents())
            res.add(mean(s.allGrades()));
        return res;
    }
}