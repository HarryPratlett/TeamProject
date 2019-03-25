package junit.myst.world.collisions;

import com.myst.world.collisions.AABB;
import com.myst.world.collisions.Line;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.joml.Vector2f;
import org.junit.Test;

public class AABBTest {
    @Test
    public void testHitbox(){
        Line line = new Line(new Vector2f(0,0), new Vector2f(1,1));
        AABB box = new AABB(new Vector2f(3,3), new Vector2f(0.5f,0.5f));
        boolean collision = box.isColliding(line,10);
        assertTrue(collision);

        line = new Line(new Vector2f(0,0), new Vector2f(-1,-1));
        box = new AABB(new Vector2f(3,3), new Vector2f(0.5f,0.5f));
        collision = box.isColliding(line, 10);
        assertFalse(collision);

        line = new Line(new Vector2f(0,0), new Vector2f(1,1));
        box = new AABB(new Vector2f(3,3), new Vector2f(0.5f,0.5f));
        collision = box.isColliding(line, 3);
        assertFalse(collision);

        line = new Line(new Vector2f(1,1), new Vector2f(1,1));
        box = new AABB(new Vector2f(3,3), new Vector2f(0.5f,0.5f));
        collision = box.isColliding(line, 10);
        assertTrue(collision);

        line = new Line(new Vector2f(0,0), new Vector2f(-1,-1));
        box = new AABB(new Vector2f(-2,-1.75f), new Vector2f(0.5f,0.5f));
        collision = box.isColliding(line, 10);
        assertTrue(collision);

        line = new Line(new Vector2f(0,0), new Vector2f(-1,-1));
        box = new AABB(new Vector2f(1,-1f), new Vector2f(0.5f,0.5f));
        collision = box.isColliding(line, 10);
        assertFalse(collision);

    }

}
