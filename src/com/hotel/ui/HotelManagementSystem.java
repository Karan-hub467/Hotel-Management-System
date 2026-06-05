package com.hotel.ui;

import com.hotel.model.*;
import com.hotel.service.HotelService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class HotelManagementSystem {
    private static HotelService service = new HotelService();
    private static Scanner sc = new Scanner(System.in);
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n+========================================+");
            System.out.println("|     HOTEL MANAGEMENT SYSTEM            |");
            System.out.println("+========================================+");
            System.out.println("| 1. Manage Rooms                        |");
            System.out.println("| 2. Manage Customers                    |");
            System.out.println("| 3. Manage Bookings                     |");
            System.out.println("| 4. Check-In / Check-Out                |");
            System.out.println("| 5. View Reports                        |");
            System.out.println("| 0. Exit                                |");
            System.out.println("+========================================+");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": roomMenu(); break;
                case "2": customerMenu(); break;
                case "3": bookingMenu(); break;
                case "4": checkInOutMenu(); break;
                case "5": reportMenu(); break;
                case "0":
                    System.out.println("Thank you for using HMS!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void roomMenu() {
        while (true) {
            System.out.println("\n--- ROOMS MENU ---");
            System.out.println("1. View All Rooms");
            System.out.println("2. Add New Room");
            System.out.println("3. View Available Rooms");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1":
                    List<Room> rooms = service.getAllRooms();
                    if (rooms.isEmpty()) { System.out.println("No rooms found."); break; }
                    System.out.println("\n+-----+----------+------------+----------+----------+---------------+");
                    System.out.println("| ID  | Room No  | Type       | Price    | Capacity | Status        |");
                    System.out.println("+-----+----------+------------+----------+----------+---------------+");
                    for (Room r : rooms) {
                        System.out.printf("| %-3d | %-8s | %-10s | $%-7.2f | %-8d | %-13s |\n",
                            r.getId(), r.getRoomNumber(), r.getRoomType(), r.getPrice(), r.getCapacity(), r.getStatus());
                    }
                    System.out.println("+-----+----------+------------+----------+----------+---------------+");
                    break;
                case "2":
                    System.out.print("Room Number: "); String rn = sc.nextLine().trim();
                    System.out.print("Room Type (Single/Double/Deluxe/Suite): "); String rt = sc.nextLine().trim().toLowerCase();
                    System.out.print("Price per night: "); double pr = Double.parseDouble(sc.nextLine().trim());
                    System.out.print("Capacity: "); int cap = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Description: "); String desc = sc.nextLine().trim();
                    Room newRoom = new Room(rn, rt, pr, cap, desc);
                    if (service.addRoom(newRoom)) {
                        System.out.println("Room added successfully!");
                    } else {
                        System.out.println("Failed to add room.");
                    }
                    break;
                case "3":
                    System.out.print("Check-In Date (yyyy-MM-dd): ");
                    LocalDate ci = parseDate(sc.nextLine().trim());
                    System.out.print("Check-Out Date (yyyy-MM-dd): ");
                    LocalDate co = parseDate(sc.nextLine().trim());
                    if (ci == null || co == null) { System.out.println("Invalid date."); break; }
                    List<Room> avail = service.getAvailableRooms(ci, co);
                    if (avail.isEmpty()) { System.out.println("No rooms available for those dates."); break; }
                    System.out.println("\nAvailable Rooms:");
                    for (Room r : avail) {
                        System.out.printf("%-3d | %-8s | %-10s | $%.2f/night | %d pax\n",
                            r.getId(), r.getRoomNumber(), r.getRoomType(), r.getPrice(), r.getCapacity());
                    }
                    break;
                case "0": return;
                default: System.out.println("Invalid.");
            }
        }
    }

    private static void customerMenu() {
        while (true) {
            System.out.println("\n--- CUSTOMERS MENU ---");
            System.out.println("1. View All Customers");
            System.out.println("2. Add New Customer");
            System.out.println("3. Search by Phone");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1":
                    List<Customer> customers = service.getAllCustomers();
                    if (customers.isEmpty()) { System.out.println("No customers found."); break; }
                    System.out.println("\n+-----+---------------------------+-----------------+----------------------+");
                    System.out.println("| ID  | Name                      | Phone           | Email                |");
                    System.out.println("+-----+---------------------------+-----------------+----------------------+");
                    for (Customer c : customers) {
                        System.out.printf("| %-3d | %-25s | %-15s | %-20s |\n",
                            c.getId(), c.getName(), c.getPhone(), c.getEmail());
                    }
                    System.out.println("+-----+---------------------------+-----------------+----------------------+");
                    break;
                case "2":
                    System.out.print("Name: "); String nm = sc.nextLine().trim();
                    System.out.print("Email: "); String em = sc.nextLine().trim();
                    System.out.print("Phone: "); String ph = sc.nextLine().trim();
                    System.out.print("Address: "); String ad = sc.nextLine().trim();
                    System.out.print("ID Proof (Passport/Driver License/National ID): "); String idp = sc.nextLine().trim();
                    int cid = service.addCustomer(new Customer(nm, em, ph, ad, idp));
                    if (cid > 0) {
                        System.out.println("Customer added with ID: " + cid);
                    } else {
                        System.out.println("Failed to add customer.");
                    }
                    break;
                case "3":
                    System.out.print("Phone: "); String phone = sc.nextLine().trim();
                    Customer cust = service.getCustomerByPhone(phone);
                    if (cust == null) {
                        System.out.println("Customer not found.");
                    } else {
                        System.out.println("ID: " + cust.getId());
                        System.out.println("Name: " + cust.getName());
                        System.out.println("Email: " + cust.getEmail());
                        System.out.println("Phone: " + cust.getPhone());
                        System.out.println("Address: " + cust.getAddress());
                        System.out.println("ID Proof: " + cust.getIdProof());
                    }
                    break;
                case "0": return;
                default: System.out.println("Invalid.");
            }
        }
    }

    private static void bookingMenu() {
        while (true) {
            System.out.println("\n--- BOOKINGS MENU ---");
            System.out.println("1. View All Bookings");
            System.out.println("2. View Active Bookings");
            System.out.println("3. New Booking");
            System.out.println("4. Cancel Booking");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1":
                case "2":
                    List<Booking> bookings = ch.equals("1") ? service.getAllBookings() : service.getActiveBookings();
                    if (bookings.isEmpty()) { System.out.println("No bookings found."); break; }
                    System.out.println("\n+-----+---------------------+----------+------------+------------+----------+--------+------------+");
                    System.out.println("| ID  | Customer            | Room     | Check-In   | Check-Out  | Nights   | Total  | Status     |");
                    System.out.println("+-----+---------------------+----------+------------+------------+----------+--------+------------+");
                    for (Booking b : bookings) {
                        System.out.printf("| %-3d | %-19s | %-8s | %-10s | %-10s | %-8d | $%-5.2f | %-10s |\n",
                            b.getId(), trunc(b.getCustomerName(), 19), b.getRoomNumber(),
                            b.getCheckIn(), b.getCheckOut(), b.getNights(), b.getTotalAmount(), b.getStatus());
                    }
                    System.out.println("+-----+---------------------+----------+------------+------------+----------+--------+------------+");
                    break;
                case "3":
                    System.out.print("Customer ID: "); int cusId = Integer.parseInt(sc.nextLine().trim());
                    Customer custCheck = service.getCustomerById(cusId);
                    if (custCheck == null) { System.out.println("Customer not found."); break; }
                    System.out.print("Check-In (yyyy-MM-dd): ");
                    LocalDate ci = parseDate(sc.nextLine().trim());
                    System.out.print("Check-Out (yyyy-MM-dd): ");
                    LocalDate co = parseDate(sc.nextLine().trim());
                    if (ci == null || co == null || co.isBefore(ci) || co.isEqual(ci)) {
                        System.out.println("Invalid date range."); break;
                    }
                    List<Room> availRooms = service.getAvailableRooms(ci, co);
                    if (availRooms.isEmpty()) { System.out.println("No rooms available."); break; }
                    System.out.println("Available Rooms:");
                    for (Room r : availRooms) {
                        System.out.printf("  %d. %s (%s) - $%.2f/night\n", r.getId(), r.getRoomNumber(), r.getRoomType(), r.getPrice());
                    }
                    System.out.print("Select Room ID: "); int roomId = Integer.parseInt(sc.nextLine().trim());
                    int bid = service.createBooking(cusId, roomId, ci, co);
                    if (bid > 0) {
                        System.out.println("Booking created! Booking ID: " + bid);
                    } else {
                        System.out.println("Failed to create booking.");
                    }
                    break;
                case "4":
                    System.out.print("Booking ID to cancel: "); int cancelId = Integer.parseInt(sc.nextLine().trim());
                    if (service.cancelBooking(cancelId)) {
                        System.out.println("Booking cancelled.");
                    } else {
                        System.out.println("Failed to cancel booking.");
                    }
                    break;
                case "0": return;
                default: System.out.println("Invalid.");
            }
        }
    }

    private static void checkInOutMenu() {
        while (true) {
            System.out.println("\n--- CHECK-IN / CHECK-OUT ---");
            System.out.println("1. Check-In");
            System.out.println("2. Check-Out");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1":
                    System.out.print("Booking ID: "); int ciId = Integer.parseInt(sc.nextLine().trim());
                    if (service.checkIn(ciId)) {
                        System.out.println("Check-In successful!");
                    } else {
                        System.out.println("Check-In failed. Check booking ID.");
                    }
                    break;
                case "2":
                    System.out.print("Booking ID: "); int coId = Integer.parseInt(sc.nextLine().trim());
                    Booking b = service.getBookingById(coId);
                    if (b == null) { System.out.println("Booking not found."); break; }
                    System.out.println("Booking: " + b);
                    System.out.println("Total Amount: $" + b.getTotalAmount());
                    System.out.print("Confirm payment received? (y/n): ");
                    if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                        if (service.checkOut(coId)) {
                            System.out.println("Check-Out successful! Payment marked as paid.");
                        } else {
                            System.out.println("Check-Out failed.");
                        }
                    } else {
                        System.out.println("Check-Out cancelled.");
                    }
                    break;
                case "0": return;
                default: System.out.println("Invalid.");
            }
        }
    }

    private static void reportMenu() {
        while (true) {
            System.out.println("\n--- REPORTS ---");
            System.out.println("1. Occupancy Summary");
            System.out.println("2. All Bookings");
            System.out.println("3. All Customers");
            System.out.println("4. All Rooms");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1":
                    List<Booking> active = service.getActiveBookings();
                    List<Room> allRooms = service.getAllRooms();
                    int total = allRooms.size();
                    int occupied = 0;
                    for (Room r : allRooms) {
                        if (r.getStatus().equals("occupied") || r.getStatus().equals("booked")) occupied++;
                    }
                    System.out.println("\n=== OCCUPANCY SUMMARY ===");
                    System.out.println("Total Rooms: " + total);
                    System.out.println("Occupied: " + occupied);
                    System.out.println("Available: " + (total - occupied));
                    System.out.println("Occupancy Rate: " + (total > 0 ? (occupied * 100 / total) : 0) + "%");
                    System.out.println("Active Bookings: " + active.size());
                    break;
                case "2":
                    List<Booking> all = service.getAllBookings();
                    if (all.isEmpty()) { System.out.println("No bookings."); break; }
                    System.out.println("\nAll Bookings:");
                    for (Booking bk : all) System.out.println("  " + bk);
                    break;
                case "3":
                    List<Customer> custs = service.getAllCustomers();
                    if (custs.isEmpty()) { System.out.println("No customers."); break; }
                    System.out.println("\nAll Customers:");
                    for (Customer c : custs) System.out.println("  " + c);
                    break;
                case "4":
                    List<Room> rooms = service.getAllRooms();
                    if (rooms.isEmpty()) { System.out.println("No rooms."); break; }
                    System.out.println("\nAll Rooms:");
                    for (Room r : rooms) System.out.println("  " + r);
                    break;
                case "0": return;
                default: System.out.println("Invalid.");
            }
        }
    }

    private static LocalDate parseDate(String s) {
        try { return LocalDate.parse(s, dtf); }
        catch (DateTimeParseException e) { return null; }
    }

    private static String trunc(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 2) + "..";
    }
}
