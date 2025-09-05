import java.util.*;
class BurgerShop {

    final static double BURGERPRICE = 500;
    public static final int PREPARING = 0;
    public static final int DELIVERED = 1;
    public static final int CANCEL = 2;


    public static String[] orderIds = new String [] {};
    public static String[] customerIds = new String [] {};
    public static String[] customerNames = new String [] {};
    public static int[] quantities = new int [] {};
    public static int[] statuses = new int [] {};
    public static int orderCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            clearConsole();
            System.out.println("-------------------------------------------------------------------");
            System.out.println("|                         iHungry Burger                          |");
            System.out.println("-------------------------------------------------------------------");
            System.out.println();
            System.out.print("[1] Place Order");
            System.out.println("        [2] Search Best Customer");
            System.out.print("[3] Search Order");
            System.out.println("       [4] Search Customer");
            System.out.print("[5] View Orders");
            System.out.println("        [6] Update Order Details");
            System.out.println("[7] Exit");
            System.out.println();
            System.out.print("Enter an option to continue > ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    placeOrder(scanner);
                    break;
                case 2:
                    searchBestCustomer(scanner);
                    break;
                case 3:
                    searchOrder(scanner);
                    break;
                case 4:
                    searchCustomer(scanner);
                    break;
                case 5:
                    viewOrders(scanner);
                    break;
                case 6:
                    updateOrderDetails(scanner);
                    break;
                case 7:
                    exit();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    pause(scanner);
            }
        }
    }

    public static void placeOrder(Scanner scanner) {
        boolean placingAnother = true;
        while (placingAnother) {
            clearConsole();
            System.out.println("-------------------------------------------------------------------");
            System.out.println("|                         Place Order                             |");
            System.out.println("-------------------------------------------------------------------");
            
            String orderId = generateOrderId();
            System.out.println();
            System.out.println("ODRDER ID - "+ orderId);
            System.out.println("================");
            System.out.println();

            String custId = getValidCustomerId(scanner);

            String custName = getCustomerName(scanner, custId);

            int qty = getValidQuantity(scanner);

            int status = PREPARING;

            double total = qty*BURGERPRICE;
            System.out.println("Total Value - " + total);

            System.out.print("         Are you confirm order - ");
            String confirm = scanner.nextLine().toUpperCase();
            while (!confirm.equals("Y") && !confirm.equals("N")){
                System.out.print("Invalid input. Enter Y/y or N/n - ");
                confirm = scanner.nextLine().toUpperCase();
            }
            if (confirm.equals("Y") || confirm.equals("y")) {
                if (orderCount<orderIds.length) {
                    orderIds[orderCount] = orderId;
                    customerIds[orderCount] = custId;
                    customerNames[orderCount] = custName;
                    quantities[orderCount] = qty;
                    statuses[orderCount] = status;
                    orderCount++;
                    System.out.println();
                    System.out.println("         Your order is enter to the system successfully...");
                } else {
                    System.out.println("Order capacity reached. Cannot place more orders.");
                }
            } else {
                System.out.println("Order not placed.");
            }
			System.out.println();
            System.out.print("Do you want to place another order (Y/N): ");
            String another = scanner.nextLine().toUpperCase();
            while (!another.equals("Y") && !another.equals("N")) {
                System.out.print("Invalid input. Enter Y or N: ");
                another = scanner.nextLine().toUpperCase();
            }
            placingAnother = another.equals("Y");
        }
    }

    public static String generateOrderId() {
        int num = orderCount + 1;
        String numStr = "" + num;
        String xy = "";
        if (num<10) {
            xy ="000";
        } else if (num<100) {
            xy = "0";
        }
        return "B" +xy+numStr;
    }

    private static String getValidCustomerId(Scanner scanner) {
        String custId;
        while (true) {
            System.out.print("Enter Customer ID (Phone no): ");
            custId = scanner.nextLine();
            if (isValidPhoneNumber(custId)) {
                break;
            } else {
                System.out.println("Invalid phone number. Must start with 0 and have 10 digits.");
            }
        }
        return custId;
    }

    public static boolean isValidPhoneNumber(String phone) {
        if (phone.length()!=10) {
            return false;
        }
        if (phone.charAt(0)!='0') {
            return false;
        }
        for (int i=0;i<phone.length();i++) {
            char c = phone.charAt(i);
            if (c<'0' || c >'9') {
                return false;
            }
        }
        return true;
    }

    private static String getCustomerName(Scanner scanner, String custId) {
        for (int i=0;i<orderCount;i++) {
            if (customerIds[i] != null && customerIds[i].equals(custId)) {
                System.out.println("Customer Name: " + customerNames[i]);
                return customerNames[i];
            }
        }
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine();
        while (name.trim().length() == 0) {
            System.out.print("Name cannot be empty. Enter Customer Name: ");
            name = scanner.nextLine();
        }
        return name;
    }

    private static int getValidQuantity(Scanner scanner) {
        int qty;
        while (true) {
            System.out.print("Enter Burger Quantity - ");
            try {
                qty = scanner.nextInt();
                scanner.nextLine();
                if (qty>0) {
                    break;
                } else {
                    System.out.println("Quantity must be greater than 0.");
                }
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Invalid input. Enter a number greater than 0.");
            }
        }
        return qty;
    }

    private static void searchBestCustomer(Scanner scanner) {
        clearConsole();
        System.out.println("-------------------------------------------------------------------");
        System.out.println("|                         Best Customer                           |");
        System.out.println("-------------------------------------------------------------------");
        System.out.println();
        if (orderCount == 0) {
            System.out.println("No orders found");
            pause(scanner);
            return;
        }

        String[] uniqueCustIds = new String[orderCount];
        String[] uniqueNames = new String[orderCount];
        double[] totals = new double[orderCount];
        int uniqueCount = 0;

        for (int i=0; i<orderCount;i++) {
            if (statuses[i] != CANCEL) {
                boolean found = false;
                for (int j = 0; j < uniqueCount; j++) {
                    if (uniqueCustIds[j].equals(customerIds[i])) {
                        totals[j] += quantities[i] * BURGERPRICE;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    uniqueCustIds[uniqueCount] = customerIds[i];
                    uniqueNames[uniqueCount] = customerNames[i];
                    totals[uniqueCount] = quantities[i] * BURGERPRICE;
                    uniqueCount++;
                }
            }
        }

        for (int i=0;i<uniqueCount-1;i++) {
            for (int j=i+1;j<uniqueCount;j++) {
                if (totals[i]<totals[j]) {
                    
                    double tempTotal = totals[i];
                    totals[i] = totals[j];
                    totals[j] = tempTotal;
                    
                    String tempId = uniqueCustIds[i];
                    uniqueCustIds[i] = uniqueCustIds[j];
                    uniqueCustIds[j] = tempId;
                    
                    String tempName = uniqueNames[i];
                    uniqueNames[i] = uniqueNames[j];
                    uniqueNames[j] = tempName;
                }
            }
        }
        System.out.println();
		System.out.println("----------------------------------------------");
        System.out.println(" Customer ID     Name                 Total");
        System.out.println("-----------------------------------------------");
        for (int i=0;i<uniqueCount;i++) {
            System.out.print(uniqueCustIds[i] + "       ");
            System.out.print(padRight(uniqueNames[i], 20) + " ");
            System.out.println("Rs." + totals[i]);
            System.out.println("-----------------------------------------------");
        }

        System.out.print("Do you want to go back to main menu (Y/N)? ");
        String back = scanner.nextLine().toUpperCase();
        while (!back.equals("Y") && !back.equals("N")) {
            System.out.print("Invalid input. Enter Y or N: ");
            back = scanner.nextLine().toUpperCase();
        }
    }

    public static String padRight(String s, int n) {
        String padded = s;
        while (padded.length() < n) {
            padded += " ";
        }
        return padded;
    }

	public static void searchOrder(Scanner scanner) {
        clearConsole();
        System.out.println("-------------------------------------------------------------------");
        System.out.println("|                          Search Order                           |");
        System.out.println("-------------------------------------------------------------------");
        System.out.println();
        System.out.print("Enter order Id - ");
        String searchId = scanner.nextLine().toUpperCase();

        boolean found = false;
        for (int i=0;i<orderCount;i++) {
            if (orderIds[i].equals(searchId)) {
				System.out.println("-----------------------------------------------------------------------------");
				System.out.println(" OrderID   CustomerID     Name       Quantitiy    OrderValue    OrderStatus |");
				System.out.println("-----------------------------------------------------------------------------");
				System.out.printf(" %4s      %10s%10s        %3d         %5.2f      %10s  |\n" , orderIds[i], customerIds[i], customerNames[i], quantities[i], (quantities[i] * BURGERPRICE), getStatusString(statuses[i]));
				System.out.println("-----------------------------------------------------------------------------");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.print("Invalid Order ID. ");
        }

        System.out.print("Do you want to enter again (Y/N)> ");
        String another = scanner.nextLine().toUpperCase();
        while (!another.equals("Y") && !another.equals("N")) {
            System.out.print("Invalid input. Enter Y or N: ");
            another = scanner.nextLine().toUpperCase();
        }
        if (another.equals("Y")) {
            searchOrder(scanner);
        }
    }

    public static void searchCustomer(Scanner scanner) {
        clearConsole();
        System.out.println("-------------------------------------------------------------------");
        System.out.println("|                         Search Customer                         |");
        System.out.println("-------------------------------------------------------------------");
        System.out.println();
        System.out.print("Enter Customer Id - ");
        String searchId = scanner.nextLine();
        System.out.println();
        

        boolean found = false;
        double x=0;
        for (int i=0;i<orderCount;i++) {
            if (customerIds[i].equals(searchId)) {
                found = true;
                double amount = quantities[i] * BURGERPRICE;
                if (statuses[i] != CANCEL) {
                    x += amount;
                }
				System.out.println("CustomerID - " + customerIds[i]);
				System.out.println("Name       - "+ customerNames[i]);
			}
		}
		System.out.println();
		System.out.println("Customer Order Details");
		System.out.println("========================");
        System.out.println();
        System.out.println("---------------------------------------------");
        System.out.println(" Order_ID    Order_Quantity     Total_Value");
        System.out.println("---------------------------------------------");
        double total = 0;
        for (int i = 0; i < orderCount; i++) {
            if (customerIds[i].equals(searchId)) {
                found = true;
                double amount = quantities[i] * BURGERPRICE;
                if (statuses[i] != CANCEL) {
                    total += amount;
                }
                System.out.print(" "+orderIds[i] + "            ");
                System.out.print(quantities[i] + "                ");
                System.out.println(amount);
            }
        }
        System.out.println("---------------------------------------------");
        if (found) {
            System.out.println();
        } else {
            System.out.println("Customer not found.");
        }

        System.out.print("Do you want to search another customer (Y/N)? ");
        String another = scanner.nextLine().toUpperCase();
        while (!another.equals("Y") && !another.equals("N")) {
            System.out.print("Invalid input. Enter Y or N: ");
            another = scanner.nextLine().toUpperCase();
        }
        if (another.equals("Y")) {
            searchCustomer(scanner);
        }
    }

    public static void viewOrders(Scanner scanner) {
        clearConsole();
        System.out.println("-------------------------------------------------------------------");
        System.out.println("|                         View Orders                             |");
        System.out.println("-------------------------------------------------------------------");
        System.out.println();
        System.out.println("[1] Delivered Order");
        System.out.println("[2] Preparing Order");
        System.out.println("[3] Cancelled Order");
        System.out.println();
        System.out.print("Enter an option to continue > ");
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Invalid input");
            pause(scanner);
            return;
        }

        int statusFilter;
        String title;
        switch (choice) {
            case 1:
                statusFilter = DELIVERED;
                title = "Delivered Orders";
                break;
            case 2:
                statusFilter = PREPARING;
                title = "Preparing Orders";
                break;
            case 3:
                statusFilter = CANCEL;
                title = "Cancelled Orders";
                break;
            default:
                System.out.println("Invalid choice.");
                pause(scanner);
                return;
        }

        clearConsole();
        System.out.println("-------------------------------------------------------------------");
        System.out.printf("|                          %16s                       |\n" , title);
        System.out.println("-------------------------------------------------------------------");
        System.out.println();
        System.out.println();
        System.out.println("--------------------------------------------------------------------");
        System.out.println("OrderID    CustomerID      Name              Quantity   OrderValue  ");
        System.out.println("--------------------------------------------------------------------");
        boolean found = false;
        for (int i = 0; i < orderCount; i++) {
            if (statuses[i] == statusFilter) {
                found = true;
                System.out.printf(" %4s      %10s      %10s%3d         %5.2f \n" , orderIds[i], customerIds[i], (padRight(customerNames[i], 20)), quantities[i], (quantities[i] * BURGERPRICE));
            }
        }
        if (!found) {
            System.out.println("No orders found for this status.");
        }

        System.out.print("Do you want to go back to main menu (Y/N)? ");
        String back = scanner.nextLine().toUpperCase();
        while (!back.equals("Y") && !back.equals("N")) {
            System.out.print("Invalid input. Enter Y or N: ");
            back = scanner.nextLine().toUpperCase();
        }
    }

    public static void updateOrderDetails(Scanner scanner) {
        clearConsole();
        System.out.println("-------------------------------------------------------------------");
        System.out.println("|                    Update Order Details                         |");
        System.out.println("-------------------------------------------------------------------");
        System.out.println();
        System.out.print("Enter Order ID: ");
        String searchId = scanner.nextLine().toUpperCase();

        int index = -1;
        for (int i = 0; i < orderCount; i++) {
            if (orderIds[i].equals(searchId)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Invalid Order ID.");
            System.out.println();
                System.out.print("Do you want to update another order details (Y/N): ");
				String another = scanner.nextLine().toUpperCase();
					while (!another.equals("Y") && !another.equals("N")) {
						System.out.print("Invalid input. Enter Y or N: ");
						another = scanner.nextLine().toUpperCase();
					}
					if (another.equals("Y")) {
						updateOrderDetails(scanner);
					}
        }

        if (statuses[index]!=PREPARING) {
            if (statuses[index]==DELIVERED) {
				System.out.println();
                System.out.println("This order is already deliverd...You can not update this order...");
                System.out.println();
                System.out.print("Do you want to update another order details (Y/N): ");
				String another = scanner.nextLine().toUpperCase();
					while (!another.equals("Y") && !another.equals("N")) {
						System.out.print("Invalid input. Enter Y or N: ");
						another = scanner.nextLine().toUpperCase();
					}
					if (another.equals("Y")) {
						updateOrderDetails(scanner);
					}
                
            } else {
                System.out.println("This order is already cancelled...You can not update this order...");
                System.out.println();
                System.out.print("Do you want to update another order details (Y/N): ");
				String another = scanner.nextLine().toUpperCase();
					while (!another.equals("Y") && !another.equals("N")) {
						System.out.print("Invalid input. Enter Y or N: ");
						another = scanner.nextLine().toUpperCase();
					}
					if (another.equals("Y")) {
						updateOrderDetails(scanner);
					}
            }
            pause(scanner);
            return;
        }

        
        System.out.println();
        System.out.println("OrderID     - " + orderIds[index]);
        System.out.println("CustomerID  - " + customerIds[index]);
        System.out.println("Name        - " + customerNames[index]);
        System.out.println("Quantity    - " + quantities[index]);
        System.out.println("Order Value - " + (quantities[index] * BURGERPRICE));
        System.out.println("OrderStatus - " + getStatusString(statuses[index]));
		
		System.out.println();
        System.out.println("What do you want to update?");
        System.out.println("        (01) Quantity");
        System.out.println("        (02) Status");
        System.out.println();
        System.out.print("Enter your option - ");
        int updateChoice;
        try {
            updateChoice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Invalid input. Please enter a number.");
            pause(scanner);
            return;
        }

        switch (updateChoice) {
            case 1:
				System.out.println();
				System.out.println("Quantity update");
				System.out.println("=================");
				System.out.println();
				System.out.println("OrderID     - " + orderIds[index]);
				System.out.println("CustomerID  - " + customerIds[index]);
				System.out.println("Name        - " + customerNames[index]);
				System.out.println();
				System.out.print("Enter your quantity update value - ");
                int newQty = scanner.nextInt();
                quantities[index] = newQty;
                System.out.println();
                System.out.println("           update order quantity success fully...");
                System.out.println();
                System.out.println("new order quantity - " + newQty);
                System.out.println("new order Value - " + (newQty * BURGERPRICE));
                break;
            case 2:
				System.out.println();
				System.out.println("Status Update");
				System.out.println("===============");
				System.out.println();
				System.out.println("OrderID     - " + orderIds[index]);
				System.out.println("CustomerID  - " + customerIds[index]);
				System.out.println("Name        - " + customerNames[index]);
				System.out.println();
				System.out.println("        (0)Preparing");
				System.out.println("        (1)Delivered");
				System.out.println("        (2)Cancel");
				System.out.println();
                System.out.print("Enter new order status - ");
                int newStatus;
                try {
                    newStatus = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.print("Invalid input. Please enter a number.");
                    pause(scanner);
                    return;
                }
                if (newStatus >=0 && newStatus<=2) {
                    statuses[index] = newStatus;
                    System.out.println("new order status - "+ getStatusString(newStatus));
                } else {
                    System.out.println("Invalid status.");
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
		System.out.println();
        System.out.print("Do you want to update another order (Y/N)? ");
        String another = scanner.next().toUpperCase();
        if (!another.equals("Y") || !another.equals("N")) {
            System.out.print("Invalid input. Enter Y or N: ");
            another = scanner.next().toUpperCase();
        }
        if (another.equals("Y")) {
            updateOrderDetails(scanner);
        }
    }

    public static String getStatusString(int status) {
        if (status == CANCEL) {
            return "Cancel";
        } else if (status == DELIVERED) {
            return "Delivered";
        } else if (status == PREPARING) {
            return "Preparing";
        } else {
            return "Unknown";
        }
    }

    public static void pause(Scanner scanner) {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    public static void exit() {
        clearConsole();
        System.out.println("\n\t\tYou left the program...\n");
        System.exit(0);
    }

    public final static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
