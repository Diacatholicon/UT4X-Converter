package org.xtx.ut4converter.t3d;

import org.junit.Assert;
import org.junit.Test;
import org.xtx.ut4converter.MapConverter;
import org.xtx.ut4converter.UTGames;

import javax.vecmath.Point2d;


public class T3DUE4TerrainTest {


    /**
     * Give a global index (starting at 0), test computation of coordinates
     * (assuming X axis is from left to right
     * and y axis is from top to bottom)
     */
    @Test
    public void testGetCoordinatesForIndexInSquareSize(){

        // index 28 means it's 29th value (0,1, .... 28)
        // -- X ->
        // - - - - - - - - - - (9)
        // - - - - - - - - - - (19)
        // - - - - - - - - * - (29)
        // - - - - - - - - - - (39)
        // - - - - - - - - - - (49)
        // |
        // Y
        // |
        // V

        final Point2d result = T3DUE4Terrain.getCoordinatesForIndexInSquareSize(28, 10, 5);

        // should return (x=8, y = 2)
        Assert.assertEquals(8d, result.getX(), 0.01d);
        Assert.assertEquals(2d, result.getY(), 0.01d);
    }

    @Test
    public void testGetCollisionComponentFromHeightMapIndex(){

        final MapConverter mc = new MapConverter(UTGames.UTGame.UT3, UTGames.UTGame.UT4);

        // simulating an ut3 terrain with 4 terrain compoonents
        final T3DUE3Terrain.TerrainComponent  tc1 = new T3DUE3Terrain.TerrainComponent();

    }
}