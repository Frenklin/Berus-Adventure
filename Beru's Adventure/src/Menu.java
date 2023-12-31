import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.*;
import javafx.animation.FadeTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;

//Class that manages the main menu, its buttons and its events
public class Menu {

    static Scene menu_scene, rules_scene;
    Group menu_root = new Group();
    static Game game = new Game();
    ImageView picture_background, picture_logo, picture_play, picture_quit, picture_rules, picture_return,
            picture_easy, picture_medium, picture_hard;
    Image image_play, image_logo, image_background, image_quit, image_rules, image_return,
            image_easy, image_medium, image_hard;
    Text text_button = new Text();
    Text text_rules = new Text();
    Rectangle rectangle_rules;
    FadeTransition blink;
    HBox hbox_picture_difficulty;

    // Function that loads the menu with the functions created in the Menu class
    public void load(Stage Stage) {
        Menu.menu_scene = new Scene(menu_root, 1024, 639, Color.LIGHTBLUE);

        print_background();
        print_logo();
        print_button_play();
        print_button_quit();
        print_button_rules();
        this.menu_root.getChildren().add(text_button);
        load_picture_difficulty();
        mouse_event_menu(Stage);
        load_rules();

        Stage.setScene(menu_scene);
    }

    // Function that makes a zoom effect when the mouse cursor passes over
    // a menu button
    void enlarge_button(ImageView picture, Image image, boolean effect, int zoom) {
        if (effect == true) {
            picture.setFitWidth(image.getWidth() + zoom);
            picture.setFitHeight(image.getHeight() + zoom);
            picture.setX(picture.getX() - (zoom / 2));
            picture.setY(picture.getY() - (zoom / 2));
        }

        if (effect == false) {
            picture.setFitWidth(image.getWidth());
            picture.setFitHeight(image.getHeight());
            picture.setX(picture.getX() + (zoom / 2));
            picture.setY(picture.getY() + (zoom / 2));
        }
    }

    // Function that handles all mouse events in the Menu (Click button, Hover
    // of the cursor on a button ...)
    public void mouse_event_menu(Stage Stage) {
        DropShadow effect = new DropShadow();
        effect.setOffsetY(0.0f);
        effect.setOffsetX(0.0f);

        // ---BUTTON PLAY---------------
        this.picture_play.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                print_difficulty(Stage);
            }
        });

        this.picture_play.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                text_button.setFont(new Font(35));
                text_button.setFill(Color.ORANGE);
                text_button.setX(picture_play.getX() + 25);
                text_button.setY(picture_play.getY() - 30);
                text_button.setText("PLAY");
                text_button.setEffect(effect);
                text_button.setVisible(true);

                enlarge_button(picture_play, image_play, true, 35);
                effect.setColor(Color.ORANGE);
                picture_play.setEffect(effect);
            }
        });

        this.picture_play.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                enlarge_button(picture_play, image_play, false, 35);
                text_button.setVisible(false);
                picture_play.setEffect(null);
            }
        });

        // ---BUTTON RULES---------------
        picture_rules.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                text_button.setFont(new Font(25));
                text_button.setFill(Color.GREEN);
                text_button.setX(picture_rules.getX() + 5);
                text_button.setY(picture_rules.getY() - 25);
                text_button.setText("RULES");
                text_button.setEffect(effect);
                text_button.setVisible(true);

                enlarge_button(picture_rules, image_rules, true, 25);
                effect.setColor(Color.GREEN);
                picture_rules.setEffect(effect);
            }
        });

        picture_rules.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                enlarge_button(picture_rules, image_rules, false, 25);
                text_button.setVisible(false);
                picture_rules.setEffect(null);
            }
        });

        picture_rules.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                hbox_picture_difficulty.setVisible(false);
                text_button.setVisible(false);
                picture_rules.setEffect(null);
                print_rules();
            }
        });

        // ---BUTTON QUIT---------------
        this.picture_quit.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                text_button.setFont(new Font(25));
                text_button.setFill(Color.RED);
                text_button.setX(picture_quit.getX() + 15);
                text_button.setY(picture_quit.getY() - 25);
                text_button.setText("EXIT");
                text_button.setEffect(effect);
                text_button.setVisible(true);

                enlarge_button(picture_quit, image_quit, true, 25);
                effect.setColor(Color.RED);
                picture_quit.setEffect(effect);
            }
        });

        this.picture_quit.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                enlarge_button(picture_quit, image_quit, false, 25);
                text_button.setVisible(false);
                picture_quit.setEffect(null);
            }
        });

        this.picture_quit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.exit(0);
            }
        });

    }

    // Function for mouse events on back button in rules
    public void mouse_event_rules() {
        DropShadow effect = new DropShadow();
        effect.setOffsetY(0.0f);
        effect.setOffsetX(0.0f);
        this.text_button.setEffect(effect);

        this.picture_return.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                effect.setColor(Color.GREEN);
                picture_return.setEffect(effect);
            }
        });

        this.picture_return.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                picture_return.setEffect(null);
            }
        });

        this.picture_return.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                rectangle_rules.setVisible(false);
                text_rules.setVisible(false);
                picture_return.setVisible(false);
                blink.stop();
            }
        });
    }

    // Function that loads the instructions (text in a rectangle)
    public void load_rules() {
        this.rectangle_rules = new Rectangle();
        this.rectangle_rules.setX(100);
        this.rectangle_rules.setY(100);
        this.rectangle_rules.setWidth(menu_scene.getWidth() - 200);
        this.rectangle_rules.setHeight(menu_scene.getHeight() - 200);
        this.rectangle_rules.setArcWidth(20);
        this.rectangle_rules.setArcHeight(20);
        this.rectangle_rules.setFill(Color.ALICEBLUE);

        this.text_rules.setFont(new Font(20));
        this.text_rules.setX(rectangle_rules.getX() + 20);
        this.text_rules.setY(rectangle_rules.getY() + 20);
        this.text_rules.setStyle("-fx-fill: #4F8A10;-fx-font-weight:bold;");
        this.text_rules.setText("\n\t\t\t\t\t\t     RULES OF THE GAME "
                + "\n\n\n\n \t\t Aim and shoot to send the bubble to clump together at least\n"
                + " \t\t 3 bubbles of the same color to make them disappear. The goal is\n"
                + " \t\t to remove each color as you go until it \n"
                + " \t\t none left at all.\n\n"
                + "\t\t But if you want to increase your score a maximum and so\n"
                + "\t\t try to beat the record, you have to juggle at the end with two \n"
                + "\t\tcolors, and make it last to accumulate maximum points.\n\n\n"
                + "\t\t\t\t\t\t\t GOOD LUCK!!!");

        this.picture_return = new ImageView();
        this.picture_return.setVisible(true);
        this.image_return = new Image(getClass().getResourceAsStream("pictures/menu/return.png"));
        this.picture_return.setX((menu_scene.getWidth()) - (this.image_play.getWidth()) - 20);
        this.picture_return.setY(10);
        this.picture_return.setImage(this.image_return);

    }

    // Function that displays the instructions and loads the mouse_event function
    // for
    // the "rules" part
    public void print_rules() {
        this.rectangle_rules.setVisible(true);
        this.text_rules.setVisible(true);
        this.picture_return.setVisible(true);
        this.menu_root.getChildren().add(this.rectangle_rules);
        this.menu_root.getChildren().add(this.text_rules);
        print_button_return();
        mouse_event_rules();
    }

    // Function that displays the back button (in the rules part)
    public void print_button_return() {
        this.menu_root.getChildren().add(this.picture_return);
    }

    // Function that displays the Menu background
    public void print_background() {
        this.picture_background = new ImageView();
        this.picture_background.setX(-35);
        this.image_background = new Image(getClass().getResourceAsStream("pictures/menu/Background.png"));
        this.picture_background.setImage(this.image_background);

        this.menu_root.getChildren().add(this.picture_background);
    }

    // Function that displays the "Beru's Adventure" logo
    public void print_logo() {
        this.picture_logo = new ImageView();
        this.image_logo = new Image(getClass().getResourceAsStream("pictures/menu/logo.png"));
        this.picture_logo.setX((menu_scene.getWidth() / 2) - (this.image_logo.getWidth() / 2));
        this.picture_logo.setY(100);
        picture_logo.setImage(this.image_logo);

        this.menu_root.getChildren().add(this.picture_logo);
    }

    // Function that displays the play button
    public void print_button_play() {
        this.picture_play = new ImageView();
        this.image_play = new Image(getClass().getResourceAsStream("pictures/menu/play.png"));
        this.picture_play.setX((menu_scene.getWidth() / 2) - (this.image_play.getWidth() / 2));
        this.picture_play.setY(320);
        this.picture_play.setImage(this.image_play);

        this.menu_root.getChildren().add(this.picture_play);
    }

    // Function that displays the quit button
    public void print_button_quit() {
        this.picture_quit = new ImageView();
        this.image_quit = new Image(getClass().getResourceAsStream("pictures/menu/quit.png"));
        this.picture_quit.setX((this.picture_play.getX() + this.image_play.getWidth() + this.image_quit.getWidth()));
        this.picture_quit.setY(420);
        this.picture_quit.setImage(this.image_quit);

        this.menu_root.getChildren().add(picture_quit);
    }

    // Function that displays the rules button
    public void print_button_rules() {
        this.picture_rules = new ImageView();
        this.image_rules = new Image(getClass().getResourceAsStream("pictures/menu/rules.png"));
        this.picture_rules.setX(this.picture_play.getX() - (this.image_rules.getWidth() * 2));
        this.picture_rules.setY(420);
        this.picture_rules.setImage(this.image_rules);

        this.menu_root.getChildren().add(this.picture_rules);
    }

    // Function that displays loads images (easy, medium and hard) and
    // position in their place
    void load_picture_difficulty() {
        this.hbox_picture_difficulty = new HBox();
        this.hbox_picture_difficulty.setSpacing(140);

        this.picture_easy = new ImageView();
        this.image_easy = new Image(getClass().getResourceAsStream("pictures/menu/easy.png"));
        this.picture_easy.setX(200);
        this.picture_easy.setY(Menu.menu_scene.getHeight() - this.image_easy.getHeight() - 20);
        this.picture_easy.setImage(this.image_easy);

        this.picture_medium = new ImageView();
        this.image_medium = new Image(getClass().getResourceAsStream("pictures/menu/medium.png"));
        this.picture_medium.setX(200);
        this.picture_medium.setY(this.picture_easy.getY());
        this.picture_medium.setImage(this.image_medium);

        this.picture_hard = new ImageView();
        this.image_hard = new Image(getClass().getResourceAsStream("pictures/menu/hard.png"));
        this.picture_hard.setX(200);
        this.picture_hard.setY(this.picture_hard.getY());
        this.picture_hard.setImage(this.image_hard);

        this.hbox_picture_difficulty.getChildren().addAll(this.picture_easy, this.picture_medium, this.picture_hard);
        this.hbox_picture_difficulty.setLayoutX(160);
        this.hbox_picture_difficulty.setLayoutY(560);
    }

    // Function that displays images (easy, medium and hard) and handles events
    // mouse over these
    void print_difficulty(Stage Stage) {

        this.hbox_picture_difficulty.setVisible(true);
        this.menu_root.getChildren().add(this.hbox_picture_difficulty);

        DropShadow effect = new DropShadow();
        effect.setOffsetY(0.0f);
        effect.setOffsetX(0.0f);
        effect.setColor(Color.ORANGE);

        // ---BUTTON EASY-----------
        this.picture_easy.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                hbox_picture_difficulty.setVisible(false);
                game.load(Stage, "easy");
            }
        });
        this.picture_easy.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                picture_easy.setEffect(effect);
            }
        });
        this.picture_easy.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                picture_easy.setEffect(null);
            }
        });

        // ---BUTTON MEDIUM-----------
        this.picture_medium.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                hbox_picture_difficulty.setVisible(false);
                game.load(Stage, "medium");
            }
        });
        this.picture_medium.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                picture_medium.setEffect(effect);
            }
        });
        this.picture_medium.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                picture_medium.setEffect(null);
            }
        });

        // ---BUTTON HARD-----------
        this.picture_hard.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                hbox_picture_difficulty.setVisible(false);
                game.load(Stage, "hard");
            }
        });
        this.picture_hard.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                picture_hard.setEffect(effect);
            }
        });
        this.picture_hard.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                picture_hard.setEffect(null);
            }
        });
    }
}
