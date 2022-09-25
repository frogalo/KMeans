import java.util.Map;

public class EuclideanDistance implements Distance {


    @Override
    public double getDistance(Map<String, Double> f1, Map<String, Double> f2) {
        double sum = 0;
        for (String key : f1.keySet()) {
            Double v1 = f1.get(key);
            Double v2 = f2.get(key);

            if (v1 != null && v2 != null) {
                //sum of squared distance between entries
                sum += Math.pow(v1 - v2, 2);
            }
        }
        //calculate actual distance
        return Math.sqrt(sum);
    }
}
