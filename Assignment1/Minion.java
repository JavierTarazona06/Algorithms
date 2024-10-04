import java.util.Scanner;
public class Minion{
    
    public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);

    int NumOfCases = scan.nextInt();
    int[] originalMoney = new int[31];
    
    originalMoney[0] = 0;
    for (int i = 0; i < NumOfCases; i++){
        int x = scan.nextInt();
        //Do not edit anything above
    
        //write the NSIDs of the members of your group here:
        //elr490
        //Write your code below
        //BEGIN
        int ans = 0;
        int pow = 1;
        for (int j=0; j<x; j++){
            ans = ans + pow;
            pow = pow * 2;
        }
        originalMoney[x] = ans;
        //END
        //Do not edit anything below 
        System.out.println(originalMoney[x]);
    }
    
    scan.close();
    }
}