import java.net.URL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Bubble {

    ImageView picture_bubble;
    Image image_bubble;
    String COULEUR[] = { "rouge", "vert", "bleu", "violet", "jaune", "orange" };
    int afficher;
    int couleur;
    boolean collision_brute;
    double bubble_width, bubble_height;
    double bubbleX;
    double bubbleY;
    double moveX, moveY;
    double angle_bubble;
    int compteur_bubble_shoot;
    int speed = 7;

    // constructor for random bubbles
    Bubble() {
        int rnd = random();
        this.picture_bubble = new ImageView();
        this.image_bubble = new Image(
                getClass().getResourceAsStream("pictures/game/bubble/" + this.COULEUR[rnd] + ".png"));
        this.picture_bubble.setImage(image_bubble);
        this.afficher = 1;
        this.couleur = rnd;
        this.bubble_width = this.image_bubble.getWidth();
        this.bubble_height = this.image_bubble.getHeight();
        this.collision_brute = false;
    }

    // bubble builder with predefined color for adding a bubble to the cloud
    Bubble(int couleur) {
        this.couleur = couleur;
        this.picture_bubble = new ImageView();
        this.image_bubble = new Image(
                getClass().getResourceAsStream("pictures/game/bubble/" + this.COULEUR[couleur] + ".png"));
        this.picture_bubble.setImage(image_bubble);
        this.bubble_width = this.image_bubble.getWidth();
        this.bubble_height = this.image_bubble.getHeight();
        this.collision_brute = false;
        this.afficher = 1;
    }

    // Function that assigns bubble positions
    public void setPosition(double X, double Y) {
        this.bubbleX = X;
        this.bubbleY = Y;
    }

    // Function that chooses a number between 0 and 5 to choose a random color from
    // the "COLOR" table
    public int random() {
        return 0 + (int) (Math.random() * ((5 - 0) + 1));
    }

    // Function that loads or reloads the first bubble in the cannon
    public void reload(int couleur) {
        this.compteur_bubble_shoot++;
        this.couleur = couleur;
        this.image_bubble = new Image(
                getClass().getResourceAsStream("pictures/game/bubble/" + this.COULEUR[couleur] + ".png"));
        this.bubbleX = Canon.picture_canon.getX() + (Canon.image_canon.getWidth() / 2)
                - (this.image_bubble.getWidth() / 2);
        this.bubbleY = Canon.picture_canon.getY() - (this.image_bubble.getHeight() / 2) - 3;
        this.picture_bubble.setX(this.bubbleX);
        this.picture_bubble.setY(this.bubbleY);
        this.picture_bubble.setImage(this.image_bubble);
    }

    // Function that reloads the second bubble of the cannon
    public void second_reload(int couleur) {
        this.couleur = couleur;
        this.image_bubble = new Image(
                getClass().getResourceAsStream("pictures/game/bubble/" + this.COULEUR[couleur] + ".png"));
        this.bubbleX = Canon.picture_canon.getX() + (Canon.image_canon.getWidth() / 2)
                - (this.image_bubble.getWidth() / 2) + 8;
        this.bubbleY = Canon.picture_canon.getY() + (Canon.image_canon.getHeight() / 2)
                - (this.image_bubble.getHeight() / 2);
        this.picture_bubble.setX(this.bubbleX);
        this.picture_bubble.setY(this.bubbleY);
        this.picture_bubble.setFitWidth(34);
        this.picture_bubble.setFitHeight(34);
        this.picture_bubble.setImage(this.image_bubble);
    }

    /*
     * Function that reloads the third bubble of the cannon, "color" passed as a
     * parameter is the result of a random draw
     * among the colors themselves chosen according to the difficulty
     */
    public void third_reload(int couleur) {
        this.couleur = couleur;
        this.image_bubble = new Image(
                getClass().getResourceAsStream("pictures/game/bubble/" + this.COULEUR[couleur] + ".png"));
        this.bubbleX = Canon.picture_canon.getX() + (Canon.image_canon.getWidth() / 2)
                - (this.image_bubble.getWidth() / 2) + 10;
        this.bubbleY = Canon.picture_canon.getY() + (Canon.image_canon.getHeight() / 2)
                - (this.image_bubble.getHeight() / 2) + 48;
        this.picture_bubble.setX(this.bubbleX);
        this.picture_bubble.setY(this.bubbleY);
        this.picture_bubble.setFitWidth(30);
        this.picture_bubble.setFitHeight(30);
        this.picture_bubble.setImage(this.image_bubble);
    }

    // Function that returns a number depending on the type of collision (edge,
    // ceiling, or bubble)
    public int collision() {
        this.bubbleX = this.picture_bubble.getX();
        this.bubbleY = this.picture_bubble.getY();

        // Collision with right edge and left edge
        if (bubbleX <= 0 || bubbleX + this.image_bubble.getWidth() >= Game.game_scene.getWidth())
            return 1;
        // Collision with ceiling
        if (bubbleY <= 0) {
            Game.bubble_shoot_stop = false;
            return 2;
        }
        if (collision_bubble() == 1) {
            Game.nuage.add(Game.bubble_shoot); // Add the drawn bubble to the bubble cloud
            return 3;
        }
        return 0;
    }

    /*
     * Function that tests if there is a collision between two bubbles (if there is
     * gross collision, the function is supposed to be performed
     * a more thorough collision test per pixel but this second part is not
     * functional)
     */
    public int collision_bubble() {
        double bubbleX = this.picture_bubble.getX();
        double bubbleY = this.picture_bubble.getY();
        double width = Game.nuage.width_cloud;
        double height = Game.nuage.height_cloud;
        int i, j;

        // if(this.collision_brute == false)
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                if (Game.nuage.nuage[i][j] != null) {
                    if (bubbleY < (Game.nuage.nuage[i][j].bubbleY + this.bubble_height) &&
                            bubbleX < Game.nuage.nuage[i][j].bubbleX &&
                            bubbleX + this.bubble_width > Game.nuage.nuage[i][j].bubbleX

                            || bubbleY < (Game.nuage.nuage[i][j].bubbleY + this.bubble_height) &&
                                    bubbleX < Game.nuage.nuage[i][j].bubbleX + this.bubble_width &&
                                    bubbleX + this.bubble_width > Game.nuage.nuage[i][j].bubbleX + this.bubble_width) {
                        this.collision_brute = true;
                        return 1;
                    }
                }

            }
        }

        return 0;
    }

    // Function that displays the bubble
    public void print() {
        this.picture_bubble.setX(this.bubbleX);
        this.picture_bubble.setY(this.bubbleY);
        Game.game_root.getChildren().add(this.picture_bubble);
    }

    // Function that deletes a bubble by resetting its fields
    public void remove() {
        this.picture_bubble.setVisible(false);
        this.afficher = -1;
        this.couleur = -1;
        this.picture_bubble.setX(-100);
        this.picture_bubble.setY(-100);
        this.bubbleX = -100;
        this.bubbleY = -100;
        this.collision_brute = false;
    }

    // Function that calculates the angle for the trajectory of the bubble when
    // fired
    public double calculate_angle_bubble(double mouse_clickX, double mouse_clickY) {
        double angle_temp;
        bubbleX = this.picture_bubble.getX();
        bubbleY = this.picture_bubble.getY();

        double pivotX, pivotY;
        double distX, distY;

        pivotX = Game.bubble_shoot.picture_bubble.getX() + Game.bubble_shoot.image_bubble.getWidth() / 2;
        pivotY = Game.bubble_shoot.picture_bubble.getY() + (Game.bubble_shoot.image_bubble.getHeight() / 2);

        distX = mouse_clickX - pivotX;
        distY = mouse_clickY - pivotY;

        angle_temp = Math.atan2(distY, distX);
        angle_temp = angle_temp * (180 / Math.PI);

        if (angle_temp <= -160 || angle_temp > 90)
            angle_temp = -160;
        if (angle_temp >= -20 && angle_temp < 90)
            angle_temp = -20;

        return angle_temp;
    }

    // Function that moves the bubble depending on the angle, as long as there are
    // no collisions between bubbles or on the ceiling
    public void move(double mouse_clickX, double mouse_clickY) {
        this.bubbleX = this.picture_bubble.getX();
        this.bubbleY = this.picture_bubble.getY();

        Game.bubble_shoot_stop = true; // to stop mouse clicks during ball movement
        this.angle_bubble = calculate_angle_bubble(mouse_clickX, mouse_clickY);

        this.moveX = Math.cos(Math.toRadians(angle_bubble));
        this.moveY = Math.sin(Math.toRadians(angle_bubble));

        Timeline move_bubble = new Timeline();
        move_bubble.setCycleCount(Timeline.INDEFINITE);
        KeyFrame bubbleMove = new KeyFrame(Duration.millis(8), // 40 // fast = 20
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {

                        bubbleX += (moveX * speed);
                        bubbleY += (moveY * speed);

                        // this in order to avoid screen overruns on the last movement of the ball
                        if (bubbleY <= 0)
                            bubbleY = 0;
                        if (bubbleX <= 0)
                            bubbleX = 0;
                        if (bubbleX + bubble_width >= Game.game_scene.getWidth())
                            bubbleX = Game.game_scene.getWidth() - bubble_width;

                        picture_bubble.setX(bubbleX);
                        picture_bubble.setY(bubbleY);
                        int test_collision = collision();
                        if (test_collision == 1) {
                            double new_angle_bubble = 180 - angle_bubble;
                            moveX = Math.cos(Math.toRadians(new_angle_bubble));
                            moveY = Math.sin(Math.toRadians(new_angle_bubble));
                            angle_bubble = new_angle_bubble;
                        }

                        int x = Game.nuage.choose_bubble_on_selection(Game.nuage.selected_bubble);
                        if (test_collision == 2) {
                            move_bubble.stop();
                            reload(Game.second_bubble.couleur);
                            Game.second_bubble.second_reload(Game.third_bubble.couleur);
                            Game.third_bubble.third_reload(x);

                        }
                        if (test_collision == 3) {
                            move_bubble.stop();
                            reload(Game.second_bubble.couleur);
                            Game.second_bubble.second_reload(Game.third_bubble.couleur);
                            Game.third_bubble.third_reload(x);
                        }
                    }
                });
        move_bubble.getKeyFrames().add(bubbleMove);
        move_bubble.play();
    }
}
