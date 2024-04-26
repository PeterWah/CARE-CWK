package cwk4; 


/**
 * Details of your team
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Teamwork
{
    private String[] details = new String[12];
    
    public Teamwork()
    {   // in each line replace the contents of the String 
        // with the details of your team member
        // Please list the member details alphabetically by surname 
        // i.e. the surname of member1 should come alphabetically 
        // before the surname of member 2...etc
        details[0] = "51";
        
        details[1] = "Wahome";
        details[2] = "Peter";
        details[3] = "20039938";

        details[4] = "Ahmed";
        details[5] = "Thouheid";
        details[6] = "21084799";

        details[7] = "Alom";
        details[8] = "Rifaat";
        details[9] = "22003415";


        details[10] = "surname of member4";
        details[11] = "first name of member4";
        details[12] = "SRN of member4";

    }
    
    public String[] getTeamDetails()
    {
        return details;
    }
    
    public void displayDetails()
    {
        for(String temp:details)
        {
            System.out.println(temp.toString());
        }
    }
}
        
