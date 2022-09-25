import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class KMeans {

    private static final Random random = new Random();


    public static Map<Centroid, List<Flower>> fit(List<Flower> flowers,
                                                  int k, Distance distance, int maxIterations) {

        //generating centroids
        List<Centroid> centroids = generateNewCentroids(flowers, k);

        Map<Centroid, List<Flower>> clusters = new HashMap<>();
        Map<Centroid, List<Flower>> lastClusters = new HashMap<>();

//TODO add custom K
        for (int i = 0; i < maxIterations; i++) {
            boolean isLastIter = i == maxIterations - 1;

            for (Flower flower : flowers) {
                Centroid centroid = findNearestCentroid(flower, centroids, distance);
                getNewClustersList(clusters, flower, centroid);
            }
            boolean shouldEnd = isLastIter || clusters.equals(lastClusters);
            lastClusters = clusters;
            if (shouldEnd) {
                break;
            }
            centroids = relocateCentroids(clusters);
            clusters = new HashMap<>();

        }
        return lastClusters;

    }

    private static List<Centroid> relocateCentroids(Map<Centroid, List<Flower>> clusters) {
        return clusters.entrySet().stream().map(e -> average(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    private static Centroid average(Centroid centroid, List<Flower> flowers) {
        if (flowers == null || flowers.isEmpty()) {
            return centroid;
        }

        Map<String, Double> average = centroid.getCoordinates();
        flowers.stream().flatMap(e -> e.getFeatures().keySet().stream())
                .forEach(k -> average.put(k, 0.0));

        for (Flower flower : flowers) {
            flower.getFeatures().forEach(
                    (k, v) -> average.compute(k, (k1, currentValue) -> v + currentValue)
            );
        }

        average.forEach((k, v) -> average.put(k, v / flowers.size()));

        return new Centroid(average);
    }


    private static void getNewClustersList(Map<Centroid, List<Flower>> clusters, Flower flower, Centroid centroid) {
        //centroid as a key, remapping function
        clusters.compute(centroid, (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(flower);
            return list;
        });
    }

    private static Centroid findNearestCentroid(Flower flower, List<Centroid> centroids, Distance distance) {
        double minDistance = Double.MAX_VALUE;
        Centroid nearest = null;
        for (Centroid centroid : centroids) {
            double currentDistance = distance.getDistance(flower.getFeatures(), centroid.getCoordinates());
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                nearest = centroid;
            }
        }
        return nearest;
    }


    private static List<Centroid> generateNewCentroids(List<Flower> flowers, int k) {
        List<Centroid> centroids = new ArrayList<>();
        Map<String, Double> maxRange = new HashMap<>();
        Map<String, Double> minRange = new HashMap<>();

        for (Flower flower : flowers) {
            flower.getFeatures().forEach((key, value) -> {
                maxRange.compute(key, (k1, max) -> max == null || value > max ? value : max);
                minRange.compute(key, (k1, min) -> min == null || value < min ? value : min);
            });
        }
        //using mapper from Flower class and collector to set
        Set<String> attributes = flowers.stream().flatMap(e -> e.getFeatures().keySet().stream()).collect(Collectors.toSet());
        for (int i = 0; i < k; i++) {

            Map<String, Double> coordinates = new HashMap<>();
            for (String attribute : attributes) {
                double max = maxRange.get(attribute);
                double min = minRange.get(attribute);
                coordinates.put(attribute, random.nextDouble() * (max - min) + min);
            }
            centroids.add(new Centroid(coordinates));

        }
        return centroids;
    }

    private static ArrayList<Flower> parse(String path) {

        ArrayList<Flower> flowerArrayList = new ArrayList<>();
        Map<String, Double> flowersMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while (null != (line = reader.readLine())) {
                String[] strings = line.split(",");
                double[] values = new double[strings.length - 1];
                for (int i = 0; i < values.length; i++)
                    values[i] = Double.parseDouble(strings[i].trim());

                flowersMap.put(strings[strings.length-1],values[0]);
                flowerArrayList.add(new Flower(strings[strings.length - 1], flowersMap));
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
        System.out.println(flowerArrayList);
        return flowerArrayList;

    }
    
}

