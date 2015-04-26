public class Entity {
    // position in px
    public Point position = new Point(0,0);
    public Direction facing = Direction.DOWN;
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
    public int getFacingTextureID(Direction direction){
        switch (direction) {
            case RIGHT:
                return 0;
            case UP:
                return 2;
            case LEFT:
                return 4;
            case DOWN:
                return 6;
        }
        return 0;
    }

    public Rectangle getRectangle(){
        return new Rectangle(this.position, this.model.actual.size);

    }

    //
    public int movementSpeed, attackSpeed;
    public int HP, maxHP;
    public int mana, maxMana;
    public int AD, AP;
    public int armor, magres;


}


class Character extends Entity {
    public Character(Point position) {
        super(position);
    }

    public Rectangle getBasicAttackArea(){
        // this is model position
        return this.getRectangle()
                // move it to the facing direction
                .move(Point.fromDirection(this.facing).mul(this.model.actual.size));
    }
}

class Knight extends Character {
    public Knight(Point point){
        super(point);
        this.maxHP = 600;
        this.HP = 600;
        this.maxMana = 400;
        this.mana = 400;
        this.movementSpeed =1;
        this.attackSpeed = 1500;
        this.AD = 50;
        this.AP = 0;
        this.armor = 50;
        this.magres = 50;


    }
}