import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class JsonCarLoader{

    public static List<Car> loadCars(String filePath) throws IOException {
        List<Car> validCars = new ArrayList<>();
        Gson gson = new Gson();

        try (Reader reader = Files.newBufferedReader(Path.of(filePath))) {
            CarData[] loadedCars = gson.fromJson(reader, CarData[].class);

            if (loadedCars == null) {
                return validCars;
            }

            for (CarData data : loadedCars) {
                if (data != null && isValidCarData(data)) {
                    validCars.add(new Car(
                            data.make,
                            data.model,
                            data.type,
                            data.category,
                            data.rentalCostPerDay,
                            data.comfort,
                            data.maxPassengers,
                            data.mpg
                    ));
                }
            }
        } catch (JsonSyntaxException e) {
          JOptionPane.showMessageDialog(
    null,
    "Invalid data file. System will run with limited functionality.",
    "Warning",
    JOptionPane.WARNING_MESSAGE
); 
              }

        return validCars;
    }
    private static boolean isTypeCategoryMatch(String type, String category) {
    if (type == null || category == null) return false;

    switch (type) {
        case "Coupe":
            return category.equals("Economy");

        case "Sedan":
        case "Hybrid":
            return category.equals("Intermediate");

        case "Truck":
        case "Crossover":
        case "SUV":
            return category.equals("Standard");

        case "Van":
        case "Minivan":
            return category.equals("Van");

        default:
            return false;
    }
}

private static boolean isCategoryDetailsValid(CarData car) {
    switch (car.category) {
        case "Economy":
            return car.rentalCostPerDay == 45
                    && car.maxPassengers == 4
                    && car.comfort.equals("Poor");

        case "Intermediate":
            return car.rentalCostPerDay == 50
                    && car.maxPassengers == 4
                    && car.comfort.equals("Medium");

        case "Standard":
            return car.rentalCostPerDay == 55
                    && car.maxPassengers == 5
                    && car.comfort.equals("Good");

        case "Van":
            return car.rentalCostPerDay == 70
                    && car.maxPassengers == 7
                    && car.comfort.equals("Medium");

        default:
            return false;
    }
}


    private static boolean isValidCarData(CarData car) {
    return isNonEmpty(car.make)
            && isNonEmpty(car.model)
            && isValidType(car.type)
            && isValidCategory(car.category)
            && isTypeCategoryMatch(car.type, car.category)
            && isValidComfort(car.comfort)
            && car.rentalCostPerDay > 0
            && car.maxPassengers > 0
            && car.mpg > 0
            && isCategoryDetailsValid(car);
}

    private static boolean isNonEmpty(String value) {
        return value != null && !value.isBlank();
    }

    private static boolean isValidType(String type) {
        if (type == null) return false;

        return type.equals("SUV")
                || type.equals("Crossover")
                || type.equals("Sedan")
                || type.equals("Truck")
                || type.equals("Coupe")
                || type.equals("Hybrid")
                || type.equals("Van")
                || type.equals("Minivan");
    }

    private static boolean isValidCategory(String category) {
        if (category == null) return false;

        return category.equals("Economy")
                || category.equals("Intermediate")
                || category.equals("Standard")
                || category.equals("Van");
    }

    private static boolean isValidComfort(String comfort) {
        if (comfort == null) return false;

        return comfort.equals("Poor")
                || comfort.equals("Medium")
                || comfort.equals("Good");
    }
}