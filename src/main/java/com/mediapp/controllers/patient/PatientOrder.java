package com.mediapp.controllers.patient;

import com.jfoenix.controls.JFXTextField;
import com.mediapp.controllers.ConnectionClass;
import com.mediapp.controllers.Drug;
import com.mediapp.controllers.helperClass;
import com.mysql.jdbc.util.ResultSetUtil;
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
    private JFXTextField order_drug_quantity_field;
    @FXML
    private Button add_to_cart_button;

    @FXML
    private TableView order_table;
    @FXML
    private TableColumn<Drug, String> orderNameColumn;
    @FXML
    private TableColumn<Drug, String> orderCategoryColumn;
    @FXML
    private TableColumn<Drug, String> orderQuantityColumn;
    @FXML
    private TableColumn<Drug, String> orderPriceColumn;

    @FXML
    private Label total_label;
    @FXML
    private Button remove_button;
    @FXML
    private Button order_button;
    @FXML
    private Label message_label;


    ObservableList<Drug> drugList; ResultSet resultSet;
    Boolean drugListEmpty=false;

    ObservableList<Drug> searchedDrugList; ResultSet searchResultSet;

    ObservableList<Drug> desiredDrugList = FXCollections.observableArrayList(); ResultSet inventoryResultSet;
    Boolean desiredDrugListEmpty = true;

    int total = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root_anchorPane.setOnMousePressed(e->root_anchorPane.requestFocus());
        homepage_hlink.setOnAction(e->new helperClass().loadPage("/views/patient/homepage.fxml"));
        logout_hlink.setOnAction(e-> Homepage.logoutHandler());

        load_all_button.setOnAction(e->loadTable());
        loadTable();
        search_button.setOnAction(e->search());

        drugs_table.setOnMouseClicked(e->tableClicked());
        add_to_cart_button.setOnAction(e->addToCart());
        remove_button.setOnAction(e->removeFromCart());
        order_button.setOnAction(e->createOrder());
    }

    //add to cart method
    private void addToCart(){
        if (!order_drug_name_field.getText().isEmpty()&&!order_drug_quantity_field.getText().isEmpty()){
            if (isDrugAvailableInInventory()){
                try {
                    Drug tempDrug = new Drug(inventoryResultSet.getInt(1),inventoryResultSet.getString(2),
                            inventoryResultSet.getString(3),order_drug_quantity_field.getText(),
                            inventoryResultSet.getString(5));

                    System.out.println("Adding to cart: "+tempDrug.toString());
                    desiredDrugList.add(tempDrug);
                    /*System.out.println("Printing desired drug list :");
                    for (int i = 0; i < desiredDrugList.size(); i++) {
                     System.out.println(desiredDrugList.get(i).toString());
                    }*/
                    desiredDrugListEmpty = false;

                    int tempTotal = Integer.parseInt(tempDrug.getPrice())*
                            Integer.parseInt(tempDrug.getStock());
                    total = total + tempTotal;
                    total_label.setText(total+" Taka");

                    loadCartTable();
                }
                catch (Exception e){e.printStackTrace();}
            }
        }
    }

    //load cart
    private void loadCartTable(){
        System.out.println("Loading cart...");
        //Setting the columns
        orderNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        orderCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        orderQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        orderPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        order_table.setItems(getDesiredDrugsList());
        order_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    //get all desired drugs from observableList 'desiredDrugList'
    private ObservableList<Drug> getDesiredDrugsList(){
        if(!desiredDrugListEmpty){
            return desiredDrugList;
        }
        return null;
    }

    //is stock available
    private boolean isDrugAvailableInInventory(){
        try{
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            PreparedStatement preparedStatement = null;

            String url = "select * from products where name=?";
            preparedStatement = connection.prepareStatement(url);
            preparedStatement.setString(1,order_drug_name_field.getText());
            inventoryResultSet = preparedStatement.executeQuery();

            if (inventoryResultSet.next()){
                int stock = Integer.parseInt(inventoryResultSet.getString("stock"));
                int quantityDemanded = Integer.parseInt(order_drug_quantity_field.getText());

                if (stock>=quantityDemanded){
                    if (quantityDemanded==0){
                        sendMessage("Can't order zero amount"); return false;
                    }
                    else {
                        sendMessage("Drug added to cart");
                        return true;
                    }
                }
                else {sendMessage("Desired quantity not available");}
                inventoryResultSet.previous();
            }
            else {sendMessage("Desired drug is not in inventory");}
        }
        catch (Exception e){e.printStackTrace();}
        return false;
    }

    //create order
    private void createOrder(){
        sendMessage("Order successfully created");
        order_table.setItems(null);
        desiredDrugList = FXCollections.observableArrayList();
        desiredDrugListEmpty = true;
        total = 0; total_label.setText("");
    }

    //remove from cart
    private void removeFromCart(){
        //selected drug
        if (!order_table.getSelectionModel().isEmpty()) {
            Drug drugSelected;
            int index = order_table.getSelectionModel().getSelectedIndex();
            drugSelected = desiredDrugList.get(index);

            System.out.println("Removing from cart: "+drugSelected.toString());
            sendMessage("Drug removed from cart");
            desiredDrugList.remove(drugSelected);
            if (desiredDrugList.size()==0){drugListEmpty=true;}
            loadCartTable();
        }
        else {sendMessage("Please choose a drug from cart to remove");}
    }

    //table click method
    private void tableClicked(){
        System.out.println("Table clicked");
        //selected drug
        Drug drugSelected;
        int index = drugs_table.getSelectionModel().getSelectedIndex();
        drugSelected = drugList.get(index);

        order_drug_name_field.setText(drugSelected.getName());
        order_drug_quantity_field.requestFocus(); order_drug_quantity_field.setPromptText("Quantity");
    }

    //search method
    private void search() {
        if (!search_field.getText().isEmpty()){
            if (name_menu_item.isSelected() || category_menu_item.isSelected()) {
                String searchField = search_field.getText().trim();
                //only search by "name" selected
                if (name_menu_item.isSelected() && !category_menu_item.isSelected()) {
                    System.out.println("name selected");
                    String url = "select * from products where name=?";
                    loadTableBySearch(url, searchField);
                }

                //only search by "category" selected
                if (category_menu_item.isSelected() && !name_menu_item.isSelected()) {
                    System.out.println("category selected");
                    String url = "select * from products where category=?";
                    loadTableBySearch(url, searchField);
                }

                //both search by "name","category" selected
                if (name_menu_item.isSelected() && category_menu_item.isSelected()) {
                    System.out.println("Please select only one 'search by' option");
                    sendMessage("Please select only one 'search by' option");
                    /*System.out.println("Both name & category selected");
                    //Below code doesn't produce desired result for some strange reason
                    String url = "select * from products where CONCAT(name, '', category) like ?";
                    loadTableSearch(url, searchField);*/
                }

            }
            else { sendMessage("Please select a 'search by' option");}
        }
        else {sendMessage("Please enter a keyword in 'search for drugs' field");}
    }

    //Load database table after search is clicked
    private void loadTableBySearch(String url,String searchField){
        System.out.println("Loading table..");

        if (!drugListEmpty) {
            drugs_table.getItems().removeAll();
            drugListEmpty = true;
        }

        drugs_table.setItems(getDrugsBySearch(url, searchField));
        drugs_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private ObservableList<Drug> getDrugsBySearch(String url,String searchField){
        ObservableList<Drug> drugs = FXCollections.observableArrayList();

        if(getDrugsFromDBBySearch(url,searchField)){
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

    private Boolean getDrugsFromDBBySearch(String url, String searchField){
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
