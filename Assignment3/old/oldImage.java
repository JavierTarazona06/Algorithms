import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class oldImage{
    static class Point{
        int x, y;

        Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString(){
            return "(" + x + "," + y + ")";
        }
    }


    static ArrayList<Integer> tree;
    static ArrayList<Double> areas;
    static ArrayList<String> solutions;

    public static double areas(int n){
        if (tree.get(n) == 0){
            areas.set(n, 0.0);
            return 0;
        }

        double area_count = 0;

        if (3*n+1 < tree.size()){
            area_count += areas(3*n + 1);
            area_count += areas(3*n + 2);
            area_count += areas(3*n + 3);
        }

        if (area_count == 0){
            areas.set(n, 0.5);
            return 0.5;
        }

        areas.set(n, area_count);
        return area_count;
    }

    public static double calc_area(Point[] points){
        double t1, t2, t3;
        t1 = points[0].x * (points[1].y - points[2].y);
        t2 = points[1].x * (points[2].y - points[0].y);
        t3 = points[2].x * (points[0].y - points[1].y);
        return (1.0/2.0)*Math.abs((t1 +t2 +t3));
    }

    public static double calc_distance(Point p1, Point p2){
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    public static double[] syst_equa_2_2(double[] A, double[] B){
        double a1 = A[0], b1 = A[1], c1 = A[2];
        double a2 = B[0], b2 = B[1], c2 = B[2];

        double det = (a1* b2) - (a2*b1);
        double det1, det2, x, y;
        double[] rest = new double[2];

        if (det == 0){
            System.out.println("No solution!! in syst_equa_2_2");
        } else {
            det1 = (c1 * b2) - (c2 * b1);
            det2 = (a1 * c2) - (a2 * c1);

            x = det1 / det;
            y = det2 / det;

            rest[0] = x;
            rest[1] = y;
        }

        return rest;
    }

    public static boolean traingles(int n, Point[] points) {
        if (tree.get(n) == 0){
            solutions.set(n, n+" 0");
            return true;
        }

        double trial_area = calc_area(points);
        double cur_area = areas.get(n);
        if (cur_area > trial_area) {
            return false;
        }
        solutions.set(n, n+" "+points[0]+","+points[1]+","+points[2]);

        int child_index = (3 * n) + 1;
        if (child_index > tree.size()-1) {
            return true;
        }
        int newTrianglesNum = tree.get(child_index) + tree.get(child_index + 1) + tree.get(child_index + 2);

        Point[] point_a = null;
        if (newTrianglesNum == 0){
            traingles(child_index, point_a);
            traingles(child_index+1, point_a);
            traingles(child_index+2, point_a);
            return true;
        }

        int index_area_min = 3 * n + 1;
        ArrayList<Integer> indexOrder = new ArrayList<>();

        indexOrder.addFirst(3 * n + 1);
        double area_min = areas.get(3 * n + 1);

        if (areas.get(3 * n + 2) < area_min) {
            index_area_min = 3 * n + 2;
            indexOrder.addFirst(3 * n + 2);
            area_min = areas.get(3 * n + 2);
        } else {
            indexOrder.add(3 * n + 2);
        }

        if (areas.get(3 * n + 3) < area_min) {
            index_area_min = 3 * n + 3;
            indexOrder.addFirst(3 * n + 3);
            area_min = areas.get(3 * n + 3);
        } else if (areas.get(3 * n + 3) < areas.get(indexOrder.get(1))){
            indexOrder.add(1, 3 * n + 3);
        } else {
            indexOrder.add(3 * n + 3);
        }

        /*One child*/
        if (newTrianglesNum == 1){
            traingles(indexOrder.get(0), points);
            traingles(indexOrder.get(1), points);
            traingles(indexOrder.get(2), points);
            return true;
        }

        /*2 or 3 chidren*/

        double[] bases = new double[3];
        bases[0] = calc_distance(points[0], points[1]);
        bases[1] = calc_distance(points[1], points[2]);
        bases[2] = calc_distance(points[2], points[0]);
        int i = 0;
        Point A, B, C, H;
        double h, m, b;
        double[] coef1= new double[3] , coef2 = new double[3];
        Point[][] areasOrder_points = new Point[3][3];

        for (; i < 3; i++) {
            if (i==0){
                A = points[0];
                B = points[1];
                C = points[2];
            } else if (i == 1){
                A = points[1];
                B = points[2];
                C = points[0];
            } else {
                A = points[2];
                B = points[0];
                C = points[1];
            }

            if (newTrianglesNum == 2){
                int dx = B.x - A.x;
                int dy = B.y - A.y;

                int steps = Math.max(Math.abs(dx), Math.abs(dy));

                double t;
                i=0;
                H = new Point(0,0);

                for (i=0; i<= steps; i++){
                    t = (double) i / steps;
                    int new_x = (int) Math.ceil(A.x + (t * dx));
                    int new_y = (int) Math.ceil(A.y + (t * dy));

                    H.x = new_x;
                    H.y = new_y;

                    Point[] cur_points = {A, C, H};
                    if (Math.abs(calc_area(cur_points) - area_min) < 0.01){
                        break;
                    }
                }

                areasOrder_points[0] = null;
                areasOrder_points[1] = new Point[]{A, C, H};
                areasOrder_points[2] = new Point[]{B, C, H};

            }

            h = Math.ceil((2* area_min) / bases[i]);

            if ((B.x - A.x) == 0){
                continue;
            }

            m = (double) (B.y - A.y) / (B.x - A.x);
            b = A.y - (m* A.x);

            if (m==0){
                H = new Point(C.x, (int) (A.y+h));
            } else {
                coef1[0] = 1/m;
                coef1[1] = 1.0;
                coef1[2] = (double) ((A.y + B.y) /2) + ((A.x + B.x)/(2*m));

                coef2[0] = m;
                coef2[1] = -1.0;
                coef2[2] = (h*Math.sqrt(Math.pow(m,2)+1)) - b;

                double[] result = syst_equa_2_2(coef1, coef2);
                H = new Point((int) Math.ceil(result[0]), (int) Math.ceil(result[1]));
            }

            Point[] ps1 = {A, B, H};
            Point[] ps2 = {A, C, H};
            Point[] ps3 = {C, B, H};

            double area_ps1, area_ps2, area_ps3;
            area_ps1 = calc_area(ps1);
            area_ps2 = calc_area(ps2);
            area_ps3 = calc_area(ps3);
            int areasInZero = 0;
            areasInZero = (area_ps1==0.0) ? areasInZero+1 : areasInZero;
            areasInZero = (area_ps2==0.0) ? areasInZero+1 : areasInZero;
            areasInZero = (area_ps3==0.0) ? areasInZero+1 : areasInZero;

            if (areasInZero > 1){
                continue;
            } else if (areasInZero == 1){
                int dx = C.x - A.x;
                int dy = C.y - A.y;

                int x_h1 = Math.abs(H.x - A.x);
                int x_h2 = Math.abs(H.x - C.x);
                int steps1 = Math.max(x_h1, x_h2);
                int y_h1 = Math.abs(H.y - A.y);
                int y_h2 = Math.abs(H.y - C.y);
                int steps2 = Math.max(y_h1, y_h2);

                int steps = Math.max(steps1, steps2);
                /*int steps = Math.max(Math.abs(dx), Math.abs(dy));*/
                double t;
                int s = 1;
                i=0;
                Point M = new Point(0,0);
                Point[] cur_points = {A, M, H};

                while (i <= steps) {
                    t = i/steps;
                    int new_x = (int) (H.x + (s)*(t * dx));
                    int new_y = (int) (H.y + (s)*(t * dy));
                    M.x = new_x;
                    M.y = new_y;
                    cur_points[1] = M;

                    boolean equals1 = Objects.equals(M.toString(), A.toString());
                    boolean equals2 = Objects.equals(M.toString(), C.toString());
                    if (!((calc_area(cur_points) >= area_min) && (!equals1) && (!equals2))) {
                        if (s > 0){s = (-1)*s;}
                        else {
                            i++;
                            s = (-1)*s;
                        }
                    } else {
                        break;
                    }
                }

                for (i=0; i<= steps; i++){
                    t = (double) i / steps;
                    int new_x = (int) Math.ceil(A.x + (t * dx));
                    int new_y = (int) Math.ceil(A.y + (t * dy));

                    M.x = new_x;
                    M.y = new_y;

                    cur_points = new Point[]{A, M, H};
                    if (calc_area(cur_points) < area_min){
                        i--;
                        t = (double) i / steps;
                        new_x = (int) Math.ceil(B.x + (t * dx));
                        new_y = (int) Math.ceil(B.y + (t * dy));
                        M.x = new_x;
                        M.y = new_y;
                        cur_points[1] =M;
                        i++;

                        if ((Objects.equals(M.toString(), B.toString())) || (Objects.equals(M.toString(), C.toString()))){
                            i++;
                            t = (double) i / steps;
                            new_x = (int) Math.ceil(B.x + (t * dx));
                            new_y = (int) Math.ceil(B.y + (t * dy));
                            M.x = new_x;
                            M.y = new_y;
                            cur_points[1] =M;
                        }

                        ps1[1] = M;
                        ps2[2] = M;
                        ps3[0] = A;
                        /*Expected
                        ps1 = {A, M, H};
                        ps2 = {A, C, M};
                        ps3 = {A, B, H};*/
                        break;
                    }
                }

                area_ps1 = calc_area(ps1);
                area_ps2 = calc_area(ps2);
                area_ps3 = calc_area(ps3);

            }

            areasOrder_points[0] = ps1;

            if (area_ps1 >= area_min){
                double sum_area = area_ps1 + area_ps2 + area_ps3;
                if (sum_area <= trial_area){
                    if (area_ps2 < area_ps3){
                        areasOrder_points[1] = ps2;
                        areasOrder_points[2] = ps3;
                    } else {
                        areasOrder_points[1] = ps3;
                        areasOrder_points[2] = ps2;
                    }
                    break;
                }
            }
        }

        if (newTrianglesNum == 2){
            traingles(indexOrder.get(0), areasOrder_points[0]);
            traingles(indexOrder.get(1), areasOrder_points[1]);
            traingles(indexOrder.get(2), areasOrder_points[2]);
        }
        if (newTrianglesNum == 3) {
            traingles(indexOrder.get(0), areasOrder_points[0]);
            traingles(indexOrder.get(1), areasOrder_points[1]);
            traingles(indexOrder.get(2), areasOrder_points[2]);
        }

        return true;
    }


    
    public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);

    /*Reading Tree*/
    String tree_input = scan.nextLine();
    String[] tree_split = tree_input.split(",\\s*");
    tree = new ArrayList<>();
    areas = new ArrayList<>();
    solutions = new ArrayList<>();
    for (String item : tree_split){
        tree.add(Integer.parseInt(item));
        areas.add(0.0);
        solutions.add("");
    }



    String points_input = scan.nextLine();
    Pattern point_pattern = Pattern.compile("\\((\\d+),(\\d+)\\)");
    Matcher point_match = point_pattern.matcher((points_input));

    Point[] points = new Point[3];

    int i = 0;
    int x, y;
    while(point_match.find()){
        x = Integer.parseInt(point_match.group(1));
        y = Integer.parseInt(point_match.group(2));
        points[i++] = new Point(x, y);
    }

    Point P1, P2, P3;
    P1 = points[0]; P2 = points[1]; P3 = points[2];
    Point[] points_root = {P1, P2, P3};

    int node = 0;

    double total_area_req = areas(node);

    if (!traingles(node, points_root)){
        System.out.println("None");
    } else {
        for (int j=0; j<tree.size(); j++){
            System.out.println(solutions.get(j));
        }
    }

    scan.close();
    }
}