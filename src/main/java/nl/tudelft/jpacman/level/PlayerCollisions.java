package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;

/**
 * A simple implementation of a collision map for the JPacman player.
 * <p>
 * It uses a number of instanceof checks to implement the multiple dispatch for the 
 * collisionmap. For more realistic collision maps, this approach will not scale,
 * and the recommended approach is to use a {@link CollisionInteractionMap}.
 *
 * @author Arie van Deursen, 2014
 *
 */

public class PlayerCollisions implements CollisionMap {

    private PointCalculator pointCalculator;

    /**
     * Create a simple player-based collision map, informing the
     * point calculator about points to be added.
     *
     * @param pointCalculator
     *             Strategy for calculating points.
     */
    public PlayerCollisions(PointCalculator pointCalculator) {
        this.pointCalculator = pointCalculator;
    }

    @Override
    public CollisionSignal collide(Unit mover, Unit collidedOn) {
        if (mover instanceof Player) {
            return playerColliding((Player) mover, collidedOn);
        }
        else if (mover instanceof Ghost) {
            return ghostColliding((Ghost) mover, collidedOn);
        }
        else if (mover instanceof Pellet) {
            return pelletColliding((Pellet) mover, collidedOn);
        }
        return CollisionSignal.NONE;
    }

    private CollisionSignal playerColliding(Player player, Unit collidedOn) {
        if (collidedOn instanceof Ghost) {
            return playerVersusGhost(player, (Ghost) collidedOn);
        }
        if (collidedOn instanceof Pellet) {
            return playerVersusPellet(player, (Pellet) collidedOn);
        }
        return CollisionSignal.NONE;
    }

    private CollisionSignal ghostColliding(Ghost ghost, Unit collidedOn) {
        if (collidedOn instanceof Player) {
            return playerVersusGhost((Player) collidedOn, ghost);
        }
        return CollisionSignal.NONE;
    }

    private CollisionSignal pelletColliding(Pellet pellet, Unit collidedOn) {
        if (collidedOn instanceof Player) {
            return playerVersusPellet((Player) collidedOn, pellet);
        }
        return CollisionSignal.NONE;
    }


    /**
     * Actual case of player bumping into ghost or vice versa.
     *
     * @param player
     *          The player involved in the collision.
     * @param ghost
     *          The ghost involved in the collision.
     */
    public CollisionSignal playerVersusGhost(Player player, Ghost ghost) {
        pointCalculator.collidedWithAGhost(player, ghost);
        player.kill();
        player.setKiller(ghost);
        return CollisionSignal.PLAYER_KILLED;
    }

    /**
     * Actual case of player consuming a pellet.
     *
     * @param player
     *           The player involved in the collision.
     * @param pellet
     *           The pellet involved in the collision.
     */
    public CollisionSignal playerVersusPellet(Player player, Pellet pellet) {
        pointCalculator.consumedAPellet(player, pellet);
        pellet.leaveSquare();
        return CollisionSignal.PELLET_CONSUMED;
    }

}
