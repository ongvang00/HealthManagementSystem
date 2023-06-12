import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class HealthDataManager {
    private static final String CALORIE_INTAKE_FILE = "calorie_intake.txt";
    private static final String EXERCISE_ACTIVITY_FILE = "exercise_activity.txt";
    private static final String SLEEP_RECORD_FILE = "sleep_record.txt";

    public void addCalorieIntake(String username, String foodItem, int calories) {
        String entry = username + "," + foodItem + "," + calories + "," + getCurrentDate();
        writeDataToFile(CALORIE_INTAKE_FILE, entry);
    }

    public void addExerciseActivity(String username, String exerciseType, int duration, int caloriesBurned) {
        String entry = username + "," + exerciseType + "," + duration + "," + caloriesBurned + "," + getCurrentDate();
        writeDataToFile(EXERCISE_ACTIVITY_FILE, entry);
    }

    public void addSleepRecord(String username, String sleepStartTime, String sleepEndTime) {
        String entry = username + "," + sleepStartTime + "," + sleepEndTime + "," + getCurrentDate();
        writeDataToFile(SLEEP_RECORD_FILE, entry);
    }

    public List<String> getDailyCaloricBalance(String username) {
        List<String> entries = readDataFromFile(CALORIE_INTAKE_FILE);
        List<String> dailyCaloricBalance = new ArrayList<>();

        for (String entry : entries) {
            String[] parts = entry.split(",");
            if (parts.length == 4 && parts[0].equals(username)) {
                String date = parts[3];
                int calories = Integer.parseInt(parts[2]);
                String caloricBalance = date + " - Calories: " + calories;
                dailyCaloricBalance.add(caloricBalance);
            }
        }

        return dailyCaloricBalance;
    }

    public double getAverageHoursOfSleep(String username) {
        List<String> entries = readDataFromFile(SLEEP_RECORD_FILE);
        int totalHoursOfSleep = 0;
        int count = 0;

        for (String entry : entries) {
            String[] parts = entry.split(",");
            if (parts.length == 4 && parts[0].equals(username)) {
                String sleepStartTime = parts[1];
                String sleepEndTime = parts[2];
                int hoursOfSleep = calculateHoursOfSleep(sleepStartTime, sleepEndTime);
                totalHoursOfSleep += hoursOfSleep;
                count++;
            }
        }

        if (count > 0) {
            return (double) totalHoursOfSleep / count;
        } else {
            return 0;
        }
    }

    public List<String> getExerciseLog(String username) {
        List<String> entries = readDataFromFile(EXERCISE_ACTIVITY_FILE);
        List<String> exerciseLog = new ArrayList<>();

        for (String entry : entries) {
            String[] parts = entry.split(",");
            if (parts.length == 5 && parts[0].equals(username)) {
                String exerciseType = parts[1];
                int duration = Integer.parseInt(parts[2]);
                int caloriesBurned = Integer.parseInt(parts[3]);
                String date = parts[4];
                String logEntry = date + " - Exercise: " + exerciseType + ", Duration: " + duration + " min, Calories Burned: " + caloriesBurned;
                exerciseLog.add(logEntry);
            }
        }

        return exerciseLog;
    }

    public String getHealthSummary(String username) {
        List<String> entries = readDataFromFile(CALORIE_INTAKE_FILE);
        int totalCaloriesConsumed = 0;
        int totalCaloriesBurned = 0;
        int totalHoursOfSleep = 0;
        int count = 0;

        Map<String, Integer> exerciseSummary = new HashMap<>();

        for (String entry : entries) {
            String[] parts = entry.split(",");
            if (parts.length == 4 && parts[0].equals(username)) {
                int calories = Integer.parseInt(parts[2]);
                totalCaloriesConsumed += calories;
            }
        }

        entries = readDataFromFile(EXERCISE_ACTIVITY_FILE);
        for (String entry : entries) {
            String[] parts = entry.split(",");
            if (parts.length == 5 && parts[0].equals(username)) {
                int caloriesBurned = Integer.parseInt(parts[3]);
                totalCaloriesBurned += caloriesBurned;

                String exerciseType = parts[1];
                exerciseSummary.put(exerciseType, exerciseSummary.getOrDefault(exerciseType, 0) + 1);
            }
        }

        entries = readDataFromFile(SLEEP_RECORD_FILE);
        for (String entry : entries) {
            String[] parts = entry.split(",");
            if (parts.length == 4 && parts[0].equals(username)) {
                String sleepStartTime = parts[1];
                String sleepEndTime = parts[2];
                int hoursOfSleep = calculateHoursOfSleep(sleepStartTime, sleepEndTime);
                totalHoursOfSleep += hoursOfSleep;
                count++;
            }
        }

        StringBuilder summary = new StringBuilder();
        summary.append("Calories Consumed: ").append(totalCaloriesConsumed).append("\n");
        summary.append("Calories Burned: ").append(totalCaloriesBurned).append("\n");
        summary.append("Total Hours of Sleep: ").append(totalHoursOfSleep).append("\n");

        if (count > 0) {
            double averageHoursOfSleep = (double) totalHoursOfSleep / count;
            summary.append("Average Hours of Sleep: ").append(averageHoursOfSleep).append("\n");
        }

        if (!exerciseSummary.isEmpty()) {
            summary.append("Exercise Summary:").append("\n");
            for (Map.Entry<String, Integer> entry : exerciseSummary.entrySet()) {
                summary.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" times").append("\n");
            }
        }

        return summary.toString();
    }

    private String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

    private List<String> readDataFromFile(String fileName) {
        List<String> entries = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                entries.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
        return entries;
    }

    private void writeDataToFile(String fileName, String entry) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(entry + "\n");
        } catch (IOException e) {
            System.out.println("Error: Unable to write to file.");
        }
    }

    private int calculateHoursOfSleep(String sleepStartTime, String sleepEndTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(sleepStartTime, formatter);
        LocalTime endTime = LocalTime.parse(sleepEndTime, formatter);

        int hours = endTime.getHour() - startTime.getHour();
        int minutes = endTime.getMinute() - startTime.getMinute();

        if (minutes < 0) {
            hours--;
            minutes += 60;
        }

        return hours;
    }
}
