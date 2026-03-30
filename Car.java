public class Car {
    private String make;
    private String model;
    private String type;
    private String category;
    private double rentalCostPerDay;
    private String comfort;
    private int maxPassengers;
    private double mpg;

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
        return rentalCostPerDay * rentalDays;
    }

    public double calculateGasCost(double mileage, double gasPricePerGallon) {
        return (mileage / mpg) * gasPricePerGallon;
    }

    public double calculateTotalCost(int rentalDays, double mileage, double gasPricePerGallon) {
        return calculateRentalCost(rentalDays) + calculateGasCost(mileage, gasPricePerGallon);
    }

    @Override
    public String toString() {
        return make + " " + model;
    }
}