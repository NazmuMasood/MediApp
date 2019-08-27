package com.mediapp.controllers.patient;

import com.jfoenix.controls.JFXTextField;
import com.mediapp.controllers.ConnectionClass;
import com.mediapp.controllers.Drug;
import com.mediapp.controllers.helperClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class PatientOrder implements Initializable {
    @FXML
    private AnchorPane root_anchorPane;
    @FXML
    private Hyperlink homepage_hlink;
    @FXML
    private Hyperlink logout_hlink;

    @FXML
    private JFXTextField search_field;
    @FXML
    private MenuButton search_type_menu_button;
    @FXML
    private CheckMenuItem name_menu_item,
                            category_menu_item;
    @FXML
    private Button search_button;
    @FXML
    private Button load_all_button;
    @FXML
    private TableView drugs_table;
    @FXML
    private TableColumn<Drug, String> nameColumn;
    @FXML
    private TableColumn<Drug, String> categoryColumn;
    @FXML
    private TableColumn<Drug, String> stockColumn;
    @FXML
    private TableColumn<Drug, String> priceColumn;

    @FXML
    private JFXTextField order_drug_name_field;
    @FXML
    private JFXTextField getOrder_drug_quantity_field;
    @FXML
    private Button order_button;
    @FXML
    private TableView order_table;
    @FXML
    private Label message_label;
    @FXML
    private Label total_label;

    ResultSet resultSet; ObservableList<Drug> drugList; ObservableList<Drug> searchedDrugList; ResultSet searchResultSet;
    Boolean drugListEmpty=false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root_anchorPane.setOnMousePressed(e->root_anchorPane.requestFocus());
        homepage_hlink.setOnAction(e->new helperClass().loadPage("/views/patient/homepage.fxml"));
        logout_hlink.setOnAction(e-> Homepage.logoutHandler());

        load_all_button.setOnAction(e->loadTable());
        loadTable(); root_anchorPane.requestFocus();
        search_button.setOnAction(e->search());
    }

    //search method
    private void search() {
        if (!search_field.getText().isEmpty()){
            if (name_menu_item.isSelected() || category_menu_item.isSelected()) {
                String searchField = search_field.getText();
                //only search by "name" selected
                if (name_menu_item.isSelected() && !category_menu_item.isSelected()) {
                    System.out.println("name selected");
                    String url = "select * from products where name=?";
                    loadTableSearch(url, searchField);
                }

                //only search by "category" selected
                if (category_menu_item.isSelected() && !name_menu_item.isSelected()) {
                    System.out.println("category selected");
                    String url = "select * from products where category=?";
                    loadTableSearch(url, searchField);
                }

                //both search by "name","category" selected
                if (name_menu_item.isSelected() && category_menu_item.isSelected()) {
                    System.out.println("Please select only one 'search by' options");
                    sendMessage("Please select only one 'search by' options");
                }

            }
            else { sendMessage("Please select a 'search by' option");}
        }
        else {sendMessage("Please enter a keyword in 'search for drugs' field");}
    }

    //Load database table after search is clicked
    private void loadTableSearch(String url,String searchField){
        System.out.println("Loading table..");

        if (!drugListEmpty) {
            drugs_table.getItems().removeAll();
            drugListEmpty = true;
        }

        drugs_table.setItems(getDrugsSearch(url, searchField));
        drugs_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private ObservableList<Drug> getDrugsSearch(String url,String searchField){
        ObservableList<Drug> drugs = FXCollections.observableArrayList();

        if(getDrugsFromDBSearch(url,searchField)){
            try {
                while (searchResultSet.next()) {
                    int id; String name, category, stock, price;
                    id = searchResultSet.getInt(1);
                    name = searchResultSet.getString(2);
                    category = searchResultSet.getString(3);
                    stock = searchResultSet.getString(4);
                    price = searchResultSet.getString(5);

                    Drug drug = new Drug(id,name,category,stock,price);
                    drugs.add(drug);

                    System.out.println(drug.toString());
                }

                searchedDrugList = drugs;
                return drugs;
            }
            catch (Exception e){e.printStackTrace();}
        }

        return null;
    }

    private Boolean getDrugsFromDBSearch(String url, String searchField){
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            PreparedStatement preparedStatement=null;

            preparedStatement = connection.prepareStatement(url);
            preparedStatement.setString(1,searchField);
            searchResultSet = preparedStatement.executeQuery();

            if(!searchResultSet.next()){
                return false;
            }else {
                searchResultSet.previous();
                return true;
            }
        }
        catch (Exception e){e.printStackTrace();}
        return false;
    }

    //Load database table
    private void loadTable(){
        System.out.println("Loading table..");
        //Setting the columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        drugs_table.setItems(getDrugs());
        drugs_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private ObservableList<Drug> getDrugs(){
        ObservableList<Drug> drugs = FXCollections.observableArrayList();

        if(getDrugsFromDB()){
            try {
                while (resultSet.next()) {
                    int id; String name, category, stock, price;
                    id = resultSet.getInt(1);
                    name = resultSet.getString(2);
                    category = resultSet.getString(3);
                    stock = resultSet.getString(4);
                    price = resultSet.getString(5);

                    Drug drug = new Drug(id,name,category,stock,price);
                    drugs.add(drug);

                    //System.out.println(drug.toString());
                }

                drugList = drugs;
                return drugs;
            }
            catch (Exception e){e.printStackTrace();}
        }

        return null;
    }

    private Boolean getDrugsFromDB(){
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            PreparedStatement preparedStatement;

            String url = "SELECT * FROM products";
            preparedStatement = connection.prepareStatement(url);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                return false;
            }else {
                resultSet.previous();
                return true;
            }
        }
        catch (Exception e){e.printStackTrace();}
        return false;
    }

    private void sendMessage(String message){
        message_label.setText("*"+message);
    }


}
