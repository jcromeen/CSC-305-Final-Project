import java.awt.*;

public class StrategyDecorator extends SquareDecorator {
    public StrategyDecorator(Square decoratedSquare) {
        super(decoratedSquare);
    }

    @Override
    protected void addDecoration(Graphics g) {
        g.setColor(Color.RED);
        g.drawString("Strategy", decoratedSquare.getX() - SIZE / 2, decoratedSquare.getY() - SIZE / 2 - 85);
    }
}
