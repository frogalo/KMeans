import java.util.Map;
import java.util.Objects;

public class Flower {
    private final String name;
    private final Map<String, Double> features;

    public Flower(String name, Map<String, Double> features) {
        this.name = name;
        this.features = features;
    }

    public String getName() {
        return name;
    }

    public Map<String, Double> getFeatures() {
        return features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flower flower = (Flower) o;
        return Objects.equals(name, flower.name) && Objects.equals(features, flower.features);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, features);
    }

    @Override
    public String toString() {
        return "Flower{" +
                "name='" + name + '\'' +
                ", features=" + features +
                '}';
    }
}
