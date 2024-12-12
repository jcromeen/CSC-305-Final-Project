import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.List;

public class DrawPanel extends JPanel {
    private final List<Square> squares;
    private final List<Connection> connections;
    private final List<UMLConnection> umlConnections;

    /**
     * Constructor initializes the panel with squares and connections to be rendered.
     *
     * @param squares        List of squares to draw.
     * @param connections    List of decoration connections (lines).
     * @param umlConnections List of UML-specific connections (relationships).
     */
    public DrawPanel(List<Square> squares, List<Connection> connections, List<UMLConnection> umlConnections) {
        this.squares = squares;
        this.connections = connections;
        this.umlConnections = umlConnections;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw all squares
        for (Square square : squares) {
            square.draw(g);
        }

        Graphics2D g2 = (Graphics2D) g;

        // Draw decoration connections
        g2.setColor(Color.BLACK);
        for (Connection connection : connections) {
            Point start = connection.getStartPoint();
            Point end = connection.getEndPoint();

            if (start != null && end != null) {
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(start.x, start.y, end.x, end.y);
            }
        }

        // Draw UML connections
        for (UMLConnection connection : umlConnections) {
            Point startPoint = connection.getStartPoint();
            Point endPoint = connection.getEndPoint();
            String connectionType = connection.getConnectionType();

            if (startPoint != null && endPoint != null) {
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

                // Add arrowheads or diamonds based on connection type
                switch (connectionType) {
                    case "Inheritance":
                        drawArrow(g2, startPoint.x, startPoint.y, endPoint.x, endPoint.y, true);
                        break;
                    case "Realization":
                        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{5}, 0));
                        drawArrow(g2, startPoint.x, startPoint.y, endPoint.x, endPoint.y, true);
                        break;
                    case "Aggregation":
                        drawDiamond(g2, startPoint.x, startPoint.y, endPoint.x, endPoint.y, false);
                        break;
                    case "Composition":
                        drawDiamond(g2, startPoint.x, startPoint.y, endPoint.x, endPoint.y, true);
                        break;
                    case "Association":
                        drawArrow(g2, startPoint.x, startPoint.y, endPoint.x, endPoint.y, false);
                        break;
                }
            }
        }
    }

    // Helper method to draw a diamond (Aggregation/Composition)
    private void drawDiamond(Graphics2D g2, int x1, int y1, int x2, int y2, boolean filled) {
        int size = 15; // Diamond size
        double angle = Math.atan2(y2 - y1, x2 - x1);

        // Calculate diamond vertices
        int[] xPoints = {
                x2,
                (int) (x2 - size * Math.cos(angle - Math.PI / 4)),
                (int) (x2 - 2 * size * Math.cos(angle)),
                (int) (x2 - size * Math.cos(angle + Math.PI / 4))
        };

        int[] yPoints = {
                y2,
                (int) (y2 - size * Math.sin(angle - Math.PI / 4)),
                (int) (y2 - 2 * size * Math.sin(angle)),
                (int) (y2 - size * Math.sin(angle + Math.PI / 4))
        };

        if (filled) {
            g2.fillPolygon(xPoints, yPoints, 4);
        } else {
            g2.drawPolygon(xPoints, yPoints, 4);
        }
    }

    // Helper method to draw an arrow (Inheritance/Association/Realization)
    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2, boolean filled) {
        int size = 15; // Arrowhead size
        double angle = Math.atan2(y2 - y1, x2 - x1);

        // Calculate arrowhead vertices
        int[] xPoints = {
                x2,
                (int) (x2 - size * Math.cos(angle - Math.PI / 6)),
                (int) (x2 - size * Math.cos(angle + Math.PI / 6))
        };

        int[] yPoints = {
                y2,
                (int) (y2 - size * Math.sin(angle - Math.PI / 6)),
                (int) (y2 - size * Math.sin(angle + Math.PI / 6))
        };

        if (filled) {
            g2.fillPolygon(xPoints, yPoints, 3);
        } else {
            g2.drawPolygon(xPoints, yPoints, 3);
        }
    }
}
