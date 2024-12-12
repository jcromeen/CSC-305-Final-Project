import java.awt.*;

public class Connection {
    private final Point startPoint;
    private final Point endPoint;

    public Connection(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }
}