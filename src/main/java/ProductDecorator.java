import java.awt.*;

public class ProductDecorator extends SquareDecorator {
    public ProductDecorator(Square decoratedSquare) {
        super(decoratedSquare);
    }

    @Override
    protected void addDecoration(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Product", decoratedSquare.getX() - SIZE / 2, decoratedSquare.getY() - SIZE / 2 - 115);
    }
}