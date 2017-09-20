package cnc.client.fx;

import cnc.client.rest.WSDAO;

import cnc.model.Attendee;

import java.io.ByteArrayInputStream;

import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import org.apache.commons.codec.binary.Base64;


/**
 * Sample application that shows how the sized of controls can be managed.
 * Sample is for demonstration purposes only, most controls are inactive.
 */
public class CNCFXBrowser extends Application {

  

    private BorderPane border;


    private ArrayList<Attendee> attendees = null;



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(CNCFXBrowser.class, args);
    }

    @Override
    public void start(Stage stage) {

      


        border = new BorderPane();
        //Search pane - top
        border.setTop(searchPane());

        border.setCenter(createNoImagePane(false));

        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("OOW17 CNC Matrix");
        stage.show();


    }

    //Generate page(s) results
    private Pagination getPagination() {

        Pagination pagination;

        //How many pages required
        pagination =
            new Pagination((attendees.size() % itemsPerPage() == 0 ? 
                           attendees.size() / itemsPerPage() :
                        (attendees.size() / itemsPerPage() + 1)));


   
        pagination.setPageFactory((Integer pageIndex) -> createPagePaneImages(pageIndex));


        AnchorPane anchor = new AnchorPane();
        AnchorPane.setTopAnchor(pagination, 10.0);
        AnchorPane.setRightAnchor(pagination, 10.0);
        AnchorPane.setBottomAnchor(pagination, 10.0);
        AnchorPane.setLeftAnchor(pagination, 10.0);
        anchor.getChildren().addAll(pagination);

        return pagination;

    }

    private HBox createNoImagePane(boolean notfound) {

        HBox hbox = new HBox();
        
        hbox.setPrefSize(300,300);
        Label noImages;
        if (notfound) {
            noImages = new Label("No results for this search were found");
        } else {
            noImages = new Label("");
        }
        
        hbox.getChildren().add(noImages);

        return hbox;
    }


    private HBox createPagePaneImages(int pageIndex) {

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();

        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.setVgap(15);
        grid.setHgap(15);

        int first = pageIndex * itemsPerPage();
        int last = (first + itemsPerPage()) > attendees.size() ? attendees.size() : (first + itemsPerPage());
        int j = 0;

        for (int i = first; i < last; i++) {
            Base64 bs = new Base64();
            ByteArrayInputStream byteImage = new ByteArrayInputStream(bs.decode(attendees.get(i).getImageAssoc()));
            Image image = new Image(byteImage, 150, 150, true, true);
            ImageView imageV = new ImageView(image);

            Attendee attendee = attendees.get(i);
            imageV.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    event.consume();
                    confirmPrint(attendee);
                }
            });

            
            Label name = new Label(attendees.get(i).getName());
            
            VBox cell = new VBox();
            cell.setAlignment(Pos.CENTER);
            
            cell.getChildren().add(imageV);
            cell.getChildren().add(name);
            
            GridPane.setConstraints(cell, j % itemsPerRow(), j / itemsPerRow());
            grid.getChildren().add(cell);
            j++;

        }

        vBox.getChildren().add(grid);
        hBox.getChildren().add(vBox);
        return hBox;
    }

    // Items per page
    public int itemsPerPage() {
        return 4;
    }

    //Rows per page, ideally -  items = rows x rows
    public int itemsPerRow() {
        return 2;
    }


    private GridPane searchPane() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(10);


        //        grid.setPrefSize(300, 300);
        //        // never size the gridpane larger than its preferred size:
        //        grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);


        final TextField name = new TextField();

        name.setPromptText("Enter partial name");
        name.setPrefColumnCount(15);
        name.setStyle("-fx-font: 22 courier;");
        name.getText();
        GridPane.setConstraints(name, 0, 0);
        grid.getChildren().add(name);


        Button search = new Button("Search");
        search.setStyle("-fx-font: 22 courier;");
        GridPane.setConstraints(search, 1, 0);
        grid.getChildren().add(search);

        Button clear = new Button("Clear");
        clear.setStyle("-fx-font: 22 courier;");
        GridPane.setConstraints(clear, 2, 0);
        grid.getChildren().add(clear);

        final Label label = new Label();
        GridPane.setConstraints(label, 0, 3);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if ((name.getText() != null && !name.getText().trim().isEmpty())) {


                    attendees = WSDAO.getAttendeesByName(name.getText());

                    // Results found?
                    if (attendees != null && attendees.size() > 0) {
                        label.setText(null);
                        border.setCenter(getPagination());
                    } else {
                        border.setCenter(null);
                        label.setText("No results found");
                    }

                } else {
                    label.setText("You have not entered name");
                }
            }
        });

        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                name.clear();
                label.setText(null);
            }
        });


        return grid;
    }


    public void confirmPrint(Attendee attendee) {

        Base64 bs = new Base64();
        ByteArrayInputStream byteImage = new ByteArrayInputStream(bs.decode(attendee.getImageAssoc()));
        Image image = new Image(byteImage, 150, 150, true, true);
        ImageView imageV = new ImageView(image);

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("OOW17 CNC MAtrix");
        alert.setHeaderText("Would you like to print this image: ");
      
        
        Button cancelButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
        cancelButton.setDefaultButton( true );
        
        
        Button okButton = (Button) alert.getDialogPane().lookupButton( ButtonType.OK);
        okButton.setDefaultButton( false );
        
        // Set the icon (must be included in the project).
        alert.setGraphic(imageV);

        Optional<ButtonType> result = alert.showAndWait();
        
        
        if (result.get() == ButtonType.OK) {
            // OK
      
            /************************************************************
             **********  attendee.getCncPath() - gets you CNCPath *******
             ************************************************************/

        } else {
            // CANCEL 
        }

    }
  


}


