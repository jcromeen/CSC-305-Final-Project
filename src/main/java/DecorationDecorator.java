import java.awt.*;

public class DecorationDecorator extends SquareDecorator {
    public DecorationDecorator(Square decoratedSquare) {
        super(decoratedSquare);
    }

    @Override
    protected void addDecoration(Graphics g) {
        g.setColor(Color.ORANGE);
        g.drawString("Decoration", decoratedSquare.getX() - SIZE / 2, decoratedSquare.getY() - SIZE / 2 - 130);
    }
}