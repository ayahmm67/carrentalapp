public class Car {
    private final String make;
    private final String model;
    private final String type;
    private final String category;
    private final double rentalCostPerDay;
    private final String comfort;
    private final int maxPassengers;
    private final double mpg;

    public Car(String make, String model, String type, String category,
               double rentalCostPerDay, String comfort, int maxPassengers, double mpg) {
        this.make = make;
        this.model = model;
        this.type = type;
        this.category = category;
        this.rentalCostPerDay = rentalCostPerDay;
        this.comfort = comfort;
        this.maxPassengers = maxPassengers;
        this.mpg = mpg;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public double getRentalCostPerDay() {
        return rentalCostPerDay;
    }

    public String getComfort() {
        return comfort;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public double getMpg() {
        return mpg;
    }

public double calculateRentalCost(int rentalDays) {
    if (rentalDays <= 0) {
        throw new IllegalArgumentException("Rental days must be greater than zero.");
    }
    return rentalCostPerDay * rentalDays;
}
public double calculateGasCost(double mileage, double gasPricePerGallon) {
    if (mileage < 0) {
        throw new IllegalArgumentException("Mileage cannot be negative.");
    }
    if (gasPricePerGallon <= 0) {
        throw new IllegalArgumentException("Gas price must be greater than zero.");
    }
    if (mpg <= 0) {
        throw new IllegalStateException("Car MPG must be greater than zero.");
    }
    return (mileage / mpg) * gasPricePerGallon;
}
public double calculateTotalCost(int rentalDays,
     double mileage, double gasPricePerGallon) {
    return calculateRentalCost(rentalDays)
     + calculateGasCost(mileage, gasPricePerGallon);
}

    @Override
    public String toString() {
        return make + " " + model;
    }
}