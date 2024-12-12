import java.awt.*;

public class SingletonDecorator extends SquareDecorator {
    public SingletonDecorator(Square decoratedSquare) {
        super(decoratedSquare);
    }

    @Override
    protected void addDecoration(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawString("Singleton", decoratedSquare.getX() - SIZE / 2, decoratedSquare.getY() - SIZE / 2 - 40);
    }
}
