import java.net.URL;
import javafx.scene.media.AudioClip;
import javafx.scene.text.*;
import javafx.scene.paint.Color;

//Class that manages the bubble cloud
public class CloudBubbles extends Bubble {

    Bubble[][] nuage;
    int width_cloud;
    int height_cloud;
    int bubble_same_color;
    int score;
    int number_of_floor;
    int browse_neighbor; // var that loops through the "neighbor_color" array to use recursion (must keep
                         // its value)
    // at the time of the recursion, so to declare as an attribute and not as a
    // local variable
    int[] selected_bubble;
    static int limit_shoot;

    // Constructor that builds the cloud according to the number of floors passed as
    // parameter
    public CloudBubbles(int number_floor) {
        selected_bubble = new int[Game.difficulty];
        fill_array_selected_bubble(this.selected_bubble);
        this.width_cloud = size_width();
        this.height_cloud = size_height();
        nuage = new Bubble[this.height_cloud][this.width_cloud];
        this.score = 0;
        print_score();
        this.bubble_same_color = 1;
        this.browse_neighbor = 1;
        this.number_of_floor = number_floor;
        fill();
    }

    // Function that resets the bubble cloud
    public void reset() {
        for (int i = 0; i < this.height_cloud; i++) {
            for (int j = 0; j < this.width_cloud; j++) {
                if (this.nuage[i][j] != null) {
                    this.nuage[i][j].remove();
                    this.nuage[i][j] = null;
                }
            }
        }
        this.score = 0;
        print_score();
        this.browse_neighbor = 1;
        fill_array_selected_bubble(this.selected_bubble);
        fill();
    }

    // Function that returns the number of bubbles the window can accommodate
    // horizontally
    int size_width() {
        return ((int) Game.game_scene.getWidth() / (int) this.image_bubble.getWidth()) - 1;
    }

    // Function that returns the number of bubbles the window can accommodate
    // vertically
    int size_height() {
        return (int) Game.game_scene.getHeight() / (int) this.image_bubble.getHeight();
    }

    /*
     * Function that returns 1 if "x" is already in the array as parameter (used to
     * choose random colors
     * depending on the difficulty)
     */
    int doublon(int[] tab, int x) {
        for (int i = 0; i < Game.difficulty; i++) {
            if (tab[i] == x)
                return 1;
        }
        return 0;
    }

    // Function that fills the array as a parameter, with a random color among the
    // total, depending on the difficulty
    void fill_array_selected_bubble(int[] tab) {
        for (int i = 0; i < Game.difficulty; i++)
            tab[i] = -1;

        int x = random();
        tab[0] = x;

        for (int i = 1; i < Game.difficulty; i++) {
            while (doublon(tab, x) == 1) {
                x = random();
            }

            tab[i] = x;
        }
    }

    // Function that chooses a color among the colors present in the
    // "selected_bubble" array
    int choose_bubble_on_selection(int[] tab) {
        int x = 0 + (int) (Math.random() * ((Game.difficulty - 1 - 0) + 1));
        return tab[x];
    }

    // Function that fills the bubble cloud with random bubbles based on difficulty
    void fill() {
        int i, j, k = 0;
        double posX = 24;
        double posY = 0;

        for (i = 0; i < this.height_cloud; i++) {
            for (j = 0; j < this.width_cloud; j++) {
                if (i < this.number_of_floor) {
                    int choice = choose_bubble_on_selection(this.selected_bubble);
                    this.nuage[i][j] = new Bubble(choice);
                    this.nuage[i][j].setPosition(posX, posY);
                } else
                    this.nuage[i][j] = null;

                posX += (this.bubble_width);
            }
            j++;
            posY += this.bubble_height;
            if (i % 2 == 0)
                posX = this.bubble_width / 2 + 24;
            else
                posX = 24;
        }
    }

    // Function that integrates a dragged bubble, into the cloud of bubbles, and
    // positions it in the right place depending on where it collided
    public void add(Bubble obj) {
        int k = 0;
        double column, line;
        int i = 0;
        line = 0.0;
        // calculer i
        while (k < this.height_cloud) {
            if (obj.bubbleY >= line && obj.bubbleY < line + this.bubble_height) {
                i = k + 1;
                k = this.height_cloud;
            }
            line += this.bubble_height;
            k += 1;
        }

        double grid = this.bubble_width / 2;
        if (i % 2 == 0)
            column = 24.0;
        else
            column = grid + 24;

        // correctly position the ball in absicce
        k = 0;
        while (k < this.width_cloud) {

            if (obj.bubbleX + grid >= column && obj.bubbleX + grid < column + this.bubble_width) {
                obj.bubbleX = column;
                obj.bubbleY = i * this.bubble_height;
            }

            column += (this.bubble_width);
            k += 1;
        }

        int j = ((int) obj.bubbleX / (int) this.bubble_width);

        this.nuage[i][j] = new Bubble(obj.couleur);
        this.nuage[i][j].setPosition(obj.bubbleX, obj.bubbleY);
        this.nuage[i][j].print();

        destroy_bubble_same_color(i, j);

        if (Game.bubble_shoot.compteur_bubble_shoot >= limit_shoot) {
            down_the_cloud();
            Game.bubble_shoot.compteur_bubble_shoot = 0;
        }

        if (test_game_over() == true) {
            Game.animation.print_toto_mood("sad");
            Game.animation.print_game_over();
            Game.blink_reset();
            Game.win_game = -1;
            Game.bubble_shoot_stop = true;
        } else if (test_win() == true) {
            Game.animation.print_toto_mood("happy");
            Game.animation.print_win();
            Game.blink_reset();
            Game.win_game = 1;
            Game.bubble_shoot_stop = true;
        } else
            Game.bubble_shoot_stop = false; // we resume the course of the game by unblocking the mouse

    }

    // Function that returns 1 if there are duplicate coordinates of a bubble in the
    // array passed as a parameter
    public int doublon_voisin(int[][] tab, int x, int y) {
        for (int i = 0; i < this.height_cloud * this.width_cloud; i++) {
            if (tab[i][0] == x && tab[i][1] == y)
                return 1;
        }
        return 0;
    }

    // Function that recursively searches for bubbles of the same color stuck
    // together, using the duplicate_neighbor function
    public void search_bubble_same_color(int i, int j, int[][] neighbor_color) {

        int compteur = 0;

        // bubble up
        if (i > 0 && this.nuage[i - 1][j] != null) {
            if (this.nuage[i][j].couleur == this.nuage[i - 1][j].couleur) {
                if (doublon_voisin(neighbor_color, i - 1, j) == 0) {
                    neighbor_color[this.bubble_same_color][0] = i - 1;
                    neighbor_color[this.bubble_same_color][1] = j;
                    this.bubble_same_color++;
                    compteur++;
                }
            }
        }
        // ----------------------------
        // right balloon
        if (j < this.width_cloud - 1 && this.nuage[i][j + 1] != null) {
            if (this.nuage[i][j].couleur == this.nuage[i][j + 1].couleur) {
                if (doublon_voisin(neighbor_color, i, j + 1) == 0) // on verifie si la boulle n'est pas déja dans le
                                                                   // tableau neighbor_color
                {
                    neighbor_color[this.bubble_same_color][0] = i;
                    neighbor_color[this.bubble_same_color][1] = j + 1;
                    this.bubble_same_color++;
                    compteur++;
                }
            }
        }
        // ----------------------------
        // left bubble
        if (j > 0 && this.nuage[i][j - 1] != null) {
            if (this.nuage[i][j].couleur == this.nuage[i][j - 1].couleur) {
                if (doublon_voisin(neighbor_color, i, j - 1) == 0) // on verifie si la boulle n'est pas déja dans le
                                                                   // tableau neighbor_color
                {
                    neighbor_color[this.bubble_same_color][0] = i;
                    neighbor_color[this.bubble_same_color][1] = j - 1;
                    this.bubble_same_color++;
                    compteur++;
                }
            }
        }

        // ----------------------------
        // bottom bubble
        if (i < this.height_cloud - 1 && this.nuage[i + 1][j] != null) {
            if (this.nuage[i][j].couleur == this.nuage[i + 1][j].couleur) {
                if (doublon_voisin(neighbor_color, i + 1, j) == 0) // on verifie si la boulle n'est pas déja dans le
                                                                   // tableau neighbor_color
                {
                    neighbor_color[this.bubble_same_color][0] = i + 1;
                    neighbor_color[this.bubble_same_color][1] = j;
                    this.bubble_same_color++;
                    compteur++;
                }
            }
        }
        // ----------------------------
        // bubble top right only for line module 2 = 1
        if (j < this.width_cloud - 1 && i > 0 && i % 2 == 1 && this.nuage[i - 1][j + 1] != null) {
            if (this.nuage[i][j].couleur == this.nuage[i - 1][j + 1].couleur) {
                if (doublon_voisin(neighbor_color, i - 1, j + 1) == 0) // we check if the ball is not already in the
                                                                       // neighbor_color array
                {
                    neighbor_color[this.bubble_same_color][0] = i - 1;
                    neighbor_color[this.bubble_same_color][1] = j + 1;
                    this.bubble_same_color++;
                    compteur++;
                }
            }
        }
        // ----------------------------
        // bottom right bubble
        if (j < this.width_cloud - 1 && i < this.height_cloud - 1 && i % 2 == 1 && this.nuage[i + 1][j + 1] != null) {
            if (this.nuage[i][j].couleur == this.nuage[i + 1][j + 1].couleur) {
                if (doublon_voisin(neighbor_color, i + 1, j + 1) == 0) // we check if the ball is not already in the
                                                                       // neighbor_color array
                {
                    neighbor_color[this.bubble_same_color][0] = i + 1;
                    neighbor_color[this.bubble_same_color][1] = j + 1;
                    this.bubble_same_color++;
                    compteur++;
                }
            }
        }
        // ----------------------------
        // bottom bubble
        if (j > 0 && i > 0 && i % 2 == 0 && this.nuage[i - 1][j - 1] != null) {
            if (this.nuage[i][j].couleur == this.nuage[i - 1][j - 1].couleur) {
                if (doublon_voisin(neighbor_color, i - 1, j - 1) == 0) // we check if the ball is not already in the
                                                                       // neighbor_color array
                {
                    neighbor_color[this.bubble_same_color][0] = i - 1;
                    neighbor_color[this.bubble_same_color][1] = j - 1;
                    this.bubble_same_color++;
                    compteur++;
                }
            }
        }
        // ----------------------------
        // bottom left bubble
        if (j > 0 && i < this.height_cloud - 1 && i % 2 == 0 && this.nuage[i + 1][j - 1] != null) {
            if (this.nuage[i][j].couleur == this.nuage[i + 1][j - 1].couleur) {
                if (doublon_voisin(neighbor_color, i + 1, j - 1) == 0) // we check if the ball is not already in the
                                                                       // neighbor_color array
                {
                    neighbor_color[this.bubble_same_color][0] = i + 1;
                    neighbor_color[this.bubble_same_color][1] = j - 1;
                    this.bubble_same_color++;
                    compteur++;
                }
            }
        }
        // ----------------------------

        if (compteur == 0)
            return;

        else {
            while (this.browse_neighbor < this.bubble_same_color) {
                search_bubble_same_color(neighbor_color[browse_neighbor][0], neighbor_color[browse_neighbor][1],
                        neighbor_color);
                this.browse_neighbor++;
            }
        }
    }

    // Function that displays the score (according to bubble_same_color) which
    // represents the number of bubbles of the same color that will be burst
    void print_score() {
        if (this.bubble_same_color >= 3)
            this.score += this.bubble_same_color * 10;
        Game.scoreText.setFont(new Font(35));
        Game.scoreText.setFill(Color.LIGHTBLUE);
        Game.scoreText.setX(750);
        Game.scoreText.setY(580);
        Game.scoreText.setText("SCORE : " + this.score);
    }

    // Function that initializes a 2d array passed as parameter with -1
    void initializate_array(int[][] tab) {
        for (int i = 0; i < this.height_cloud * this.width_cloud; i++) {
            tab[i][0] = -1;
            tab[i][1] = -1;
        }
    }

    /*
     * Function that destroys bubbles of the same color stuck together if they are >
     * or = 3, after execution of the function
     * search_bubble_same_color
     */
    public void destroy_bubble_same_color(int i, int j) {

        int[][] neighbor_color = new int[this.height_cloud * this.width_cloud][2];
        initializate_array(neighbor_color);
        neighbor_color[0][0] = i;
        neighbor_color[0][1] = j;

        final URL resource = getClass().getResource("/music/destroy_bubble.wav");
        final AudioClip destroy_bubble = new AudioClip(resource.toString());

        search_bubble_same_color(i, j, neighbor_color);

        // if more than three bubbles found then we explode them
        if (this.bubble_same_color >= 3) {
            for (i = 0; i < this.bubble_same_color; i++) {
                if (this.bubble_same_color <= 3)
                    Game.animation.print_toto_mood("pleased");
                else
                    Game.animation.print_toto_mood("happy");

                destroy_bubble.play();
                this.nuage[neighbor_color[i][0]][neighbor_color[i][1]].remove(); // remove bubbles
                this.nuage[neighbor_color[i][0]][neighbor_color[i][1]] = null;
            }
        }

        neighbor_color = null;
        print_score();
        destroy_bubble_alone();
        this.bubble_same_color = 1;
        this.browse_neighbor = 1;
    }

    /*
     * Function that destroys bubbles alone, which fit in a vacuum (works half the
     * time)
     * don't destroy piles of bubbles that fit in a vacuum
     */
    public void destroy_bubble_alone() {
        for (int i = 1; i < this.height_cloud - 1; i++) {
            for (int j = 1; j < width_cloud - 1; j++) {
                if (i % 2 == 0) {
                    if (this.nuage[i][j] != null) {
                        if (this.nuage[i - 1][j] == null &&
                                this.nuage[i - 1][j - 1] == null &&
                                this.nuage[i][j - 1] == null &&
                                this.nuage[i][j + 1] == null &&
                                this.nuage[i + 1][j - 1] == null &&
                                this.nuage[i + 1][j] == null) {
                            this.nuage[i][j].remove();
                            this.nuage[i][j] = null;
                        }
                    }
                }
            }
        }
    }

    // Function that tests if the game is lost, by testing if the limit threshold is
    // reached by a bubble
    public boolean test_game_over() {
        for (int i = 9; i == 9; i++) {
            for (int j = 0; j < this.width_cloud; j++) {
                if (this.nuage[i][j] != null)
                    return true;
            }
        }
        return false;
    }

    public boolean test_win() {
        for (int i = 0; i < this.height_cloud; i++) {
            for (int j = 0; j < this.width_cloud; j++) {
                if (this.nuage[i][j] != null) {
                    if (this.nuage[i][j].afficher != -1)
                        return false;
                }
            }
        }
        return true;
    }

    // Function that lowers the bubble cloud by one level depending on the number of
    // shots fired
    public void down_the_cloud() {
        for (int i = this.height_cloud - 2; i >= 0; i--) {
            for (int j = this.width_cloud - 1; j >= 0; j--) {
                if (this.nuage[i][j] != null) {
                    this.nuage[i + 1][j] = null;
                    this.nuage[i + 1][j] = new Bubble(this.nuage[i][j].couleur);
                    this.nuage[i + 1][j].picture_bubble.setVisible(false);

                    if (i % 2 == 0)
                        // this.nuage[i+1][j].bubbleX = this.nuage[i][j].picture_bubble.getX() + 25;
                        this.nuage[i + 1][j].bubbleX = this.nuage[i][j].bubbleX + 25;
                    else
                        // this.nuage[i+1][j].bubbleX = this.nuage[i][j].picture_bubble.getX() - 25;
                        this.nuage[i + 1][j].bubbleX = this.nuage[i][j].bubbleX - 25;

                    this.nuage[i + 1][j].bubbleY = this.nuage[i][j].picture_bubble.getY() + 50;
                    this.nuage[i][j].picture_bubble.setVisible(false);
                    this.nuage[i + 1][j].picture_bubble.setVisible(true);

                    this.nuage[i + 1][j].print();
                }
            }
        }

        // add a new line at the start of the cloud
        double posX = 24;
        double posY = 0;

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < this.width_cloud; j++) {
                int choice = choose_bubble_on_selection(this.selected_bubble);
                this.nuage[i][j] = new Bubble(choice);
                this.nuage[i][j].setPosition(posX, posY);
                posX += this.bubble_width;
                this.nuage[i][j].print();
            }
        }

    }

    // Function that displays the bubble cloud
    @Override
    public void print() {
        int i, j;
        for (i = 0; i < this.height_cloud; i++) {
            for (j = 0; j < this.width_cloud; j++) {
                if (this.nuage[i][j] != null) {
                    this.nuage[i][j].print();
                }
            }
        }
    }

}
