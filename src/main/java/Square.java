import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Square {
    protected static final int SIZE = 50;
    private int x;
    private int y;
    private String name;

    // Flags to enable/disable specific decoration patterns.
    private boolean observerEnabled;
    private boolean observableEnabled;
    private boolean singletonEnabled;
    private boolean decorationEnabled;
    private boolean decorateableEnabled;
    private boolean chainMemberEnabled;
    private boolean strategyEnabled;
    private boolean factoryEnabled;
    private boolean productEnabled;

    // Maps for managing decoration hitboxes and their centers.
    private final Map<String, Rectangle> decorationHitboxes = new HashMap<>();
    private final Map<String, Point> decorationCenters = new HashMap<>();

    /**
     * Constructs a square with the specified position and name.
     *
     * @param x    X-coordinate of the square center.
     * @param y    Y-coordinate of the square center.
     * @param name Name to display on the square.
     */
    public Square(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void draw(Graphics g) {
        // Draw the square
        g.setColor(Color.BLUE);
        g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        g.setColor(Color.WHITE);

        // Draw the square's name
        Font font = g.getFont();
        int fontSize = font.getSize();
        FontMetrics metrics;
        do {
            g.setFont(new Font(font.getName(), font.getStyle(), fontSize));
            metrics = g.getFontMetrics();
            fontSize--;
        } while ((metrics.stringWidth(name) > SIZE || metrics.getHeight() > SIZE) && fontSize > 0);

        g.drawString(name, x - metrics.stringWidth(name) / 2, y + metrics.getHeight() / 4);

        // Draw decorations above the square
        int decorationYOffset = -10;
        if (observerEnabled) {
            drawDecoration(g, "Observer", Color.MAGENTA, decorationYOffset);
            decorationYOffset -= 20; // Increase gap between decorations for better hitboxes
        }
        if (observableEnabled) {
            drawDecoration(g, "Observable", Color.GREEN, decorationYOffset);
            decorationYOffset -= 20;
        }
        if (singletonEnabled) {
            drawDecoration(g, "Singleton", Color.BLUE, decorationYOffset);
            decorationYOffset -= 20;
        }
        if (decorationEnabled) {
            drawDecoration(g, "Decoration", Color.ORANGE, decorationYOffset);
            decorationYOffset -= 20;
        }
        if (decorateableEnabled) {
            drawDecoration(g, "Decorateable", Color.PINK, decorationYOffset);
            decorationYOffset -= 20;
        }
        if (chainMemberEnabled) {
            drawDecoration(g, "Chain Member", Color.CYAN, decorationYOffset);
            decorationYOffset -= 20;
        }
        if (strategyEnabled) {
            drawDecoration(g, "Strategy", Color.RED, decorationYOffset);
            decorationYOffset -= 20;
        }
        if (factoryEnabled) {
            drawDecoration(g, "Factory", Color.DARK_GRAY, decorationYOffset);
            decorationYOffset -= 20;
        }
        if (productEnabled) {
            drawDecoration(g, "Product", Color.LIGHT_GRAY, decorationYOffset);
        }
        // Draw decoration hitboxes in red
        g.setColor(Color.RED);
        for (Rectangle hitbox : decorationHitboxes.values()) {
            ((Graphics2D) g).draw(hitbox);
        }
    }

    private void drawDecoration(Graphics g, String label, Color color, int yOffset) {
        g.setColor(color);
        int textX = x - SIZE / 2;
        int textY = y - SIZE / 2 + yOffset;

        g.drawString(label, textX, textY);

        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth(label) + 10;
        int height = metrics.getHeight();

        // Create and store the hitbox for this decoration
        Rectangle hitbox = new Rectangle(textX - 5, textY - height, width, height);
        decorationHitboxes.put(label, hitbox);

        // Calculate and store decoration center
        Point center = new Point(textX + width / 2, textY - height / 2);
        decorationCenters.put(label, center);

        System.out.println("Decoration '" + label + "' center: " + center + ", hitbox: " + hitbox);
    }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x - SIZE / 2 && mouseX <= x + SIZE / 2 && mouseY >= y - SIZE / 2 && mouseY <= y + SIZE / 2;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Retrieves the decoration clicked based on mouse coordinates
    public String getClickedDecoration(int mouseX, int mouseY) {
        for (Map.Entry<String, Rectangle> entry : decorationHitboxes.entrySet()) {
            if (entry.getValue().contains(mouseX, mouseY)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Point getDecorationCenter(String decorationLabel) {
        Point center = decorationCenters.get(decorationLabel);
        System.out.println("Decoration center for " + decorationLabel + ": " + center);
        return center;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isObserverEnabled() {
        return observerEnabled;
    }

    public void setObserverEnabled(boolean observerEnabled) {
        this.observerEnabled = observerEnabled;
    }

    public boolean isObservableEnabled() {
        return observableEnabled;
    }

    public void setObservableEnabled(boolean observableEnabled) {
        this.observableEnabled = observableEnabled;
    }

    public boolean isSingletonEnabled() {
        return singletonEnabled;
    }

    public void setSingletonEnabled(boolean singletonEnabled) {
        this.singletonEnabled = singletonEnabled;
    }

    public boolean isDecorationEnabled() {
        return decorationEnabled;
    }

    public void setDecorationEnabled(boolean decorationEnabled) {
        this.decorationEnabled = decorationEnabled;
    }

    public boolean isDecorateableEnabled() {
        return decorateableEnabled;
    }

    public void setDecorateableEnabled(boolean decorateableEnabled) {
        this.decorateableEnabled = decorateableEnabled;
    }

    public boolean isChainMemberEnabled() {
        return chainMemberEnabled;
    }

    public void setChainMemberEnabled(boolean chainMemberEnabled) {
        this.chainMemberEnabled = chainMemberEnabled;
    }

    public boolean isStrategyEnabled() {
        return strategyEnabled;
    }

    public void setStrategyEnabled(boolean strategyEnabled) {
        this.strategyEnabled = strategyEnabled;
    }

    public boolean isFactoryEnabled() {
        return factoryEnabled;
    }

    public void setFactoryEnabled(boolean factoryEnabled) {
        this.factoryEnabled = factoryEnabled;
    }

    public boolean isProductEnabled() {
        return productEnabled;
    }

    public void setProductEnabled(boolean productEnabled) {
        this.productEnabled = productEnabled;
    }

    public List<String> getDecorations() {
        // Return a list of active decorations for this square
        List<String> activeDecorations = new ArrayList<>();
        if (isObserverEnabled()) activeDecorations.add("Observer");
        if (isObservableEnabled()) activeDecorations.add("Observable");
        if (isSingletonEnabled()) activeDecorations.add("Singleton");
        if (isDecorationEnabled()) activeDecorations.add("Decorator");
        if (isDecorateableEnabled()) activeDecorations.add("Decorateable");
        if (isChainMemberEnabled()) activeDecorations.add("Chain Member");
        if (isStrategyEnabled()) activeDecorations.add("Strategy");
        if (isFactoryEnabled()) activeDecorations.add("Factory");
        if (isProductEnabled()) activeDecorations.add("Product");
        return activeDecorations;
    }

    public List<UMLConnection> getConnections(List<UMLConnection> allConnections) {
        List<UMLConnection> relevantConnections = new ArrayList<>();
        for (UMLConnection connection : allConnections) {
            if (connection.getStartSquare() == this) {
                relevantConnections.add(connection);
            }
        }
        return relevantConnections;
    }
}
