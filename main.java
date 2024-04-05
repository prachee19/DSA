import java.util.*;

class TimeSlot {
    private String startTime;
    private String endTime;

    public TimeSlot(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}

class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class ConferenceRoom {
    private String name;
    private int capacity;
    private String location;
    private boolean isAvailable;
    private User bookedBy;
    private String meetingTitle;
    private List<TimeSlot> bookedTimeSlots;

    public ConferenceRoom(String name, int capacity, String location) {
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.isAvailable = true;
        this.bookedBy = null;
        this.meetingTitle = "";
        this.bookedTimeSlots = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public User getBookedBy() {
        return bookedBy;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public boolean isAvailable(String startTime, String endTime) {
        for (TimeSlot slot : bookedTimeSlots) {
            if (slot.getStartTime().compareTo(endTime) < 0 && slot.getEndTime().compareTo(startTime) > 0) {
                return false; // Room is booked during the requested time slot
            }
        }
        return true; // Room is available during the requested time slot
    }

    public void bookRoom(User user, String startTime, String endTime, String meetingTitle) {
        if (!isAvailable(startTime, endTime)) {
            System.out.println("Room " + name + " is already booked for the specified time slot.");
            return;
        }

        this.isAvailable = false;
        this.bookedBy = user;
        this.meetingTitle = meetingTitle;
        this.bookedTimeSlots.add(new TimeSlot(startTime, endTime));

        System.out.println("Room " + name + " has been booked by " + user.getName() + " for '" + meetingTitle + "' from " + startTime + " to " + endTime + ".");
    }

    public void makeAvailable() {
        this.isAvailable = true;
        this.bookedBy = null;
        this.meetingTitle = "";
        this.bookedTimeSlots.clear();
    }
}

class RoomBookingSystem {
    private List<ConferenceRoom> conferenceRooms;

    public RoomBookingSystem() {
        conferenceRooms = new ArrayList<>();
    }

    public void addConferenceRoom(ConferenceRoom room) {
        conferenceRooms.add(room);
    }

    public void displayRoomsAvailability() {
        System.out.println("Conference Room Availability:");
        for (ConferenceRoom room : conferenceRooms) {
            System.out.println(room.getName() + ": " + (room.isAvailable() ? "Available" : "Not Available"));
        }
    }

    public void displayAvailableRooms() {
        System.out.println("Available Conference Rooms:");
        boolean availableRooms = false;
        for (ConferenceRoom room : conferenceRooms) {
            if (room.isAvailable()) {
                System.out.println(room.getName() + " - Capacity: " + room.getCapacity() + ", Location: " + room.getLocation());
                availableRooms = true;
            }
        }
        if (!availableRooms) {
            System.out.println("No available rooms at the moment.");
        }
    }

    public void bookRoom(String roomName, String startTime, String endTime, User user, String meetingTitle) {
        for (ConferenceRoom room : conferenceRooms) {
            if (room.getName().equals(roomName)) {
                if (room.isAvailable(startTime, endTime)) {
                    room.bookRoom(user, startTime, endTime, meetingTitle);
                } else {
                    System.out.println("Room " + roomName + " is not available for the requested time slot.");
                }
                return;
            }
        }
        System.out.println("Room " + roomName + " not found.");
    }

    public void displayBookedRooms() {
        System.out.println("Booked Conference Rooms:");
        boolean bookedRooms = false;
        for (ConferenceRoom room : conferenceRooms) {
            if (!room.isAvailable()) {
                System.out.println(room.getName() + " - Booked by: " + room.getBookedBy().getName() + ", Meeting: " + room.getMeetingTitle());
                bookedRooms = true;
            }
        }
        if (!bookedRooms) {
            System.out.println("No rooms are currently booked.");
        }
    }
}

public class main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RoomBookingSystem bookingSystem = new RoomBookingSystem();

        // Adding some initial conference rooms
        ConferenceRoom room1 = new ConferenceRoom("Room 1", 10, "1st floor");
        ConferenceRoom room2 = new ConferenceRoom("Room 2", 15, "5th floor");
        ConferenceRoom room3 = new ConferenceRoom("Room 3", 15, "10th floor");
        bookingSystem.addConferenceRoom(room1);
        bookingSystem.addConferenceRoom(room2);
        bookingSystem.addConferenceRoom(room3);

        while (true) {
            System.out.println("Enter your role: ");
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.println("3. Exit");
            int roleChoice = sc.nextInt();
            sc.nextLine(); // Consume newline

            if (roleChoice == 1) { // Admin
                while(true) {
                    bookingSystem.displayAvailableRooms();
                    System.out.println("Admin Menu:");
                    System.out.println("1. Add Conference Room");
                    System.out.println("2. View Booked Rooms");
                    System.out.println("3. Exit");
                    int adminChoice = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    switch (adminChoice) {
                        case 1:
                            System.out.println("Enter room name:");
                            String roomName = sc.nextLine();
                            System.out.println("Enter room capacity:");
                            int capacity = sc.nextInt();
                            sc.nextLine(); // Consume newline
                            System.out.println("Enter room location:");
                            String location = sc.nextLine();
                            ConferenceRoom newRoom = new ConferenceRoom(roomName, capacity, location);
                            bookingSystem.addConferenceRoom(newRoom);
                            break;
                        case 2:
                            bookingSystem.displayBookedRooms();
                            break;
                        case 3:
                            System.out.println("Exiting...");
                            return;
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
            } else if (roleChoice == 2) { // User
                System.out.println("Enter the room name:");
                String roomName = sc.nextLine();
                System.out.println("Enter the meeting start time (HH:MM):");
                String startTime = sc.nextLine();
                System.out.println("Enter the meeting end time (HH:MM):");
                String endTime = sc.nextLine();
                System.out.println("Enter your name:");
                String userName = sc.nextLine();
                User user = new User(userName);
                System.out.println("Enter the meeting title:");
                String meetingTitle = sc.nextLine();
                bookingSystem.bookRoom(roomName, startTime, endTime, user, meetingTitle);
            } else if (roleChoice == 3) { // Exit
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid role choice.");
            }
        }
    }
}