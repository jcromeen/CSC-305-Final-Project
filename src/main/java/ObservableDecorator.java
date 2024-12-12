import java.awt.*;

public class ObservableDecorator extends SquareDecorator {
    public ObservableDecorator(Square decoratedSquare) {
        super(decoratedSquare);
    }

    @Override
    protected void addDecoration(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawString("Observable", decoratedSquare.getX() - SIZE / 2, decoratedSquare.getY() - SIZE / 2 - 25);
    }
}