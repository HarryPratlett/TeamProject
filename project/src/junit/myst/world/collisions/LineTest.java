package junit.myst.world.collisions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.myst.world.collisions.Line;
import org.joml.Vector2f;
import org.junit.Test;

public class LineTest {

    @Test
    public void testLineIntersectionCo(){
        Line line1 = new Line(new Vector2f(-1,-1), new Vector2f(1,1));
        Line line2 = new Line(new Vector2f(3,0), new Vector2f(0,1));

        Float lambda = line1.intersectionCo(line2);
        assertEquals(3, lambda,0.001);

        line1 = new Line(new Vector2f(0,0), new Vector2f(1,1));
        line2 = new Line(new Vector2f(1,1), new Vector2f(1,1));

        lambda = line1.intersectionCo(line2);
        assertNull(lambda);

        line1 = new Line(new Vector2f(-1,-1), new Vector2f(-1,-1));
        line2 = new Line(new Vector2f(3,0), new Vector2f(0,1));

        lambda = line1.intersectionCo(line2);
        assertNull(lambda);
    }


    @Test
    public void testDistBetween(){
        Line line1 = new Line(new Vector2f(-1,0), new Vector2f(1,0));
        Line line2 = new Line(new Vector2f(3,-4), new Vector2f(0,1));

        Float lambda = line1.distanceTo(line2);
        assertEquals(4, lambda,0.001);

        lambda = line2.distanceTo(line1);
        assertEquals(4, lambda,0.001);
    }
}
