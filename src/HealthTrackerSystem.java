import java.util.Scanner;
import java.util.*;

public class HealthTrackerSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser;
    private static UserManagement userManagement;
    private static HealthDataManager healthDataManager;

    public static void main(String[] args) {
        userManagement = new UserManagement();
        healthDataManager = new HealthDataManager();

        showWelcomeMessage();

        boolean exit = false;
        while (!exit) {
            try {
                showMainMenu();
                int choice = getUserChoice();

                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        if (currentUser != null) {
                            enterHealthData();
                        } else {
                            System.out.println("Please log in first.");
                        }
                        break;
                    case 4:
                        if (currentUser != null) {
                            showHealthDataAnalysis();
                        } else {
                            System.out.println("Please log in first.");
                        }
                        break;
                    case 5:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine();
            }
        }

        System.out.println("Thank you for using the Health Tracker System. Goodbye!");
    }

    private static void showWelcomeMessage() {
        System.out.println("Welcome to the Health Tracker System!");
    }

    private static void showMainMenu() {
        System.out.println("========== Main Menu ==========");
        System.out.println("1. Create User");
        System.out.println("2. Log In");
        System.out.println("3. Enter Health Data");
        System.out.println("4. Show Health Data Analysis");
        System.out.println("5. Exit");
    }

    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    private static void createUser() {
        scanner.nextLine();
        System.out.print("Enter a unique username: ");
        String username = scanner.nextLine();

        if (userManagement.createUser(username)) {
            System.out.println("User created successfully!");
        } else {
            System.out.println("Username already exists. Please choose a different username.");
        }
    }

    private static void login() {
        scanner.nextLine();
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        if (userManagement.login(username)) {
            System.out.println("Login successful!");
            currentUser = userManagement.getCurrentUser();
        } else {
            System.out.println("Invalid username. Please try again.");
        }
    }

    private static void enterHealthData() {
        boolean backToMainMenu = false;
        while (!backToMainMenu) {
            showHealthDataMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    enterCalorieIntake();
                    break;
                case 2:
                    enterExerciseActivity();
                    break;
                case 3:
                    enterSleepRecord();
                    break;
                case 4:
                    backToMainMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void showHealthDataMenu() {
        System.out.println("======== Health Data Menu ========");
        System.out.println("1. Enter Calorie Intake");
        System.out.println("2. Enter Exercise Activity");
        System.out.println("3. Enter Sleep Record ");
        System.out.println("4. Back to Main Menu");
    }

    private static void enterCalorieIntake() {
        scanner.nextLine();
        System.out.print("Enter the food item: ");
        String foodItem = scanner.nextLine();

        System.out.print("Enter the caloric value: ");
        int calories = scanner.nextInt();

        healthDataManager.addCalorieIntake(currentUser.getUsername(), foodItem, calories);
        System.out.println("Calorie intake recorded successfully!");
    }

    private static void enterExerciseActivity() {
        scanner.nextLine();
        System.out.print("Enter the type of exercise: ");
        String exerciseType = scanner.nextLine();

        System.out.print("Enter the duration in minutes: ");
        int duration = scanner.nextInt();

        System.out.print("Enter the estimated calories burned: ");
        int caloriesBurned = scanner.nextInt();

        healthDataManager.addExerciseActivity(currentUser.getUsername(), exerciseType, duration, caloriesBurned);
        System.out.println("Exercise activity recorded successfully!");
    }

    private static void enterSleepRecord() {
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter the sleep start time (HH:mm): ");
        String sleepStartTime = scanner.nextLine();

        System.out.print("Enter the sleep end time (HH:mm): ");
        String sleepEndTime = scanner.nextLine();

        healthDataManager.addSleepRecord(currentUser.getUsername(), sleepStartTime, sleepEndTime);
        System.out.println("Sleep record recorded successfully!");
    }

    private static void showHealthDataAnalysis() {
        boolean backToMainMenu = false;
        while (!backToMainMenu) {
            showAnalysisMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    showDailyCaloricBalance();
                    break;
                case 2:
                    showSleepAnalysis();
                    break;
                case 3:
                    showExerciseLog();
                    break;
                case 4:
                    showHealthSummary();
                    break;
                case 5:
                    backToMainMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void showAnalysisMenu() {
        System.out.println("======= Analysis Menu =======");
        System.out.println("1. Show Daily Caloric Balance");
        System.out.println("2. Show Sleep Analysis");
        System.out.println("3. Show Exercise Log");
        System.out.println("4. Show Health Summary");
        System.out.println("5. Back to Main Menu");
    }

    private static void showDailyCaloricBalance() {
        List<String> dailyCaloricBalance = healthDataManager.getDailyCaloricBalance(currentUser.getUsername());
        System.out.println("=== Daily Caloric Balance ===");
        for (String entry : dailyCaloricBalance) {
            System.out.println(entry);
        }
    }

    private static void showSleepAnalysis() {
        double averageHoursOfSleep = healthDataManager.getAverageHoursOfSleep(currentUser.getUsername());
        System.out.println("=== Sleep Analysis ===");
        System.out.printf("Average hours of sleep per day: %.2f%n", averageHoursOfSleep);
    }

    private static void showExerciseLog() {
        List<String> exerciseLog = healthDataManager.getExerciseLog(currentUser.getUsername());
        System.out.println("=== Exercise Log ===");
        for (String entry : exerciseLog) {
            System.out.println(entry);
        }
    }

    private static void showHealthSummary() {
        try {
            String healthSummary = healthDataManager.getHealthSummary(currentUser.getUsername());
            System.out.println("=== Health Summary ===");
            System.out.println(healthSummary);
        } catch (NullPointerException e) {
            System.out.println("Error: User's health data not found.");
        }
    }
}

