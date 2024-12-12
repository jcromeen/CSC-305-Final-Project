import java.awt.*;

public class DecorateableDecorator extends SquareDecorator {
    public DecorateableDecorator(Square decoratedSquare) {
        super(decoratedSquare);
    }

    @Override
    protected void addDecoration(Graphics g) {
        g.setColor(Color.PINK);
        g.drawString("Decorateable", decoratedSquare.getX() - SIZE / 2, decoratedSquare.getY() - SIZE / 2 - 55);
    }
}