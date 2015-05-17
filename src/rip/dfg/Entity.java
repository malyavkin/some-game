package rip.dfg;
import org.newdawn.slick.opengl.Texture;

/**
 *  Facing:
 *  8   1   2
 *    \ | /
 *  7 - 0 - 3
 *    / | \
 *  6   5   4
 */

public class Entity {
    // position in px
    public Point position = new Point();
    public int facing = 5;
    public Model model;
    public Entity(Point position) {
        this.position = position;
    }

    public Point[] getAnglesOfSide(Direction direction) {
        Point[] out = new Point[2];

        switch (direction) {
            case UP:
                out[0] = getAngle(0);
                out[1] = getAngle(1);
                break;
            case RIGHT:
                out[0] = getAngle(1);
                out[1] = getAngle(2);
                break;
            case DOWN:
                out[0] = getAngle(2);
                out[1] = getAngle(3);
                break;
            case LEFT:
                out[0] = getAngle(3);
                out[1] = getAngle(0);
                break;
        }

        return out;
    }

    /**
     * Gets coordinates of model angle
     * @param n 0-3; 0 - top-left, 1 - top-right, 2 - bottom-right, 3 - bottom-left
     * @return Coordinates of corresponding angle
     */
    public Point getAngle(int n) {
        Point angle;
        switch (n) {
            case 0:
            default:
                angle = position;
                break;
            case 1:
                angle = position.add(model.actual.size.onlyX()).add(Point.left);
                break;
            case 2:
                angle = position.add(model.actual.size).add(new Point(-1, -1));
                break;
            case 3:
                angle = position.add(model.actual.size.onlyY()).add(Point.up);
                break;
        }
        return angle;
    }


    public Rectangle getRectangle(){
        return new Rectangle(this.position, this.model.actual.size);

    }

    //
    public int movementSpeed, attackSpeed;
    public int hp, max_hp;
    public int mp, max_mp;
    public int ad, ap;
    public int armor, magic_resistance;
    public Rectangle getBasicAttackArea(){
        return new Rectangle(0,0,0,0);

    }

    // animation stuff

    public Animation currentAnimation;
    public String fallbackAnimation;
    public int currentFrame;
    public long time;
    private long animationStart;
    public int animationDuration = 0;


    public void setAnimation(String animation) {
        // don't need to set this more
        if(model.animations.get(animation) == currentAnimation) return;

        // getting animation from storage
        Animation requestedAnimation = model.animations.get(animation);
        if(requestedAnimation == null) {
            requestedAnimation = model.animations.get("default");
        }

        // if there is an interruption animation playing
        if(currentAnimation!=null && currentAnimation.interrupting && System.currentTimeMillis() < animationDuration+animationStart){
            fallbackAnimation = requestedAnimation.name;
        } else {
            currentAnimation = requestedAnimation;

            animationDuration = 0;
            for (int i = 0; i < currentAnimation.timings.length; i++) {
                animationDuration += currentAnimation.timings[i];
            }
            animationStart = System.currentTimeMillis();
        }

        //setup

    }
    public Texture getCurrentTexture() {
        if(currentAnimation == null) {
            setAnimation("default");
        }
        if(currentAnimation.interrupting && System.currentTimeMillis() >= animationDuration+animationStart){
            setAnimation(fallbackAnimation);
        }
        time = (System.currentTimeMillis() - animationStart);
        long time1 = time%animationDuration;
        currentFrame = -1;
        for (int i = 0; i < currentAnimation.timings.length; i++) {

            if(time1 < currentAnimation.timings[i] || currentAnimation.timings[i] == -1) {
                currentFrame = currentAnimation.tiles[i];
                break;
            } else {
                time1 -=currentAnimation.timings[i];
            }
        }
        if(currentFrame == -1) {
            System.out.println("wtf? currentFrame == -1");
        }
        return model.res.textures[currentFrame];
    }
}


class Character extends Entity {
    public Character(Point position) {
        super(position);
    }

    public Rectangle getBasicAttackArea(){
        // this is model position
        return this.getRectangle()
                // move it to the facing direction
                .move(InputManager.getPointFrom8way(facing).mul(this.model.actual.size));
    }
}

class Knight extends Character {
    public Knight(Point point){
        super(point);
        Stats stats = StatLoader.get("knight");
        this.max_hp = stats.max_hp;
        this.hp = stats.max_hp;
        this.max_mp = stats.max_mp;
        this.mp = stats.max_mp;
        this.movementSpeed = stats.movementSpeed;
        this.attackSpeed = stats.attackSpeed;
        this.ad = stats.ad;
        this.ap = stats.ap;
        this.armor = stats.armor;
        this.magic_resistance = stats.magic_resistance;
    }
}