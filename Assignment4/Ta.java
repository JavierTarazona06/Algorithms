import java.util.Scanner;


public class Ta {
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        String tree = myObj.nextLine();
        String[] tokens = tree.replaceAll("\\s+","").split(",");
        //you can uncomment and see what happens but uncomment this when you submit your code
        for (String t : tokens){
            System.out.println(Integer.parseInt(t));
        }

        String triangle = myObj.nextLine();
        tokens = triangle.replaceAll("[\\s+()]","").split(",");
        //you can uncomment and see what happens but uncomment this when you submit your code
        for (String t : tokens)
        System.out.println(Integer.parseInt(t));

        //write your code
    }
}