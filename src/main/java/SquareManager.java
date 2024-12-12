import javax.swing.*;
import java.awt.*;
import java.util.List;

class SquareManager {
    private final List<Square> squares;
    private final DirectoryManager directoryManager;
    private final SquareCreatorFrame frame;

    /**
     * Constructs a SquareManager with the given list of squares, directory manager, and frame.
     *
     * @param squares          List of squares to manage.
     * @param directoryManager Directory manager for handling file structure.
     * @param frame            Main application frame.
     */
    public SquareManager(List<Square> squares, DirectoryManager directoryManager, SquareCreatorFrame frame) {
        this.squares = squares;
        this.directoryManager = directoryManager;
        this.frame = frame;
    }

    public void createSquare(int x, int y) {
        String name = "Name" + String.format("%02d", squares.size() + 1);
        squares.add(new Square(x, y, name));
        directoryManager.addBoxToDirectory(name);
        frame.repaint();
    }

    // Popup for editing square name
    public void showEditPopup(Square square, int x, int y) {
        frame.setSelectedSquare(square);
        JPopupMenu patternMenu = frame.getPatternMenu();
        patternMenu.show(frame.getDrawPanel(), x, y);
    }

    public void showNameEditDialog(Square square) {
        JDialog dialog = new JDialog(frame, "Edit Square Name", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);

        JTextField nameField = new JTextField(square.getName());
        dialog.add(nameField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            String newName = nameField.getText().trim();
            if (!newName.isEmpty() && !newName.equals(square.getName())) {
                renameBox(square, newName);
            }
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    public void renameBox(Square box, String newName) {
        String oldName = box.getName();
        box.setName(newName);
        directoryManager.renameBoxInDirectory(oldName, newName);
        frame.repaint();
    }

    public void deleteBox(Square box) {
        squares.remove(box);
        directoryManager.removeBoxFromDirectory(box.getName());
        frame.repaint();
    }
}
