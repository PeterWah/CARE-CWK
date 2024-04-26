package cwk4;

import java.util.*;
import java.io.*;

/**
 * This interface specifies the behavior expected from CARE
 * as required for 5COM2007 Cwk 4
 *
 * @author Thouheid Ahmed
 * @version
 */
public class Tournament implements CARE {
    private String vizierName;
    private int treasury;
    private boolean defeated;
    private List<String> team;
    private Map<String, Champion> champions;
    private Map<String, Challenge> challenges;
    // Constructors and other methods...

    @Override
    public void play() {
        // Implementation of play method
    }

    @Override
    public boolean challenge(int num) {
        // Implementation of challenge method
        return false; // Placeholder return statement, replace as needed
    }

    @Override
    public boolean championDetails(String name) {
        // Implementation of championDetails method
        return false; // Placeholder return statement, replace as needed
    }

    @Override
    public boolean retireChampion(String name) {
        // Implementation of retireChampion method
        return false; // Placeholder return statement, replace as needed
    }

    @Override
    public boolean isInViziersTeam(String name) {
        // Implementation of isInViziersTeam method
        return false; // Placeholder return statement, replace as needed
    }

    @Override
    public boolean isChallenge(int num) {
        // Implementation of isChallenge method
        return false; // Placeholder return statement, replace as needed
    }

    @Override
    public String getChallenge(int num) {
        // Implementation of getChallenge method
        return null; // Placeholder return statement, replace as needed
    }

    @Override
    public String getAllChallenges() {
        // Implementation of getAllChallenges method
        return null; // Placeholder return statement, replace as needed
    }

}

    //**************** CARE **************************
    /** Constructor requires the name of the vizier
     * @param viz the name of the vizier
     */
    // Constructor with vizier name parameter
    public Tournament(String viz) {
        this.vizierName = viz;
        this.treasury = 0;
        this.defeated = false;
        this.team = new ArrayList<>();
        this.champions = new HashMap<>();
        this.challenges = new HashMap<>();
        setupChampions();
        setupChallenges();
    }

    // Default constructor
    public Tournament() {
        // Initialize default values
        this.vizierName = "";
        this.treasury = 0;
        this.defeated = false;
        this.team = new ArrayList<>();
        this.champions = new HashMap<>();
        this.challenges = new HashMap<>();
        setupChampions();
        setupChallenges();
    }
/** Constructor requires the name of the vizier and the
 * name of the file storing challenges
 * @param viz the name of the vizier
 * @param filename name of file storing challenges
 */
public Tournament(String viz, String filename)  //Task 3.5
{


    setupChampions();
    readChallenges(filename);
}

    /**Returns a String representation of the state of the game,
     * including the name of the vizier, state of the treasury,
     * whether defeated or not, and the champions currently in the
     * team,(or, "No champions" if team is empty)
     *
     * @return a String representation of the state of the game,
     * including the name of the vizier, state of the treasury,
     * whether defeated or not, and the champions currently in the
     * team,(or, "No champions" if team is empty)
     **/
    public String toString() {
        // Initialize a StringBuilder to construct the string representation
        StringBuilder sb = new StringBuilder();

        // Append vizier name
        sb.append("Vizier: ").append(vizier.getName()).append("\n");

        // Append treasury state
        sb.append("Treasury: ").append(treasury).append("\n");

        // Append defeated status
        sb.append("Defeated: ").append(defeated).append("\n");

        // Append champions in the team, or "No champions" if team is empty
        if (team.isEmpty()) {
            sb.append("Champions: No champions");
        } else {
            sb.append("Champions: ");
            for (String championName : team) {
                sb.append(championName).append(", ");
            }
            // Remove the trailing comma and space
            sb.setLength(sb.length() - 2);
        }

        // Return the constructed string
        return sb.toString();
    }

    /** returns true if Treasury <=0 and the vizier's team has no
     * champions which can be retired.
     * @returns true if Treasury <=0 and the vizier's team has no
     * champions which can be retired.
     */
    public boolean isDefeated() {
        // Check if the treasury is less than or equal to 0
        if (treasury <= 0) {
            // Check if there are no champions in the team or all champions are disqualified
            for (String championName : team) {
                Champion champion = champions.get(championName);
                if (!champion.isDisqualified()) {
                    return false; // If at least one champion can be retired, the vizier is not defeated
                }
            }
            // If none of the champions can be retired and treasury is <= 0, vizier is defeated
            return true;
        } else {
            // If treasury is greater than 0, vizier is not defeated
            return false;
        }
    }

    /** returns the amount of money in the Treasury
     * @returns the amount of money in the Treasury
     */
    public int getMoney() {
        return treasury;
    }

    /**Returns a String representation of all champions in the reserves
     * @return a String representation of all champions in the reserves
     **/
    public String getReserve() {
        StringBuilder reserveString = new StringBuilder();

        reserveString.append("************ Champions available in reserves********\n");

        // Iterate through all champions
        for (Map.Entry<String, Champion> entry : champions.entrySet()) {
            String championName = entry.getKey();
            Champion champion = entry.getValue();

            // Check if the champion is not in the team (in reserve)
            if (!team.contains(championName)) {
                // Append champion name to the reserveString
                reserveString.append(championName).append("\n");
            }
        }

        return reserveString.toString();
    }

    /** Returns details of the champion with the given name.
     * Champion names are unique.
     * @return details of the champion with the given name
     **/
    public String getChampionDetails(String name) {
        Champion champion = champions.get(name); // Retrieve the champion by name

        if (champion != null) {
            // If the champion is found, construct and return its details
            StringBuilder details = new StringBuilder();
            details.append("Champion Name: ").append(name).append("\n");
            details.append("Level: ").append(champion.getLevel()).append("\n");
            // Add more details as needed

            return details.toString();
        } else {
            // If the champion is not found, return a message indicating that
            return "No such champion";
        }
    }

    /** returns whether champion is in reserve
     * @param nme champion's name
     * @return true if champion in reserve, false otherwise
     */
    public boolean isInReserve(String name) {
        // Check if the champion exists in the champions map
        if (champions.containsKey(name)) {
            // Check if the champion is not in the team (in reserve)
            return !team.contains(name);
        } else {
            // If the champion doesn't exist in the champions map, it's not in reserve
            return false;
        }
    }

    // ***************** Team champions ************************
    /** Allows a champion to be entered for the vizier's team, if there
     * is enough money in the Treasury for the entry fee.The champion's
     * state is set to "active"
     * 0 if champion is entered in the vizier's team,
     * 1 if champion is not in reserve,
     * 2 if not enough money in the treasury,
     * -1 if there is no such champion
     * @param nme represents the name of the champion
     * @return as shown above
     **/
    public int enterChampion(String name) {
        // Check if the champion exists
        if (champions.containsKey(name)) {
            // Check if the champion is in reserve
            if (!team.contains(name)) {
                // Retrieve the champion object
                Champion champion = champions.get(name);
                // Check if there is enough money in the treasury for the entry fee
                if (treasury >= ENTRY_FEE) {
                    // Deduct the entry fee from the treasury
                    treasury -= ENTRY_FEE;
                    // Set the champion's state to "active"
                    champion.setState("active");
                    // Add the champion to the vizier's team
                    team.add(name);
                    return 0; // Champion entered successfully
                } else {
                    return 2; // Not enough money in the treasury
                }
            } else {
                return 1; // Champion is not in reserve
            }
        } else {
            return -1; // No such champion
        }
    }

    /** Returns true if the champion with the name is in
     * the vizier's team, false otherwise.
     * @param nme is the name of the champion
     * @return returns true if the champion with the name
     * is in the vizier's team, false otherwise.
     **/
    public boolean isInViziersTeam(String name) {
        // Check if the champion with the given name is in the vizier's team
        return team.contains(name);
    }

    /** Removes a champion from the team back to the reserves (if they are in the team)
     * Pre-condition: isChampion()
     * 0 - if champion is retired to reserves
     * 1 - if champion not retired because disqualified
     * 2 - if champion not retired because not in team
     * -1 - if no such champion
     * @param nme is the name of the champion
     * @return as shown above
     **/
    public int retireChampion(String name) {
        // Check if the champion exists
        if (champions.containsKey(name)) {
            // Check if the champion is in the team
            if (team.contains(name)) {
                // Check if the champion is disqualified
                Champion champion = champions.get(name);
                if (champion.isDisqualified()) {
                    return 1; // Champion not retired because disqualified
                }
                // Remove the champion from the team
                team.remove(name);
                // Set the champion's state to "retired"
                champion.setState("retired");
                return 0; // Champion retired to reserves successfully
            } else {
                return 2; // Champion not retired because not in team
            }
        } else {
            return -1; // No such champion
        }
    }

    /**Returns a String representation of the champions in the vizier's team
     * or the message "No champions entered"
     * @return a String representation of the champions in the vizier's team
     **/
    public String getTeam() {
        StringBuilder teamString = new StringBuilder();

        // Check if the team is empty
        if (team.isEmpty()) {
            return "No champions entered";
        } else {
            // Append header for vizier's team
            teamString.append("************ Vizier's Team of champions ********\n");

            // Append each champion name to the teamString
            for (String championName : team) {
                teamString.append(championName).append("\n");
            }

            return teamString.toString();
        }
    }

    /**Returns a String representation of the disqualified champions in the vizier's team
     * or the message "No disqualified champions "
     * @return a String representation of the disqualified champions in the vizier's team
     **/
    public String getDisqualified() {
        StringBuilder disqualifiedString = new StringBuilder();

        // Check if there are no disqualified champions
        boolean hasDisqualifiedChampions = false;

        // Append header for disqualified champions
        disqualifiedString.append("************ Vizier's Disqualified champions ********\n");

        // Iterate through each champion in the team
        for (String championName : team) {
            Champion champion = champions.get(championName);
            // Check if the champion is disqualified
            if (champion.isDisqualified()) {
                // Append the name of the disqualified champion to the disqualifiedString
                disqualifiedString.append(championName).append("\n");
                hasDisqualifiedChampions = true;
            }
        }

        // Check if there are no disqualified champions
        if (!hasDisqualifiedChampions) {
            return "No disqualified champions";
        } else {
            return disqualifiedString.toString();
        }
    }

    //**********************Challenges*************************
    /** returns true if the number represents a challenge
     * @param num is the  number of the challenge
     * @return true if the  number represents a challenge
     **/
    public boolean isChallenge(int num) {
        // Assuming challenges are stored in a map with challenge numbers as keys
        return challenges.containsKey(num);
    }
    /** Provides a String representation of an challenge given by
     * the challenge number
     * @param num the number of the challenge
     * @return returns a String representation of a challenge given by
     * the challenge number
     **/
    public String getChallenge(int num) {
        // Check if the challenge number exists in the challenges map
        if (challenges.containsKey(num)) {
            // Retrieve the challenge description from the challenges map
            String challengeDescription = challenges.get(num);
            return challengeDescription;
        } else {
            // If the challenge number doesn't exist, return a message indicating so
            return "No such challenge";
        }
    }
    /** Provides a String representation of all challenges
     * @return returns a String representation of all challenges
     **/
    public String getAllChallenges() {
        StringBuilder allChallengesString = new StringBuilder();

        // Append header for all challenges
        allChallengesString.append("************ All Challenges ************\n");

        // Iterate through each challenge in the challenges map
        for (Map.Entry<Integer, String> entry : challenges.entrySet()) {
            int challengeNumber = entry.getKey();
            String challengeDescription = entry.getValue();
            // Append challenge number and description to the allChallengesString
            allChallengesString.append("Challenge ").append(challengeNumber).append(": ").append(challengeDescription).append("\n");
        }

        return allChallengesString.toString();
    }

    // Add methods from CARE interface
}


/** Retrieves the challenge represented by the challenge
      * number.Finds a champion from the team who can meet the
      * challenge. The results of meeting a challenge will be
      * one of the following:
      * 0 - challenge won by champion, add reward to the treasury,
      * 1 - challenge lost on skills  - deduct reward from
      * treasury and record champion as "disqualified"
      * 2 - challenge lost as no suitable champion is  available, deduct
      * the reward from treasury
      * 3 - If a challenge is lost and vizier completely defeated (no money and
      * no champions to withdraw)
      * -1 - no such challenge
      * @param chalNo is the number of the challenge
      * @return an int showing the result(as above) of fighting the challenge
      */
    public int meetChallenge(int chalNo)
    {
        //Nothing said about accepting challenges when bust
        int outcome = -1 ;
        
        return outcome;
    }
 

    //****************** private methods for Task 3 functionality*******************
    //*******************************************************************************
    private void setupChampions() {
        champions = new HashMap<>();

        champions.put("Ganfrank", new Champion("Ganfrank", 7, true, "wizard", "transmutation"));
        champions.put("Rudolf", new Champion("Rudolf", 6, true, "wizard", "invisibility"));
        champions.put("Elblond", new Champion("Elblond", 1, false, "warrior", "sword"));
        champions.put("Flimsi", new Champion("Flimsi", 2, false, "warrior", "bow"));
        champions.put("Drabina", new Champion("Drabina", 7, false, "dragon", null));
        champions.put("Golum", new Champion("Golum", 7, true, "dragon", null));
        champions.put("Argon", new Champion("Argon", 9, false, "warrior", "mace"));
        champions.put("Neon", new Champion("Neon", 2, false, "wizard", "translocation"));
        champions.put("Xenon", new Champion("Xenon", 7, true, "dragon", null));
        champions.put("Atlanta", new Champion("Atlanta", 5, false, "warrior", "bow"));
        champions.put("Krypton", new Champion("Krypton", 8, false, "wizard", "fireballs"));
        champions.put("Hedwig", new Champion("Hedwig", 1, true, "wizard", "flying"));



    }

    private void setupChallenges() {
        challenges = new ArrayList<>();

        challenges.add(new Challenge(1, "Magic", "Borg", 3, 100, true, true, true));
        challenges.add(new Challenge(2, "Fight", "Huns", 3, 120, true, false, true));
        challenges.add(new Challenge(3, "Mystery", "Ferengi", 3, 150, true, false, true));
        challenges.add(new Challenge(4, "Magic", "Vandal", 9, 200, true, false, false));
        challenges.add(new Challenge(5, "Mystery", "Borg", 7, 90, true, false, true));
        challenges.add(new Challenge(6, "Fight", "Goth", 8, 45, true, false, true));
        challenges.add(new Challenge(7, "Magic", "Frank", 10, 200, true, false, true));
        challenges.add(new Challenge(8, "Fight", "Sith", 10, 170, true, false, true));
        challenges.add(new Challenge(9, "Mystery", "Cardashian", 9, 300, true, false, true));
        challenges.add(new Challenge(10, "Fight", "Jute", 2, 300, true, true, true));
        challenges.add(new Challenge(11, "Magic", "Celt", 2, 250, true, true, true));
        challenges.add(new Challenge(12, "Mystery", "Celt", 1, 250, true, true, true));



    }
        
    // Possible useful private methods
//     private Challenge getAChallenge(int no)
//     {
//         
//         return null;
//     }
//    
//     private Champion getChampionForChallenge(Challenge chal)
//     {
//         
//         return null;
//     }

    //*******************************************************************************
    //*******************************************************************************
  
    /************************ Task 3.5 ************************************************/  
    
    // ***************   file write/read  *********************
    /**
     * reads challenges from a comma-separated textfile and stores in the game
     * @param filename of the comma-separated textfile storing information about challenges
     */
    public void readChallenges(String filename)
    { 
        
    }   
    
     /** reads all information about the game from the specified file 
     * and returns a CARE reference to a Tournament object, or null
     * @param fname name of file storing the game
     * @return the game (as a Tournament object)
     */
    public Tournament loadGame(String fname)
    {   // uses object serialisation 
       Tournament yyy = null;
       
       return yyy;
   } 
   
   /** Writes whole game to the specified file
     * @param fname name of file storing requests
     */
   public void saveGame(String fname){
        // uses object serialisation 
        
    }
 

}



