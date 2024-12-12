import java.awt.*;

public class UMLConnection {
    private final Square startSquare;
    private final Square endSquare;
    private final String connectionType;

    public UMLConnection(Square startSquare, Square endSquare, String connectionType) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.connectionType = connectionType;
    }

    public Square getStartSquare() {
        return startSquare;
    }

    public Square getEndSquare() {
        return endSquare;
    }

    public Point getStartPoint() {
        return new Point(startSquare.getX(), startSquare.getY());
    }

    public Point getEndPoint() {
        return new Point(endSquare.getX(), endSquare.getY());
    }

    public String getConnectionType() {
        return connectionType;
    }
}
