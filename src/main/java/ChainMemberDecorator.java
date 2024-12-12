import java.awt.*;

public class ChainMemberDecorator extends SquareDecorator {
    public ChainMemberDecorator(Square decoratedSquare) {
        super(decoratedSquare);
    }

    @Override
    protected void addDecoration(Graphics g) {
        g.setColor(Color.CYAN);
        g.drawString("Chain Member", decoratedSquare.getX() - SIZE / 2, decoratedSquare.getY() - SIZE / 2 - 70);
    }
}
