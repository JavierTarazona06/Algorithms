import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class View {

    public static String createHTMLTriangles(List<int[][]> triangles) {
        int width, height;
        width = 0;
        height = 0;

        for (int[][] triangle : triangles) {
            for (int[] vertex : triangle) {
                if (vertex[0]*50 > width){
                    width = vertex[0]*50;
                }
                if (vertex[1]*50 > height){
                    height = vertex[1]*50;
                }
            }
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
                   <h1>CMPT360 - Assigment 4 - Q5</h1>
                   <h1>elr490 - Javier Tarazona - 11411898</h1>
                   <h1>Drawing of Triangles</h1>
                   %s
               </body>
               </html>
               """.formatted(svg.toString());
    }

    public static void writeFile(String toWrite){
        try (FileWriter writer = new FileWriter("T.html")) {
            writer.write(toWrite);
            System.out.println("HTML file created with the drwaings of Triangles: T.html");
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error creating HTML file");
        }
    }
}

class Utils{
    public static double findAreaTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        return Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0;
    }
}

class Triangle {
    int[] x = new int[3];
    int[] y = new int[3];
    int thisArea;

    // Constructor for triangle using three points
    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        x[0] = x1;
        y[0] = y1;
        x[1] = x2;
        y[1] = y2;
        x[2] = x3;
        y[2] = y3;
        thisArea = Utils.findAreaTriangle(x1, y1, x2, y2, x3, y3);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d),(%d,%d),(%d,%d)", x[0], y[0], x[1], y[1], x[2], y[2]);
    }

    // Is the point (x, y) inside the triangle
    public boolean isInsideTriangle(int px, int py) {

        double A1 = Utils.findAreaTriangle(px, py, x[1], y[1], x[2], y[2]);
        double A2 = Utils.findAreaTriangle(x[0], y[0], px, py, x[2], y[2]);
        double A3 = Utils.findAreaTriangle(x[0], y[0], x[1], y[1], px, py);

        // If point is inside triangle, the sum of theareas is equal to the original
        return thisarea == A1 + A2 + A3;
    }

}

public class Thtml {

    public static void main(String[] args) {
        int width, height;
        width = 0;
        height = 0;
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
        if (x1 > width){width = x1;}
        int y1 = Integer.parseInt(pointValues[1].trim());
        if (y1 > height){height = y1;}
        int x2 = Integer.parseInt(pointValues[2].trim());
        if (x2 > width){width = x2;}
        int y2 = Integer.parseInt(pointValues[3].trim());
        if (y2 > height){height = y2;}
        int x3 = Integer.parseInt(pointValues[4].trim());
        if (x3 > width){width = x3;}
        int y3 = Integer.parseInt(pointValues[5].trim());
        if (y3 > height){height = y3;}

        // Create the root triangle using the parsed coordinates
        Triangle rootTriangle = new Triangle(x1, y1, x2, y2, x3, y3);


        List<int[][]> mytriangles = List.of(
            new int[][]{{6,8}, {0,0}, {12,0}},
            new int[][]{{6,8}, {0,0}, {8,2}},
            new int[][]{{12,0}, {0,0}, {8,2}}
        );

        // Draw tringles and create HTML File
        String htmlDrawingTriangles = View.createHTMLTriangles(mytriangles);
        Html.writeFile(htmlDrawingTriangles);
    }
}