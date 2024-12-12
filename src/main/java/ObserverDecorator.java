import java.awt.*;

public class ObserverDecorator extends SquareDecorator {
    public ObserverDecorator(Square decoratedSquare) {
        super(decoratedSquare);
    }

    @Override
    protected void addDecoration(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.drawString("Observer", decoratedSquare.getX() - SIZE / 2, decoratedSquare.getY() - SIZE / 2 - 10);
    }
}