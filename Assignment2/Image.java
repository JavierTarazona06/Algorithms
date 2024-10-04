import java.util.Scanner;
public class Image{
    
    public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);

    int NumOfCases = scan.nextInt();
    int[] Count= new int[31];
    
    Count[0] = 1;
    for (int i = 0; i < NumOfCases; i++){
        int x = scan.nextInt();
        //Do not edit anything above
    
        //write the NSIDs of the members of your group here:
        // elr490
        //Write your code below
        //BEGIN
        int[][] dp = new int[21][3];

        dp[0][0] = 1;
        dp[0][1] = 1;
        dp[0][2] = 1;

        for (int m = 1; m < 21; m++){
            for (int n=2; n > -1; n--){
                if (n == 2){
                    dp[m][n] = dp[m-1][1] + dp[m-1][2];
                } else if (n == 1){
                    dp[m][n] = dp[m-1][0] + dp[m][2];
                } else {
                    dp[m][n] = dp[m][1];
                }

            }
        }

        System.out.println(dp[x][0]);

        //END
        //Do not edit anything below 
        System.out.println(Count[x]);
    }
    
    scan.close();
    }
}