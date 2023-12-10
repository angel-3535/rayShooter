package weapon;

import Entity.Entity;
import gfx.Texture;
import gfx.Window;
import util.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class Weapon {
    Entity owner;

    Projectile bullet;

    double dmg;
    double fireRate;
    double fireCD = 0;
    double reloadCooldown;
    double activeRC = -0.01;
    int magSize;
    int currentMag;
    ArrayList<Projectile> liveProjectiles = new ArrayList<>();
    double lifeTime;
    Texture img;

    @Override
    public String toString() {
        return String.format("%d/%s",
                currentMag, magSize);
    }

    public Weapon(Entity owner, double dmg, double fireRate, double reloadCooldown, int magSize, double lifeTime) {
        this.owner = owner;
        this.dmg = dmg;
        this.fireRate = fireRate;
        this.reloadCooldown = reloadCooldown;
        this.magSize = magSize;
        this.currentMag = magSize;
        this.lifeTime = lifeTime;
        try {
            this.img = new Texture("src/assets/weapons/handgun.png");
        }catch (Exception e){

        }
    }


    public void shoot(double x, double y) {
        if(activeRC>0){
            return;
        }
        if (fireCD <=0 && currentMag > 0) {
            // shoot bullet

            Vector2D origin = new Vector2D(owner.transform.getCenterX(),owner.transform.getCenterY());
            Vector2D destination = new Vector2D((float) x, (float) y);

            Vector2D bulletTravelDirection = origin.getVectorTo(destination);

//
//            //createProjectile(travelDirection);
//            liveProjectiles.add(new weapon.Projectile(
//                    (int) (origin.getX()),
//                    (int) (origin.getY()),
//                    bulletTravelDirection,
//                    this.lifeTime)
//            );
            fireCD = fireRate;
            currentMag--;
        } else if (currentMag == 0){
            reload();
        }
    }
    public void createProjectile(Vector2D travelDirection) {
//        liveProjectiles.add(new weapon.Projectile(
//                (int) (owner.transform.getX() + owner.transform.getSize().getX()/2),
//                (int) (owner.transform.getY() + owner.transform.getSize().getY()/2),
//                travelDirection,
//                this.lifeTime)
//        );

    }

    public void reload() {
        // Put weapon into cooldown state
        activeRC = reloadCooldown;
        currentMag = magSize;
    }


    public void update(double deltaTime) {
        fireCD -= deltaTime;
        activeRC -= deltaTime;
        for (int i =0; i < liveProjectiles.size(); i++) {

            if (liveProjectiles.get(i) == null){
                continue;
            }
//            if (liveProjectiles.get(i).getToBeDestroy()){
                liveProjectiles.remove(i);
                continue;
            }
//            liveProjectiles.get(i).update(deltaTime);

    }

    public void draw(Graphics g) {
        g.drawImage(
                img.img.getImage(),
            Window.getWindow().getWidth()-img.img.getIconWidth(),
            Window.getWindow().getHeight()-img.img.getIconHeight(),
                null
        );
        for (Projectile p:liveProjectiles) {
//            p.draw(g);
        }
    }

}
