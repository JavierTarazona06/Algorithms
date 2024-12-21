import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class aImage{
    static class Point implements Cloneable {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        protected Point clone(){
            return new Point(this.x, this.y);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    static class Line {
        public double m;
        public double b;
        public boolean isVertical;
        public double xConstant;

        public Line(Point p1, Point p2) {
            if (p2.x - p1.x == 0) {
                this.isVertical = true;
                this.xConstant = p1.x;
            } else {
                this.isVertical = false;
                this.m = (p2.y - p1.y) / (p2.x - p1.x); // Calculate the slope
                this.b = p1.y - m * p1.x; // Calculate the y-intercept
            }
        }

        public boolean isP_in_Line(Point p) {
            if (isVertical) {
                return Math.abs(p.x - xConstant) == 0;
            } else {
                double yOnLine = m * p.x + b;
                return Math.abs(p.y - yOnLine) == 0;
            }
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

    public static double crossProd(Point p1, Point p2){
        return (p1.x*p2.y) - (p1.y*p2.x);
    }

    public static boolean isPoinTriag(Point P, Point p1, Point p2, Point p3){
        Point cur_point1 = new Point(0,0);
        Point cur_point2 = new Point(0,0);

        cur_point1.x = p2.x - p1.x;
        cur_point1.y = p2.y - p1.y;
        cur_point2.x = P.x - p1.x;
        cur_point2.y = P.y - p1.y;
        double d1 = crossProd(cur_point1, cur_point2);

        cur_point1.x = p3.x - p2.x;
        cur_point1.y = p3.y - p2.y;
        cur_point2.x = P.x - p2.x;
        cur_point2.y = P.y - p2.y;
        double d2 = crossProd(cur_point1, cur_point2);

        cur_point1.x = p1.x - p3.x;
        cur_point1.y = p1.y - p3.y;
        cur_point2.x = P.x - p3.x;
        cur_point2.y = P.y - p3.y;
        double d3 = crossProd(cur_point1, cur_point2);

        boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(hasNeg && hasPos);
    }

    public static boolean traingles(int n, Point[] points){
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
        Point p1 = points[0].clone();
        Point p2 = points[0].clone();
        Point p3 = points[0].clone();

        int min_x, max_x, min_y, max_y;
        min_x = p1.x;
        max_x = p1.x;
        min_y = p1.y;
        max_y = p1.y;

        if (p2.x < min_x){min_x = p2.x;
        } else if (p2.x > max_x){ max_x = p2.x;}
        if (p2.y < min_y){min_y = p2.y;
        } else if (p2.y > max_y){ max_y = p2.y;}

        if (p3.x < min_x){min_x = p3.x;
        } else if (p3.x > max_x){ max_x = p3.x;}
        if (p3.y < min_y){min_y = p3.y;
        } else if (p3.y > max_y){ max_y = p3.y;}

        Point[] viable_vertex = new Point[3];
        double curr_less_area = trial_area;
        Point H = new Point(0,0);
        int k_win = 0;

        ArrayList<Point> points_inAB = new ArrayList<>();
        ArrayList<Point> points_inBC = new ArrayList<>();
        ArrayList<Point> points_inCA = new ArrayList<>();

        Line lineAB = new Line(p1, p2);
        Line lineBC = new Line(p2, p3);
        Line lineCA = new Line(p3, p1);

        for (int i = min_y; i < max_y+1; i++){
            for (int j = min_x; j < max_x+1; j++){
                Point current_point = new Point(0,0);
                current_point.x = j; current_point.y = i;

                if (!((Objects.equals(current_point.toString(), p1.toString())) ||
                        Objects.equals(current_point.toString(), p2.toString()) ||
                        Objects.equals(current_point.toString(), p3.toString()))) {
                    if (lineAB.isP_in_Line(current_point)) {
                        points_inAB.add(current_point);
                    }
                    if (lineBC.isP_in_Line(current_point)) {
                        points_inBC.add(current_point);
                    }
                    if (lineCA.isP_in_Line(current_point)) {
                        points_inCA.add(current_point);
                    }

                    if (isPoinTriag(current_point.clone(), p1.clone(), p2.clone(), p3.clone())) {
                        for (int k = 0; k < 3; k++) {
                            Point[] possible_vertex = new Point[3];
                            if (k == 0) {
                                possible_vertex[0] = current_point.clone();
                                possible_vertex[1] = p1.clone();
                                possible_vertex[2] = p2.clone();
                            } else if (k == 1) {
                                possible_vertex[0] = current_point.clone();
                                possible_vertex[1] = p1.clone();
                                possible_vertex[2] = p3.clone();
                            } else {
                                possible_vertex[0] = current_point.clone();
                                possible_vertex[1] = p2.clone();
                                possible_vertex[2] = p3.clone();
                            }
                            double curr_area = calc_area(possible_vertex);

                            if ((curr_area >= area_min) && (curr_area < curr_less_area)) {
                                viable_vertex[0] = possible_vertex[0].clone();
                                viable_vertex[1] = possible_vertex[1].clone();
                                viable_vertex[2] = possible_vertex[2].clone();
                                H.x = possible_vertex[0].x; H.y = possible_vertex[0].y;
                                curr_less_area = curr_area;
                                k_win = k;
                            }
                        }
                    }
                }
            }
        }

        if (newTrianglesNum == 2){
            /*Tiene dos hijos, 2 triangulos nuevos*/
            curr_less_area = trial_area;

            boolean flag = true;
            boolean found = false;
            /*For First Vector A*/
            Point M = new Point(0, 0);

            H = points_inBC.getFirst().clone();
            Point[] possible_vertex = {p1.clone(), p2.clone(), H.clone()};

            for (int i=1; i < points_inBC.size(); i++){
                double curr_area = calc_area(possible_vertex);

                if ((curr_area >= area_min) && (curr_area < curr_less_area)) {
                    Point[] vertex1 = {p1.clone(), p3.clone(), H.clone()};
                    traingles(indexOrder.get(0), possible_vertex);
                    traingles(indexOrder.get(1), possible_vertex);
                    traingles(indexOrder.get(2), vertex1);
                    found = true;
                    return true;
                }

                possible_vertex[0] = p1.clone();
                possible_vertex[1] = H.clone();
                M = points_inBC.get(i);
                possible_vertex[2] = M.clone();
            }

            /*From AB*/
            Point N = new Point(0,0);
            for (int i=0; i<points_inAB.size(); i++){
                N = points_inAB.get(i).clone();
                for (int j=0; j<points_inBC.size()-1; j++){
                    H = points_inBC.get(j).clone();
                    M = points_inBC.get(j+1).clone();

                    possible_vertex [0] = N.clone();
                    possible_vertex [1] = H.clone();
                    possible_vertex [2] = M.clone();
                    double curr_area = calc_area(possible_vertex);

                    if ((curr_area >= area_min) && (curr_area < curr_less_area)) {
                        Point[] vertex1 = {p1.clone(), p3.clone(), H.clone()};
                        traingles(indexOrder.get(0), possible_vertex);
                        traingles(indexOrder.get(1), possible_vertex);
                        traingles(indexOrder.get(2), vertex1);
                        found = true;
                        return true;
                    }
                }
            }



        }

        if ((curr_less_area == trial_area) || (newTrianglesNum == 2)){
            /*No encontró puntos internos o tiene dos hijos, 2 triangulos nuevos*/
            curr_less_area = trial_area;

            /*For First Vector A*/
            Point M = new Point(0,0);
            H = points_inBC.getFirst();




            for (int i=0; i<points_inAB.size(); i++){


                if (newTrianglesNum==2){Point[] possible_vertex = {p1.clone(), p2.clone(), H.clone()};}
                else{
                    Point[] possible_vertex = {p1.clone(), p2.clone(), H.clone()};
                }

                /*double curr_area = calc_area(possible_vertex);*/

            }

        } else {
            if (newTrianglesNum == 3){
                Point[] vertex1 = {H.clone(), p1.clone(), p2.clone()};
                Point[] vertex2 = {H.clone(), p1.clone(), p3.clone()};
                Point[] vertex3 = {H.clone(), p2.clone(), p3.clone()};

                double area1 = calc_area(vertex1);
                double area2 = calc_area(vertex2);
                double area3 = calc_area(vertex3);

                ArrayList<Point[]> all_vertex = new ArrayList<>();
                ArrayList<Double> all_areas = new ArrayList<>();

                all_vertex.addFirst(vertex1);
                all_areas.addFirst(area1);

                if (area2 < all_areas.getFirst()){
                    all_vertex.addFirst(vertex2);
                    all_areas.addFirst(area2);
                } else {
                    all_vertex.add(vertex2);
                    all_areas.add(area2);
                }

                if (area3 < all_areas.getFirst()){
                    all_vertex.addFirst(vertex3);
                    all_areas.addFirst(area3);
                } else if (area3 < all_areas.get(1)){
                    all_vertex.add(1, vertex3);
                    all_areas.add(1, area3);
                } else {
                    all_vertex.add(vertex3);
                    all_areas.add(area3);
                }

                traingles(indexOrder.get(0), all_vertex.get(0));
                traingles(indexOrder.get(1), all_vertex.get(1));
                traingles(indexOrder.get(2), all_vertex.get(2));
            }


        }

        System.out.println("Curr less area: "+ curr_less_area);

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