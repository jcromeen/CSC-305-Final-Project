import javax.swing.*;
import java.awt.*;
import java.util.List;

class ConnectionManager {
    private final List<Connection> connections;
    private final List<UMLConnection> umlConnections;
    private final SquareCreatorFrame frame;
    private Square selectedStartSquare = null;
    private String selectedConnectionType = null;

    /**
     * Constructor to initialize the ConnectionManager with required dependencies.
     *
     * @param connections    List of line connections between decorations to manage.
     * @param umlConnections List of UML connections.
     * @param frame          Reference to the parent frame for interaction and updates.
     */
    public ConnectionManager(List<Connection> connections, List<UMLConnection> umlConnections, SquareCreatorFrame frame) {
        this.connections = connections;
        this.umlConnections = umlConnections;
        this.frame = frame;
    }

    // Starts or completes a connection process depending on the current state
    public void startConnection(Square square) {
        String connectionType = frame.getSelectedConnectionType();
        System.out.println("Current connection type: " + connectionType);

        if (connectionType == null) {
            JOptionPane.showMessageDialog(frame, "Please select a connection type first.", "Error", JOptionPane.ERROR_MESSAGE);
            cancelConnection(); // Reset mode and variables
            return;
        }

        if (selectedStartSquare == null) { // Must be the first square clicked
            selectedStartSquare = square;
            System.out.println("Start square selected: " + square.getName());
        } else {
            // Complete the connection by adding a UMLConnection
            System.out.println("End square selected: " + square.getName());
            umlConnections.add(new UMLConnection(selectedStartSquare, square, connectionType));
            System.out.println("UMLConnection added: " + connectionType + " from " + selectedStartSquare.getName() + " to " + square.getName());
            cancelConnection(); // Reset mode and variables
            frame.repaint();
        }
    }

    public void cancelConnection() {
        selectedStartSquare = null;
        frame.setMode("default");
        System.out.println("Connection canceled, mode reset to default.");
    }

    public void addConnection(Point startPoint, Point endPoint) {
        connections.add(new Connection(startPoint, endPoint));
        frame.repaint();
    }
}
