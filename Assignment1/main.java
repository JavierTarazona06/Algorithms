import java.util.Scanner;

public class main{
    public static boolean is_prime(int n){
        int lim = (int) Math.sqrt(n);

        for (int i=2; i<= lim; i++){
            if (n%i == 0){
                System.out.println(i);
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args){
        int n = 98999;
        System.out.println(is_prime(n));
    }
}