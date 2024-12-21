import java.util.Objects;
import java.util.Scanner;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Anish Bhagat, cyn482, 11337909
// Heer Patel, ieq782, 11358904
//Javier Tarazona, elr490, 11411898

class View {

    public static String createHTMLTriangles(List<int[][]> triangles) {
        int width, height;
        StringBuilder trianglesStr = new StringBuilder();
        width = 0;
        height = 0;

        trianglesStr.append("<h1>Triangles List</h1>\n");

        int i = 0;
        for (int[][] triangle : triangles) {
            trianglesStr.append("<h2>Triangle Number %s</h2>\n".formatted(i));
            trianglesStr.append("<p>");
            for (int[] vertex : triangle) {
                trianglesStr.append("(%s,%s),".formatted(vertex[0], vertex[1]));
                if (vertex[0]*50 > width){
                    width = vertex[0]*50;
                }
                if (vertex[1]*50 > height){
                    height = vertex[1]*50;
                }
            }
            trianglesStr.append("</p>\n");
            i++;
        }

        StringBuilder svg = new StringBuilder();
        svg.append("<svg width='%s' height='%s' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 %s %s'>\n"
                .formatted(width,height, width, height));

        for (int[][] triangle : triangles) {
            svg.append("<polygon points='");
            for (int[] vertex : triangle) {
                svg.append(vertex[0]*50).append(",").append(height-vertex[1]*50).append(" ");
            }
            svg.append("' style='fill:none;stroke:black;stroke-width:2'/>\n");
        }

        svg.append("</svg>");

        return """
               <!DOCTYPE html>
               <html>
               <head>
                   <title>CMPT360 - elr490</title>
               </head>
               <body>
                   <h1>CMPT360 - Assignment 4 - Q5</h1>
                   <h1>elr490 - Javier Tarazona - 11411898</h1>
                   <h1>Drawing of Triangles</h1>
                   %s
                   %s
               </body>
               </html>
               """.formatted(svg.toString(), trianglesStr);
    }

    public static void writeFile(String toWrite){
        try (FileWriter writer = new FileWriter("T.html")) {
            writer.write(toWrite);
            System.out.println("HTML file created with the drawings of Triangles: T.html");
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error creating HTML file");
        }
    }
}

class Triangle {
    int[] x = new int[3];
    int[] y = new int[3];

    // Constructor for triangle using three points
    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        x[0] = x1;
        y[0] = y1;
        x[1] = x2;
        y[1] = y2;
        x[2] = x3;
        y[2] = y3;
    }
    @Override
    public String toString() {
        return String.format("(%d,%d),(%d,%d),(%d,%d)", x[0], y[0], x[1], y[1], x[2], y[2]);
    }

    
    // Method to calculate the Euclidean distance between two points
    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    private double area(int x1, int y1, int x2, int y2, int x3, int y3) {
        return Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0;
    }
    // Method to check if the triangle can be partitioned further
    public boolean canBePartitioned() {
        // Compute the side lengths
        double side1 = distance(x[0], y[0], x[1], y[1]);
        double side2 = distance(x[1], y[1], x[2], y[2]);
        double side3 = distance(x[2], y[2], x[0], y[0]);

        // Check if any side is smaller than a given threshold (e.g., 1 unit)
        boolean canBePartitioned = (side1 >= 2.0) && (side2 >= 2.0 && side3 >= 2.0);
        return canBePartitioned;
    }
    // Method to check if a given point lies inside the triangle using the Area Method
    private boolean checkIfInsideUsingAreaMethod(int xP, int yP) {
        double fullArea = area(x[0], y[0], x[1], y[1], x[2], y[2]);
        double area1 = area(xP, yP, x[1], y[1], x[2], y[2]);
        double area2 = area(x[0], y[0], xP, yP, x[2], y[2]);
        double area3 = area(x[0], y[0], x[1], y[1], xP, yP);

        return ((fullArea == (area1 + area2 + area3)) && area1 > 0 && area2 > 0 && area3 > 0); // Strict equality for inside check

    }
    // Partition this triangle into 3 sub-triangles
    public Triangle[] partition() {
        if (!canBePartitioned()) {
            return null;    // Cannot partition further
        }

        // Calculate the centroid of the triangle
        int xcenter = (x[0] + x[1] + x[2]) / 3;
        int ycenter = (y[0] + y[1] + y[2]) / 3;

        // Create the sub-triangles based on centroid partitioning
        Triangle[] subTriangles = new Triangle[3];
        // if area of subtriangle is not zero, then we have a valid midpoint
        if(checkIfInsideUsingAreaMethod(xcenter, ycenter)){
            subTriangles[0] = new Triangle(x[0], y[0], x[1], y[1], xcenter, ycenter); // Sub-triangle 1
            subTriangles[1] = new Triangle(x[1], y[1], x[2], y[2], xcenter, ycenter); // Sub-triangle 2
            subTriangles[2] = new Triangle(x[2], y[2], x[0], y[0], xcenter, ycenter); // Sub-triangle 3
        }else{
            // else we have to navigate to right midpoint, so try all directions then check if valid midpoint
            // go left
            if(checkIfInsideUsingAreaMethod(xcenter + 1, ycenter)){
                subTriangles[0] = new Triangle(x[0], y[0], x[1], y[1], xcenter + 1, ycenter); // Sub-triangle 1
                subTriangles[1] = new Triangle(x[1], y[1], x[2], y[2], xcenter + 1, ycenter); // Sub-triangle 2
                subTriangles[2] = new Triangle(x[2], y[2], x[0], y[0], xcenter + 1, ycenter); // Sub-triangle 3
                return subTriangles;
            }
            // go right
            if(checkIfInsideUsingAreaMethod(xcenter - 1, ycenter)){
                subTriangles[0] = new Triangle(x[0], y[0], x[1], y[1], xcenter - 1, ycenter); // Sub-triangle 1
                subTriangles[1] = new Triangle(x[1], y[1], x[2], y[2], xcenter - 1, ycenter); // Sub-triangle 2
                subTriangles[2] = new Triangle(x[2], y[2], x[0], y[0], xcenter - 1, ycenter); // Sub-triangle 3
                return subTriangles;
            }
            // go up
            if(checkIfInsideUsingAreaMethod(xcenter, ycenter + 1)){
                subTriangles[0] = new Triangle(x[0], y[0], x[1], y[1], xcenter, ycenter + 1); // Sub-triangle 1
                subTriangles[1] = new Triangle(x[1], y[1], x[2], y[2], xcenter, ycenter + 1); // Sub-triangle 2
                subTriangles[2] = new Triangle(x[2], y[2], x[0], y[0], xcenter, ycenter + 1); // Sub-triangle 3
                return subTriangles;
            }
            // go down
            if(checkIfInsideUsingAreaMethod(xcenter, ycenter - 1)){
                subTriangles[0] = new Triangle(x[0], y[0], x[1], y[1], xcenter, ycenter - 1); // Sub-triangle 1
                subTriangles[1] = new Triangle(x[1], y[1], x[2], y[2], xcenter, ycenter - 1); // Sub-triangle 2
                subTriangles[2] = new Triangle(x[2], y[2], x[0], y[0], xcenter, ycenter - 1); // Sub-triangle 3
                return subTriangles;
            }
        }
        return subTriangles;
    }
}

public class Thtml {
    // Flag to indicate if any triangle has duplicates
    public static boolean hasDuplicateTriangles = false;

    // Recursive function to assign triangles to tree nodes
    public static void assignTriangles(int[] T, int index, Triangle triangle, String[] results) {
        if(triangle == null){
            results[index] = index + " 0";
            return;
        }
        // If the node does not exist, return "0"
        if (T[index] == 0) {
            results[index] = index + " 0";
            return;
        }
        // Store the current triangle's result
        results[index] = index + " " + triangle;

        // Partition the triangle into 3 sub-triangles
        Triangle[] subTriangles = null;
        if(triangle.canBePartitioned()){
            subTriangles = triangle.partition();
        }
        else{
            return;
        }
        // Calculate child indices
        int child1 = 3 * index + 1;
        int child2 = 3 * index + 2;
        int child3 = 3 * index + 3;

        // Assign triangles for each child if they exist
        if (child1 < T.length) assignTriangles(T, child1, subTriangles[0], results);
        if (child2 < T.length) assignTriangles(T, child2, subTriangles[1], results);
        if (child3 < T.length) assignTriangles(T, child3, subTriangles[2], results);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Read the first line and parse it into an int array T
        String firstLine = in.nextLine().trim();
        String[] tValues = firstLine.split(",");
        int[] T = new int[tValues.length];
        for (int i = 0; i < tValues.length; i++) {
            T[i] = Integer.parseInt(tValues[i].trim()); // Convert and trim each value
        }


        // Parse the second line for root triangle coordinates
        String secondLine = in.nextLine().trim();
        String[] pointValues = secondLine.replace("(", "").replace(")", "").split(",");
        int x1 = Integer.parseInt(pointValues[0].trim());
        int y1 = Integer.parseInt(pointValues[1].trim());
        int x2 = Integer.parseInt(pointValues[2].trim());
        int y2 = Integer.parseInt(pointValues[3].trim());
        int x3 = Integer.parseInt(pointValues[4].trim());
        int y3 = Integer.parseInt(pointValues[5].trim());

        // Create the root triangle using the parsed coordinates
        Triangle rootTriangle = new Triangle(x1, y1, x2, y2, x3, y3);


        // Initialize an array to store results from indices 0 to 12
        String[] results = new String[T.length];

        // Start assigning triangles from the root
        assignTriangles(T, 0, rootTriangle, results);

        for (int i = results.length - 1; i > 0; i--) {
            if (T[i] == 1 && ( results[i] == null || results[i].endsWith("0") )) {
                System.out.println("None");
                return;
            }
        }
        for (int i = 0; i < results.length; i++) {
            if (results[i] != null) {
                System.out.println(results[i]);
            }
        }


        List<int[][]> triangles = new ArrayList<>();

        for (int i = 0; i < results.length; i++) {
            String result = results[i];
            // Split the line by spaces and take the part after the index
            String[] parts = result.split(" ");
            String coordinates = parts[1];
            if (Objects.equals(coordinates, "0")){
                // There are no coordinates
                continue;
            }

            // Remove parentheses and split by commas
            String[] points = coordinates.split("\\),\\(");

            // Take each point
            int[][] triangle = new int[3][2];
            for (int j = 0; j < 3; j++) {
                String point = points[j].replaceAll("[()]", "");
                String[] coords = point.split(",");
                triangle[j][0] = Integer.parseInt(coords[0]);
                triangle[j][1] = Integer.parseInt(coords[1]);
            }

            // Add the triangle to the list
            triangles.add(triangle);
        }

        String htmlDrawingTriangles = View.createHTMLTriangles(triangles);
        View.writeFile(htmlDrawingTriangles);

    }
}