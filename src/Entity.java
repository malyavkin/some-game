
class Facing{
    public static byte UP = 2;
    public static byte RIGHT = 0;
    public static byte DOWN = 6;
    public static byte LEFT = 4;
}



public class Entity {
    // position in px
    public Point position = new Point(0,0);
    public byte facing = Facing.DOWN;
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
     * @return
     */
    public Point getAngle(int n) {
        Point angle;
        switch (n) {
            case 0:
            default:
                angle = position;
                break;
            case 1:
                angle = position.add(model.size.onlyX()).add(Point.left);
                break;
            case 2:
                angle = position.add(model.size).add(new Point(-1,-1));
                break;
            case 3:
                angle = position.add(model.size.onlyY()).add(Point.up);
                break;
        }
        return angle;
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