import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SquareMouseListener implements MouseListener, MouseMotionListener {
    private final SquareCreatorFrame frame;
    private final List<Square> squares;
    private Square startSquare = null;
    private String startDecorationLabel = null;
    private final ConnectionManager connectionManager;

    /**
     * Constructs a mouse listener for handling square interactions.
     *
     * @param frame            Main application frame.
     * @param squares          List of squares to interact with.
     * @param connectionManager Manages connections between squares.
     */
    public SquareMouseListener(SquareCreatorFrame frame, List<Square> squares, ConnectionManager connectionManager) {
        this.frame = frame;
        this.squares = squares;
        this.connectionManager = connectionManager;
    }

    /**
     * Handles mouse click events.
     * - Creates a new square if clicked on an empty space.
     * - Opens edit menus or renaming dialogs for existing squares.
     * - Starts UML connections if in connection mode.
     *
     * @param e Mouse event.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (frame.getMode().equals("umlConnection")) {
            // Handle UML connection creation
            boolean validClick = false;
            for (Square square : squares) {
                if (square.contains(e.getX(), e.getY())) {
                    System.out.println("Square detected at (" + e.getX() + ", " + e.getY() + "): " + square.getName());
                    connectionManager.startConnection(square);
                    validClick = true;
                    break;
                }
            }
            if (!validClick) { // Reset connection state on invalid click
                System.out.println("No square detected at (" + e.getX() + ", " + e.getY() + ").");
                connectionManager.cancelConnection();
            }
        } else {
            // Handle default click behavior (create/edit square)
            for (Square square : squares) {
                if (square.contains(e.getX(), e.getY())) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        frame.showEditPopup(square, e.getX(), e.getY());
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        frame.showNameEditDialog(square);
                    }
                    return;
                }
            }
            frame.createSquare(e.getX(), e.getY());
        }
    }

    /**
     * Handles mouse press events.
     * Detects if a drag starts from a decoration or a square.
     *
     * @param e Mouse event.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        for (Square square : squares) {
            String decorationLabel = square.getClickedDecoration(e.getX(), e.getY());
            if (decorationLabel != null && SwingUtilities.isLeftMouseButton(e)) {
                // Start dragging from a decoration
                startSquare = square;
                startDecorationLabel = decorationLabel;
                System.out.println("Starting to drag from a decoration on square: " + square.getName() + ", decoration: " + decorationLabel); // Debugging output
                return;
            } else if (square.contains(e.getX(), e.getY())) {
                // Regular square dragging
                frame.setSelectedSquare(square);
                frame.setOffset(e.getX() - square.getX(), e.getY() - square.getY());
                return;
            }
        }
    }

    /**
     * Handles mouse release events.
     * Finalizes decoration connections or resets connection state.
     *
     * @param e Mouse event.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released at: " + e.getX() + ", " + e.getY());

        // Handle decoration connection logic
        if (startSquare != null && startDecorationLabel != null) {
            for (Square square : squares) {
                String endDecorationLabel = square.getClickedDecoration(e.getX(), e.getY());
                System.out.println("Checking square: " + square.getName() + ", endDecorationLabel: " + endDecorationLabel);

                // Ensure the target square and decoration are valid
                if (square != startSquare && endDecorationLabel != null) {
                    System.out.println("Valid target found: " + square.getName() + ", decoration: " + endDecorationLabel);

                    Point startPoint = startSquare.getDecorationCenter(startDecorationLabel);
                    Point endPoint = square.getDecorationCenter(endDecorationLabel);
                    System.out.println("Start point: " + startPoint + ", End point: " + endPoint);

                    // Validate points and add connection
                    if (startPoint != null && endPoint != null) {
                        frame.addConnection(startPoint, endPoint);
                        System.out.println("Connection added from " + startPoint + " to " + endPoint);
                    } else {
                        System.err.println("Failed to retrieve valid decoration centers.");
                    }

                    // Reset connection state
                    startSquare = null;
                    startDecorationLabel = null;
                    return;
                }
            }
        }

        // Handle invalid or incomplete connections
        System.out.println("Release detected but no valid target for connection.");
        startSquare = null;
        startDecorationLabel = null;
        frame.setSelectedSquare(null);
    }

    /**
     * Handles mouse drag events.
     * Moves the selected square with the mouse.
     *
     * @param e Mouse event.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (frame.getMode().equals("umlConnection")) {
            // Do nothing while in UML connection mode
            return;
        }

        // Default dragging behavior
        Square selectedSquare = frame.getSelectedSquare();
        if (selectedSquare != null) {
            selectedSquare.setPosition(e.getX() - frame.getOffsetX(), e.getY() - frame.getOffsetY());
            frame.repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not used
    }
}
