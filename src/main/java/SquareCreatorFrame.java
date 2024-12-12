import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SquareCreatorFrame extends JFrame {
    private static SquareCreatorFrame instance;
    private final List<Square> squares;
    private final List<Connection> connections;
    private final List<UMLConnection> umlConnections;
    private File currentSaveFile = null;
    private DirectoryManager directoryManager;
    private CodeGenerator codeGenerator;
    private ConnectionManager connectionManager;
    private SquareManager squareManager;
    private Square selectedSquare = null;
    private String selectedConnectionType = null;
    private Square selectedStartSquare = null;
    private int offsetX, offsetY;
    private String mode = "default";
    private DefaultMutableTreeNode srcNode;
    private JTextArea codeArea;

    private JPanel drawPanel;
    private JPopupMenu patternMenu;
    private JMenuBar menuBar;
    private JPanel codePanel;
    private JTabbedPane tabbedPane;


    // Checkboxes for decoration patterns
    private JCheckBoxMenuItem observerItem;
    private JCheckBoxMenuItem observableItem;
    private JCheckBoxMenuItem singletonItem;
    private JCheckBoxMenuItem decorationItem;
    private JCheckBoxMenuItem decorateableItem;
    private JCheckBoxMenuItem chainMemberItem;
    private JCheckBoxMenuItem strategyItem;
    private JCheckBoxMenuItem factoryItem;
    private JCheckBoxMenuItem productItem;

    /**
     * Private constructor for the singleton pattern. Initializes the frame and its components.
     */
    private SquareCreatorFrame() {
        setTitle("Square Creator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize data structures
        squares = new ArrayList<>();
        connections = new ArrayList<>();
        umlConnections = new ArrayList<>();

        directoryManager = new DirectoryManager();
        codeGenerator = new CodeGenerator();
        connectionManager = new ConnectionManager(connections, umlConnections, this);
        squareManager = new SquareManager(squares, directoryManager, this);

        // Setup Draw Panel
        drawPanel = new DrawPanel(squares, connections, umlConnections);
        drawPanel.setBackground(Color.WHITE);
        drawPanel.setPreferredSize(new Dimension(800, 600));
        SquareMouseListener listener = new SquareMouseListener(this, squares, connectionManager);
        drawPanel.addMouseListener(listener);
        drawPanel.addMouseMotionListener(listener);

        // Setup Code tab
        codePanel = new JPanel();
        codePanel.setLayout(new BorderLayout());

        // Code Left panel: Directory structure
        JTree directoryTree = directoryManager.getDirectoryTree();
        JScrollPane directoryScrollPane = new JScrollPane(directoryTree);
        directoryScrollPane.setPreferredSize(new Dimension(200, 600));

        // Code Right panel: Code display
        codeArea = new JTextArea();
        codeArea.setEditable(false);
        codeArea.setText("Select a file to view its code...");
        JScrollPane codeScrollPane = new JScrollPane(codeArea);

        // Split the Code tab into left and right panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, directoryScrollPane, codeScrollPane);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.3);

        codePanel.add(splitPane, BorderLayout.CENTER);

        // Add a listener to handle file selection
        directoryTree.addTreeSelectionListener(e -> {
            String selectedFile = directoryManager.getSelectedFileName();
            if (selectedFile != null) {
                String code = codeGenerator.generateCode(selectedFile.replace(".java", ""), squares, umlConnections);
                codeArea.setText(code);
            }
        });

        // Create the tabbed pane and add tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Draw Area", drawPanel);
        tabbedPane.addTab("Code", codePanel);

        // Set layout and add components
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);

        // Setup menus
        setupPatternMenu(); // If specific to drawPanel, it is set before
        setupMenuBar(); // Ensure menuBar is configured last
    }


    public static SquareCreatorFrame getInstance() {
        if (instance == null) {
            instance = new SquareCreatorFrame();
        }
        return instance;
    }

    public void setSelectedConnectionType(String connectionType) {
        this.selectedConnectionType = connectionType;
    }

    public String getSelectedConnectionType() {
        return selectedConnectionType;
    }

    public void setMode(String mode) {
        this.mode = mode;
        System.out.println("Mode set to: " + mode);
    }

    public String getMode() {
        return mode;
    }

    public JPopupMenu getPatternMenu() {
        return patternMenu;
    }

    public JPanel getDrawPanel() {
        return drawPanel;
    }

    public void renameBox(Square box, String newName) {
        squareManager.renameBox(box, newName);
    }

    public void deleteBox(Square box) {
        squareManager.deleteBox(box);
    }

    public void addConnection(Point startPoint, Point endPoint) {
        connections.add(new Connection(startPoint, endPoint));
        repaint();
    }

    // Popup for About dropdown
    private void showAboutPopup() {
        JOptionPane.showMessageDialog(
                this,
                "Final Project\nAuthor: Jimmy Cromeenes\nYear: " + java.time.Year.now(),
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // File management functions (New, Open, Save, Save As)

    private void handleNewFile() {
        if (!squares.isEmpty() || !connections.isEmpty() || !umlConnections.isEmpty()) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "You have unsaved changes. Do you want to overwrite the current file?",
                    "Confirm New File",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Clear all existing data
        squares.clear();
        connections.clear();
        umlConnections.clear();
        codeArea.setText("Select a file to view its code...");
        directoryManager.clearDirectory();
        drawPanel.repaint();
        System.out.println("New file created.");
    }

    private void handleOpenFile() {
        System.out.println("Open file functionality to be implemented.");
    }

    private void saveToZip(File zipFile) throws IOException {
        File tempSvgFile = File.createTempFile("drawArea", ".svg");
        File tempSrcFolder = new File(tempSvgFile.getParentFile(), "src");
        if (!tempSrcFolder.exists()) tempSrcFolder.mkdir();

        // Save the SVG
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempSvgFile))) {
            writer.write(generateSvg());
        }

        // Save Java files to the src folder
        for (Square square : squares) {
            File javaFile = new File(tempSrcFolder, square.getName() + ".java");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile))) {
                writer.write(codeGenerator.generateCode(square.getName(), squares, umlConnections));
            }
        }

        // Create the zip file
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            addFileToZip(tempSvgFile, "drawArea.svg", zos);
            for (File javaFile : tempSrcFolder.listFiles()) {
                addFileToZip(javaFile, "src/" + javaFile.getName(), zos);
            }
        }

        // Clean up temporary files
        tempSvgFile.delete();
        for (File file : tempSrcFolder.listFiles()) {
            file.delete();
        }
        tempSrcFolder.delete();
    }

    private void addFileToZip(File file, String entryName, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length); // Specify offset (0)
            }
            zos.closeEntry();
        }
    }

    private String generateSvg() {
        StringBuilder svg = new StringBuilder();
        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"800\" height=\"600\">\n");

        // Add squares
        svg.append("<style>.box { fill: blue; }</style>\n");
        for (Square square : squares) {
            int rectX = square.getX() - Square.SIZE / 2;
            int rectY = square.getY() - Square.SIZE / 2;

            svg.append(String.format(
                    "    <rect class=\"box\" x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" />\n",
                    rectX, rectY, Square.SIZE, Square.SIZE
            ));

            // Draw name
            svg.append(String.format(
                    "    <text x=\"%d\" y=\"%d\" font-size=\"12\" fill=\"white\" text-anchor=\"middle\">%s</text>\n",
                    square.getX(), rectY + Square.SIZE / 2 + 5, square.getName()
            ));

            // Draw decorations
            for (String decoration : square.getDecorations()) {
                Point center = square.getDecorationCenter(decoration);
                if (center != null) {
                    svg.append(String.format(
                            "    <text x=\"%d\" y=\"%d\" font-size=\"10\" fill=\"black\">%s</text>\n",
                            center.x, center.y, decoration
                    ));
                }
            }
        }

        // Add connections
        svg.append("<style>.connection { stroke: black; stroke-width: 2; }</style>\n");
        for (Connection connection : connections) {
            Point start = connection.getStartPoint();
            Point end = connection.getEndPoint();
            if (start != null && end != null) {
                svg.append(String.format(
                        "    <line class=\"connection\" x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" />\n",
                        start.x, start.y, end.x, end.y
                ));
            }
        }

        // Add UML connections
        svg.append("<style>.umlConnection { stroke: black; stroke-width: 2; }</style>\n");
        for (UMLConnection umlConnection : umlConnections) {
            Point start = umlConnection.getStartPoint();
            Point end = umlConnection.getEndPoint();
            if (start != null && end != null) {
                svg.append(String.format(
                        "    <line class=\"umlConnection\" x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" />\n",
                        start.x, start.y, end.x, end.y
                ));
            }
        }

        svg.append("</svg>\n");
        return svg.toString();
    }


    private void handleSaveAs() {
        // Create a file chooser dialog for saving files
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(".zip")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".zip");
            }

            try { // Attempt to save the current project data
                saveToZip(selectedFile);

                JOptionPane.showMessageDialog(
                        this,
                        "File saved successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to save the file: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("Save As operation canceled.");
        }
    }

    private void handleSave() {
        if (currentSaveFile == null) {
            // If no save file is associated, Save As
            handleSaveAs();
        } else {
            try {
                saveToZip(currentSaveFile);

                JOptionPane.showMessageDialog(
                        this,
                        "File saved successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to save the file: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // Sets up the main menu bar with file and help menus
    private void setupMenuBar() {
        menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New...");
        JMenuItem openItem = new JMenuItem("Open...");
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(saveItem);

        // Add action listeners for File menu items
        newItem.addActionListener(e -> handleNewFile());
        openItem.addActionListener(e -> handleOpenFile());
        saveAsItem.addActionListener(e -> handleSaveAs());
        saveItem.addActionListener(e -> handleSave());

        menuBar.add(fileMenu);

        // Box Connector menu
        JMenu boxConnectorMenu = MenuBuilder.createMenu(
                "Box Connector",
                MenuBuilder.createMenuItem("Aggregation", e -> selectConnectionType("Aggregation")),
                MenuBuilder.createMenuItem("Composition", e -> selectConnectionType("Composition")),
                MenuBuilder.createMenuItem("Association", e -> selectConnectionType("Association")),
                MenuBuilder.createMenuItem("Inheritance", e -> selectConnectionType("Inheritance")),
                MenuBuilder.createMenuItem("Realization", e -> selectConnectionType("Realization"))
        );
        menuBar.add(boxConnectorMenu);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);
        aboutItem.addActionListener(e -> showAboutPopup());

        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        setJMenuBar(menuBar);
    }

    // When choosing a UML connection type
    public void selectConnectionType(String connectionType) {
        selectedConnectionType = connectionType;
        setMode("umlConnection");
        selectedStartSquare = null;
        System.out.println("Selected connection type: " + connectionType);
    }

    public void startConnection(Square square) {
        connectionManager.startConnection(square);
    }

    public void cancelConnection() {
        connectionManager.cancelConnection();
    }

    private void toggleDecoration(String decoration, JCheckBoxMenuItem item) {
        if (selectedSquare != null) {
            switch (decoration) {
                case "Observer":
                    selectedSquare.setObserverEnabled(item.isSelected());
                    break;
                case "Observable":
                    selectedSquare.setObservableEnabled(item.isSelected());
                    break;
                case "Singleton":
                    selectedSquare.setSingletonEnabled(item.isSelected());
                    break;
                case "Decoration":
                    selectedSquare.setDecorationEnabled(item.isSelected());
                    break;
                case "Decorateable":
                    selectedSquare.setDecorateableEnabled(item.isSelected());
                    break;
                case "Chain Member":
                    selectedSquare.setChainMemberEnabled(item.isSelected());
                    break;
                case "Strategy":
                    selectedSquare.setStrategyEnabled(item.isSelected());
                    break;
                case "Factory":
                    selectedSquare.setFactoryEnabled(item.isSelected());
                    break;
                case "Product":
                    selectedSquare.setProductEnabled(item.isSelected());
                    break;
            }
            drawPanel.repaint();
        }
    }

    // Sets up the context/checkbox menu for decoration patterns
    private void setupPatternMenu() {
        patternMenu = new JPopupMenu();

        observerItem = MenuBuilder.createCheckBoxMenuItem("Observer", false, e -> toggleDecoration("Observer", observerItem));
        observableItem = MenuBuilder.createCheckBoxMenuItem("Observable", false, e -> toggleDecoration("Observable", observableItem));
        singletonItem = MenuBuilder.createCheckBoxMenuItem("Singleton", false, e -> toggleDecoration("Singleton", singletonItem));
        decorationItem = MenuBuilder.createCheckBoxMenuItem("Decoration", false, e -> toggleDecoration("Decoration", decorationItem));
        decorateableItem = MenuBuilder.createCheckBoxMenuItem("Decorateable", false, e -> toggleDecoration("Decorateable", decorateableItem));
        chainMemberItem = MenuBuilder.createCheckBoxMenuItem("Chain Member", false, e -> toggleDecoration("Chain Member", chainMemberItem));
        strategyItem = MenuBuilder.createCheckBoxMenuItem("Strategy", false, e -> toggleDecoration("Strategy", strategyItem));
        factoryItem = MenuBuilder.createCheckBoxMenuItem("Factory", false, e -> toggleDecoration("Factory", factoryItem));
        productItem = MenuBuilder.createCheckBoxMenuItem("Product", false, e -> toggleDecoration("Product", productItem));

        patternMenu.add(observerItem);
        patternMenu.add(observableItem);
        patternMenu.add(singletonItem);
        patternMenu.add(decorationItem);
        patternMenu.add(decorateableItem);
        patternMenu.add(chainMemberItem);
        patternMenu.add(strategyItem);
        patternMenu.add(factoryItem);
        patternMenu.add(productItem);

        patternMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if (selectedSquare != null) {
                    observerItem.setSelected(selectedSquare.isObserverEnabled());
                    observableItem.setSelected(selectedSquare.isObservableEnabled());
                    singletonItem.setSelected(selectedSquare.isSingletonEnabled());
                    decorationItem.setSelected(selectedSquare.isDecorationEnabled());
                    decorateableItem.setSelected(selectedSquare.isDecorateableEnabled());
                    chainMemberItem.setSelected(selectedSquare.isChainMemberEnabled());
                    strategyItem.setSelected(selectedSquare.isStrategyEnabled());
                    factoryItem.setSelected(selectedSquare.isFactoryEnabled());
                    productItem.setSelected(selectedSquare.isProductEnabled());
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                drawPanel.repaint();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    public void createSquare(int x, int y) {
        squareManager.createSquare(x, y);
    }

    public void showEditPopup(Square square, int x, int y) {
        squareManager.showEditPopup(square, x, y);
    }

    public void setSelectedSquare(Square square) {
        this.selectedSquare = square;
    }

    public Square getSelectedSquare() {
        return selectedSquare;
    }

    public void setOffset(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void showNameEditDialog(Square square) {
        squareManager.showNameEditDialog(square);
    }
}