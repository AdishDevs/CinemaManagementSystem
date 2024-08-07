import java.util.InputMismatchException;
import java.util.Scanner;

public class CinemaManagement {
    // Number of seats in the cinema
    private static final int NUM_SEATS = 48;

    // Number of rows and seats per row
    private static final int NUM_ROWS = 3;
    private static final int SEATS_PER_ROW = 16;
    
    // Array created to store seats available
    private static int[] seats = new int[NUM_SEATS];

    // Array created to store tickets
    private static Ticket[] tickets = new Ticket[NUM_SEATS];
    private static int ticketCount = 0;

    public static void main(String[] args) {
        System.out.println("Welcome to The London Lumiere");

        // Initialize all seats to be available
        for (int i = 0; i < NUM_SEATS; i++) {
            seats[i] = 0;
        }
                
        userMenu();//call user manu
    }

    //User menu
    public static void userMenu() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int option;


            do {
                //Display menu options
                System.out.println("------------------------------------------------");
                System.out.println("Please select an option:");
                System.out.println("1) Buy a ticket");
                System.out.println("2) Cancel ticket");
                System.out.println("3) See seating plan");
                System.out.println("4) Find first seat available");
                System.out.println("5) Print tickets information and total price");
                System.out.println("6) Search ticket");
                System.out.println("7) Sort tickets by price");
                System.out.println("8) Exit");
                System.out.println("------------------------------------------------");
                System.out.print("Select option: ");

                //Validate input
                while (!scanner.hasNextInt()) {
                    System.out.print("Invalid input, Please enter a valid number. ");
                    scanner.next();
                }

                option = scanner.nextInt();

                //Menu options
                switch (option) {
                    case 1:
                        buyTicket(scanner);
                        break;
                    case 2:
                        cancelTicket(scanner);
                        break;
                    case 3:
                        seeSeatingPlan();
                        break;
                    case 4:
                        findFirstSeatAvailable();
                        break;
                    case 5:
                        printTicketsInfo();
                        break;
                    case 6:
                        searchTicket(scanner);
                        break;
                    case 7:
                        sortTickets();
                        break;
                    case 8:
                        System.out.println("Exiting program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please select a number between 1 and 8.");

                }
                if(option==8){
                    break;
                }
            }while(option != 8);
        scanner.close();



    }

    //Buy a ticket
    public static void buyTicket(java.util.Scanner scanner) {
        int row = 0;
        int seat = 0;

        while(true){
            try{
                System.out.print("Enter row number (1-3): ");
                row = scanner.nextInt();

                // Validate row number
                if (row < 1 || row > NUM_ROWS) {
                    System.out.println("Invalid row number. ");
                    continue; 
                }
                break; 
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value for the row number.");
                scanner.nextLine(); // To Consume the invalid input
            }
        }

        
        while(true){
            try{
                System.out.print("Enter seat number (1-16): ");
                seat = scanner.nextInt();

                // Validate seat number
                if (seat < 1 || seat > SEATS_PER_ROW) {
                    System.out.println("Invalid seat number. ");
                    continue; 
                }
                break; // Exit the loop when a valid seat number is entered
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value for the seat number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        // Calculate the index in the array
        int index = (row - 1) * SEATS_PER_ROW + (seat - 1);

        // Check if the seat is available
        if (seats[index] == 0) {
            seats[index] = 1;  // Mark the seat as sold

            // Get person information
            scanner.nextLine(); // newline
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter surname: ");
            String surname = scanner.nextLine();
            
            // For valid email
            String email = null;
            while (true) {
                System.out.print("Enter email: ");
                email = scanner.nextLine();
                try {
                    new Person(name, surname, email); // Use the constructor to validate email
                    break; // Exit the loop if the email is valid
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage()); // Print the exception message
                }
            }

            Person person = new Person(name, surname, email);


            double price = 0;
            if(row == 1){
                price = 12.0;
            }else if(row == 2){
                price = 10.0;
            }else{
                price = 8.0;
            }


            // Create and store the ticket
            tickets[ticketCount++] = new Ticket(row, seat, price, person);

            System.out.println("The seat has been booked.");
        } else {
            System.out.println("This seat is not available.");
        }
    }


    // Cancel a ticket
    public static void cancelTicket(java.util.Scanner scanner) {
        int row = 0;
        int seat = 0;

        while (true) {
            try {
                System.out.print("Enter row number (1-3): ");
                row = scanner.nextInt();

                if (row < 1 || row > NUM_ROWS) {
                    System.out.println("Invalid row number. Please try again.");
                    continue;
                }
                break; // Exit the loop when a valid row number is entered
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value for the row number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        while (true) {
            try {
                System.out.print("Enter seat number (1-16): ");
                seat = scanner.nextInt();
    
                if (seat < 1 || seat > SEATS_PER_ROW) {
                    System.out.println("Invalid seat number. Please try again.");
                    continue;
                }
                break; // Exit the loop when a valid seat number is entered
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value for the seat number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

    // Calculate the index in the array
    int index = (row - 1) * SEATS_PER_ROW + (seat - 1);

    // Check if the seat is unavailable (sold)
    if (seats[index] == 1) {
        seats[index] = 0; // Mark the seat as available
        System.out.println("The seat has been cancelled.");


        // Remove the ticket from the tickets array
        for (int i = 0; i < ticketCount; i++) {
            if (tickets[i].getRow() == row && tickets[i].getSeat() == seat) {
                // Shift subsequent tickets leftward
                for (int j = i; j < ticketCount - 1; j++) {
                    tickets[j] = tickets[j + 1];
                }
                tickets[--ticketCount] = null; // Reduce ticket count and nullify last element
                break;
            }
        }

    } else {
        System.out.println("This seat is already available.");
    }
}


    // Print seating area
    public static void seeSeatingPlan() {
        System.out.println();
        System.out.println("****************************************");
        System.out.println("*               SCREEN                 *");
        System.out.println("****************************************");
        System.out.println();

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int seat = 0; seat < SEATS_PER_ROW / 2; seat++) {
                int index = row * SEATS_PER_ROW + seat;
                if (seats[index] == 0) {
                    System.out.print(" O");
                } else {
                    System.out.print(" X");
                }
            }
            System.out.print("     ");
            for (int seat = SEATS_PER_ROW / 2; seat < SEATS_PER_ROW; seat++) {
                int index = row * SEATS_PER_ROW + seat;
                if (seats[index] == 0) {
                    System.out.print(" O");
                } else {
                    System.out.print(" X");
                }
            }
            System.out.println();
        }
    }    
         
    // Find the first available seat
    public static void findFirstSeatAvailable() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int seat = 0; seat < SEATS_PER_ROW; seat++) {
                int index = row * SEATS_PER_ROW + seat;
                if (seats[index] == 0) {
                    System.out.println("First available seat is at row " + (row + 1) + ", seat " + (seat + 1));
                    return;
                }
            }
        }
        System.out.println("No available seats.");
    }

    // Print ticket information and total price
    public static void printTicketsInfo() {
        double totalPrice = 0.0;
        for (int i = 0; i < ticketCount; i++) {
            tickets[i].printInfo();
            totalPrice += tickets[i].getPrice();
        }
        System.out.println("Total price: $" + totalPrice);
    }

    // Search for a ticket
    public static void searchTicket(Scanner scanner) {
        int row =0;
        int seat =0;

        while(true){
            try{
                System.out.print("Enter row number (1-3): ");
                row = scanner.nextInt();

                if (row < 1 || row > NUM_ROWS) {
                    System.out.println("Invalid row number. Please try again.");
                    continue; // Prompt again
                }
                break; // Valid input, exit loop
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value for the row number.");
                scanner.next(); // Consume the invalid input
            }
        }
            
        while(true){
            try{
                System.out.print("Enter seat number (1-16): ");
            seat = scanner.nextInt();

            if (seat < 1 || seat > SEATS_PER_ROW) {
                System.out.println("Invalid seat number. Please try again.");
                continue; // Prompt again
            }
            break; // Valid input, exit loop
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric value for the seat number.");
            scanner.next(); // Consume the invalid input
            }
        }

        // Calculate the index in the array
        int index = (row - 1) * SEATS_PER_ROW + (seat - 1);

        // Check if the seat is sold
        if (seats[index] == 1) {
            // Find and print the ticket information
        for (int i = 0; i < ticketCount; i++) {
            if (tickets[i].getRow() == row && tickets[i].getSeat() == seat) {
                tickets[i].printInfo();
                return;
            }
        }
        System.out.println("Ticket not found.");
    } else {
        System.out.println("This seat is available.");
    }
}
        
        

    // Sort tickets by price and print information
    public static void sortTickets() {
        // Sort tickets using a simple sorting algorithm
        for (int i = 0; i < ticketCount - 1; i++) {
            for (int j = 0; j < ticketCount - i - 1; j++) {
                if (tickets[j].getPrice() > tickets[j + 1].getPrice()) {
                    // Swap tickets[j] and tickets[j + 1]
                    Ticket temp = tickets[j];
                    tickets[j] = tickets[j + 1];
                    tickets[j + 1] = temp;
                }
            }
        }

        //Print sorted tickets
        System.out.println("Tickets sorted by price (ascending):");
        for (int i = 0; i < ticketCount; i++) {
            tickets[i].printInfo();
        }
    }
}
