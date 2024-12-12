import java.awt.*;

public abstract class SquareDecorator extends Square {
    protected Square decoratedSquare;

    /**
     * Constructs a decorator for a given Square.
     *
     * @param decoratedSquare The Square to be decorated.
     */
    public SquareDecorator(Square decoratedSquare) {
        super(decoratedSquare.getX(), decoratedSquare.getY(), decoratedSquare.getName());
        this.decoratedSquare = decoratedSquare;
    }

    @Override
    public void draw(Graphics g) {
        decoratedSquare.draw(g);
        addDecoration(g);
    }

    protected abstract void addDecoration(Graphics g);
}