package minesweeper;

import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Minesweeper extends Application {

	public static int[][] field = new int[9][9];
	public static Button[][] btn_field = new Button[9][9];
	public static int flagCounter = 0;
	public static int maxFlagCounter = 0;

	public static void main(String[] args) {
		Random rnd = new Random();

		for (int z = 0; z < 9; z++) {
			int i = rnd.nextInt(9);
			int j = rnd.nextInt(9);

			if (field[i][j] != 9) {
				field[i][j] = 9;
			} else {
				z--;
				continue;
			}

			int starti = i - 1;
			int startj = j - 1;
			int endi = i + 1;
			int endj = j + 1;

			if (starti == -1) {
				starti = 0;
			}
			if (startj == -1) {
				startj = 0;
			}
			if (endi == 9) {
				endi = 8;
			}
			if (endj == 9) {
				endj = 8;
			}

			for (; starti <= endi; starti++) {
				for (int tmp = startj; tmp <= endj; tmp++) {
					if (field[starti][tmp] != 9)
						field[starti][tmp]++;
				}
			}
		}

		launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {
		GridPane group = new GridPane();
		Scene scene = new Scene(group, 800, 500);

		Image image1 = new Image(Minesweeper.class.getResourceAsStream("bombe.png"));
		Image image2 = new Image(Minesweeper.class.getResourceAsStream("flag.png"));

		stage.setScene(scene);
		stage.setTitle("Minesweeper");
		stage.getIcons().add(new Image(Minesweeper.class.getResourceAsStream("icon.png")));
		group.setAlignment(Pos.CENTER);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Button btn = new Button();
				btn_field[i][j] = btn;
				group.add(btn, i, j);
				btn.setPrefSize(50, 50);
				int tmpi = i;
				int tmpj = j;

				btn.setFont(Font.font("arial", 20));

				switch (field[i][j]) {
				case 1:
					btn.setTextFill(Color.DARKBLUE);
					break;
				case 2:
					btn.setTextFill(Color.DARKGREEN);
					break;
				case 3:
					btn.setTextFill(Color.RED);
					break;
				case 4:
					btn.setTextFill(Color.DARKMAGENTA);
					break;
				case 5:
					btn.setTextFill(Color.DARKRED);
					break;
				case 6:
					btn.setTextFill(Color.DARKORANGE);
					break;
				case 7:
					btn.setTextFill(Color.CHOCOLATE);
					break;
				case 8:
					btn.setTextFill(Color.YELLOW);
					break;
				}

				btn.setOnMouseClicked(tag_the_bomb -> {
					if (flagCounter == 9) {
						Alert alertYouWon = new Alert(AlertType.CONFIRMATION, "YOU WON!!!");
						alertYouWon.setHeaderText("You flagged all bombs :)");
						alertYouWon.show();
					}
					if (tag_the_bomb.getButton() != MouseButton.SECONDARY || !btn.getText().isEmpty())
						return;

					if (btn.getGraphic() == null) {
						if (maxFlagCounter == 9) {
							return;
						}
						if (field[tmpi][tmpj] == 9) {
							flagCounter++;
						}
						maxFlagCounter++;
						ImageView image_view2 = new ImageView(image2);
						btn.setGraphic(image_view2);
						image_view2.setFitHeight(btn.getPrefHeight() - 28);
						image_view2.setFitWidth(btn.getPrefWidth() - 28);
					} else {
						btn.setGraphic(null);
						maxFlagCounter--;
						if (field[tmpi][tmpj] == 9)
							flagCounter--;
					}
				});

				btn.setOnAction(action -> {

					if (!btn.getText().isEmpty() || btn.getGraphic() != null)
						return;

					if (field[tmpi][tmpj] == 9) {
						ImageView image_view1 = new ImageView(image1);
						image_view1.setFitHeight(btn.getPrefHeight() - 28);
						image_view1.setFitWidth(btn.getPrefWidth() - 28);
						btn.setGraphic(image_view1);
						Alert alertbombe = new Alert(AlertType.CONFIRMATION, "YOU LOST!!!");
						alertbombe.setHeaderText("You stepped on a bomb...");
						alertbombe.show();
						stage.close();
					} else
						btn.setText(String.valueOf(field[tmpi][tmpj]));

					if (field[tmpi][tmpj] == 0) {
						if (tmpj != 0)
							btn_field[tmpi][tmpj - 1].getOnAction().handle(action);
						if (tmpi != 0)
							btn_field[tmpi - 1][tmpj].getOnAction().handle(action);
						if (tmpj != 8)
							btn_field[tmpi][tmpj + 1].getOnAction().handle(action);
						if (tmpi != 8)
							btn_field[tmpi + 1][tmpj].getOnAction().handle(action);
					}
				});

			}
		}

		stage.show();

	}

}
