import java.awt.*;

public class FactoryDecorator extends SquareDecorator {
    public FactoryDecorator(Square decoratedSquare) {
        super(decoratedSquare);
    }

    @Override
    protected void addDecoration(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.drawString("Factory", decoratedSquare.getX() - SIZE / 2, decoratedSquare.getY() - SIZE / 2 - 100);
    }
}