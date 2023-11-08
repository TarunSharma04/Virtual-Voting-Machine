import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VirtualVotingMachine {
    private static Map<String, Integer> candidates = new HashMap<>();
    private static Map<String, Integer> votes = new HashMap<>();
    private static Map<String, Boolean> studentIDs = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String ADMIN_KEY = "Tarun123"; // Change this to a secure admin key

    public static void main(String[] args) {
        System.out.println("Welcome to the Virtual Voting Machine!");

        // Set up admin key
        System.out.print("Enter the admin key: ");
        String enteredKey = scanner.nextLine();
        if (!enteredKey.equals(ADMIN_KEY)) {
            System.out.println("Incorrect admin key. Exiting...");
            System.exit(1);
        }

        // Input the number of candidates
        System.out.print("Enter the number of candidates: ");
        int numCandidates = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 1; i <= numCandidates; i++) {
            System.out.print("Enter the name of candidate " + i + ": ");
            String candidateName = scanner.nextLine();
            candidates.put(candidateName, i);
            votes.put(candidateName, 0);
        }

        // Input student data
        while (true) {
            System.out.print("Enter student ID (or 'done' to finish input): ");
            String studentID = scanner.nextLine();
            if (studentID.equals("done")) {
                break; // Exit input loop
            }
            studentIDs.put(studentID, false);
        }

        int totalVoters = studentIDs.size();
        int votesCast = 0;

        boolean votingClosed = false;

        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    if (!votingClosed) {
                        String voterID = vote();
                        if (voterID != null) {
                            votesCast++;
                            studentIDs.put(voterID, true); // Mark the student as voted
                        }
                    } else {
                        System.out.println("Voting is closed. Cannot vote anymore.");
                    }
                    break;
                case 2:
                    if (votingClosed) {
                        displayResults(); // Display results (admin only)
                    } else {
                        System.out.println("Cannot view results until voting is closed.");
                    }
                    break;
                case 3:
                    if (adminExit()) {
                        System.out.println("Exiting the program as per admin request...");
                        announceWinner(); // Announce the winner before exiting
                        return; // Exit the program only if adminExit() returns true
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please select again.");
                    break;
            }

            // Check if all voters have cast their votes
            if (votesCast == totalVoters && !votingClosed) {
                System.out.println("Voting closed!! Result will be declared soon. Stay tuned.");
                votingClosed = true;
                announceWinner(); // Announce the winner when voting is closed
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n==== Virtual Voting Machine ====");
        System.out.println("1. Vote");
        System.out.println("2. Display Results (Admin Only)");
        System.out.println("3. Exit (Admin Only)");
        System.out.print("Enter your choice: ");
    }

    private static String vote() {
        System.out.print("\nEnter your student ID: ");
        String studentID = scanner.nextLine();

        if (studentIDs.containsKey(studentID) && !studentIDs.get(studentID)) {
            System.out.println("\nStudent ID verified. You can vote now.");
            System.out.println("Candidates:");
            for (String candidate : candidates.keySet()) {
                System.out.println(candidates.get(candidate) + ". " + candidate);
            }

            System.out.print("Enter the number of the candidate you want to vote for: ");
            int candidateNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String selectedCandidate = getCandidateName(candidateNumber);
            if (selectedCandidate != null) {
                votes.put(selectedCandidate, votes.get(selectedCandidate) + 1);
                System.out.println("\nVote recorded for " + selectedCandidate);
                return studentID;
            } else {
                System.out.println("\nInvalid candidate number.");
            }
        } else if (studentIDs.containsKey(studentID) && studentIDs.get(studentID)) {
            System.out.println("\nYou have already voted. Cannot vote again.");
        } else {
            System.out.println("\nInvalid student ID.");
        }
        return null;
    }

    private static String getCandidateName(int candidateNumber) {
        for (String candidate : candidates.keySet()) {
            if (candidates.get(candidate) == candidateNumber) {
                return candidate;
            }
        }
        return null;
    }

    private static void displayResults() {
        System.out.print("\nEnter the admin key to view results: ");
        String enteredKey = scanner.nextLine();
        if (enteredKey.equals(ADMIN_KEY)) {
            System.out.println("\n==== Election Results ====");
            for (String candidate : candidates.keySet()) {
                System.out.println(candidate + ": " + votes.get(candidate) + " votes");
            }
        } else {
            System.out.println("\nIncorrect admin key. Cannot view results.");
        }
    }

    private static boolean adminExit() {
        System.out.print("\nEnter the admin key to exit: ");
        String enteredKey = scanner.nextLine();
        return enteredKey.equals(ADMIN_KEY);
    }

    private static void announceWinner() {
        System.out.println("\n==== Winner Announcement ====");
        String winner = getWinner();
        if (winner != null) {
            System.out.println("Winner: " + winner);
        } else {
            System.out.println("No winner. It's a tie!");
        }
    }

    private static String getWinner() {
        String winner = null;
        int maxVotes = 0;

        for (String candidate : candidates.keySet()) {
            int candidateVotes = votes.get(candidate);
            if (candidateVotes > maxVotes) {
                maxVotes = candidateVotes;
                winner = candidate;
            } else if (candidateVotes == maxVotes) {
                winner = null; // It's a tie
            }
        }
        return winner;
    }
}
