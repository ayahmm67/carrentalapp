import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonCarLoader {

    public static List<Car> loadCars(String filePath) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);

        List<Car> cars = new ArrayList<>();

        Pattern objectPattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
        Matcher objectMatcher = objectPattern.matcher(json);

        while (objectMatcher.find()) {
            String carObject = objectMatcher.group(1);

            String make = extractString(carObject, "make");
            String model = extractString(carObject, "model");
            String type = extractString(carObject, "type");
            String category = extractString(carObject, "category");
            double rentalCostPerDay = extractDouble(carObject, "rentalCostPerDay");
            String comfort = extractString(carObject, "comfort");
            int maxPassengers = extractInt(carObject, "maxPassengers");
            double mpg = extractDouble(carObject, "mpg");

            Car car = new Car(make, model, type, category, rentalCostPerDay, comfort, maxPassengers, mpg);
            cars.add(car);
        }

        return cars;
    }

    private static String extractString(String jsonObject, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(jsonObject);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static double extractDouble(String jsonObject, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)");
        Matcher matcher = pattern.matcher(jsonObject);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return 0.0;
    }

    private static int extractInt(String jsonObject, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*([0-9]+)");
        Matcher matcher = pattern.matcher(jsonObject);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }
}