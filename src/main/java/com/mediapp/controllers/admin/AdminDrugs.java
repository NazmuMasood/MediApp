package com.mediapp.controllers.admin;

import com.mediapp.controllers.ConnectionClass;
import com.mediapp.controllers.CryptWithMD5;
import com.mediapp.controllers.Drug;
import com.mediapp.controllers.helperClass;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTabPane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class AdminDrugs implements Initializable {
    @FXML
    private AnchorPane root_anchorPane;
    //Add tab
    @FXML
    private Tab add_tab;
    @FXML
    private JFXTextField add_name_field,
                         add_category_field,
                        add_stock_field,
                        add_price_field;
    @FXML
    private Button add_button;

    //Update tab
    @FXML
    private Tab update_tab;
    @FXML
    private JFXTextField update_name_field,
            update_category_field,
            update_stock_field,
            update_price_field;
    @FXML
    private Button update_button;

    //Delete tab
    @FXML
    private Tab delete_tab;
    @FXML
    private JFXTextField delete_name_field,
            delete_category_field,
            delete_stock_field,
            delete_price_field;
    @FXML
    private Button delete_button;

    //Top right Navigation menu
    @FXML
    private Hyperlink homepage_hlink,
                      logout_hlink;

    //TableView
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
    private Button refresh_button;
    @FXML
    private Label message_label;

    ResultSet resultSet; ObservableList<Drug> drugList;
    private Boolean isAddTabSelected=true, isUpdateTabSelected=false, isDeleteTabSelected=false;
    private boolean secondTime = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root_anchorPane.setOnMousePressed(e->root_anchorPane.requestFocus());
        add_tab.setOnSelectionChanged(e->addTabSelected());
        update_tab.setOnSelectionChanged(e->updateTabSelected());
        delete_tab.setOnSelectionChanged(e->deleteTabSelected());
        loadTable();

        homepage_hlink.setOnAction(e->new helperClass().loadPage("/views/admin/homepage.fxml"));
        logout_hlink.setOnAction(e->Homepage.logoutHandler());
        refresh_button.setOnAction(e->loadTable());

        drugs_table.setOnMouseClicked(e->tableClicked());
        add_button.setOnAction(e->add());
        update_button.setOnAction(e->update());
        delete_button.setOnAction(e->delete());

    }

    //add method
    private void add(){
        if (!add_name_field.getText().isEmpty()&&!add_category_field.getText().isEmpty()&&
                !add_stock_field.getText().isEmpty()&&!add_price_field.getText().isEmpty()) {

            //Get the "add details"
            String newName,newCategory,newPrice,newStock;
            newName = add_name_field.getText();
            newCategory = add_category_field.getText();
            newPrice = add_price_field.getText();
            newStock = add_stock_field.getText();

            //Insert into database
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.getConnection();
                PreparedStatement preparedStatement = null;

                String url = "INSERT INTO products(`name`,category,stock,price) values (?,?,?,?)";
                preparedStatement = connection.prepareStatement(url);
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newCategory);
                preparedStatement.setString(3, newStock);
                preparedStatement.setString(4, newPrice);
                preparedStatement.executeUpdate();

                sendMessage("New product successfully added");
                loadTable();
                clearTextFieldsValue("add");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {sendMessage("Please enter every detail");}
    }

    //update method
    private void update(){
        if (!update_name_field.getText().isEmpty()&&!update_category_field.getText().isEmpty()&&
                !update_stock_field.getText().isEmpty()&&!update_price_field.getText().isEmpty()){

            //Get the selected drug's id
            Drug drugSelected;
            int index = drugs_table.getSelectionModel().getSelectedIndex();
            drugSelected = drugList.get(index);
            int id = drugSelected.getId();

            //Get the "update details"
            String newName,newCategory,newPrice,newStock;
            newName = update_name_field.getText();
            newCategory = update_category_field.getText();
            newPrice = update_price_field.getText();
            newStock = update_stock_field.getText();

            //Update into database
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.getConnection();
                PreparedStatement preparedStatement = null;

                String url = "UPDATE products set `name`=?,category=?,stock=?,price=? WHERE product_id = ?";
                preparedStatement = connection.prepareStatement(url);
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newCategory);
                preparedStatement.setString(3, newStock);
                preparedStatement.setString(4, newPrice);
                preparedStatement.setInt(5, id);
                preparedStatement.executeUpdate();

                sendMessage("Selected product successfully updated");
                loadTable();
                clearTextFieldsValue("update");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {sendMessage("Please enter 'update details' first");}
    }
    //Set update_fields editable (true/false)
    private void setUpdateFieldsEditable(Boolean b){
        update_name_field.setEditable(b);
        update_category_field.setEditable(b);
        update_stock_field.setEditable(b);
        update_price_field.setEditable(b);
    }

    //delete method
    private void delete(){
        //selected drug
        if (!drugs_table.getSelectionModel().isEmpty()) {
            Drug drugSelected;
            int index = drugs_table.getSelectionModel().getSelectedIndex();
            drugSelected = drugList.get(index);

            /*System.out.println("Deleting drug: "+drugSelected.toString());
            System.out.println("Deleted drug's id: "+drugSelected.getId());*/

            //deleting drug from database table "products"
            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.getConnection();
                PreparedStatement preparedStatement = null;

                String url = "DELETE FROM products WHERE product_id = ?";
                preparedStatement = connection.prepareStatement(url);
                preparedStatement.setString(1, Integer.toString(drugSelected.getId()));
                preparedStatement.executeUpdate();

                sendMessage("Selected product successfully deleted");
                loadTable();
                clearTextFieldsValue("delete");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Please choose a drug first");
            sendMessage("Please choose a drug first");
        }
    }


    //Method to handle table click
    private void tableClicked(){
        System.out.println("Table clicked"); clearMessageLabel();
        //selected drug
        Drug drugSelected;
        int index = drugs_table.getSelectionModel().getSelectedIndex();
        drugSelected = drugList.get(index);

        if (isUpdateTabSelected) {
            update_name_field.setText(drugSelected.getName());
            update_category_field.setText(drugSelected.getCategory());
            update_stock_field.setText(drugSelected.getStock());
            update_price_field.setText(drugSelected.getPrice());
            setUpdateFieldsEditable(true);
        }
        if (isDeleteTabSelected) {
            delete_name_field.setText(drugSelected.getName());
            delete_category_field.setText(drugSelected.getCategory());
            delete_stock_field.setText(drugSelected.getStock());
            delete_price_field.setText(drugSelected.getPrice());
        }
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

    //add_tab clicked
    private void addTabSelected(){
        if (secondTime) {
            System.out.println("Add tab selected");
            isDeleteTabSelected = false;
            isAddTabSelected = true;
            isUpdateTabSelected = false;

            //clears all selections and field values
            drugs_table.getSelectionModel().clearSelection();
            clearMessageLabel();
            clearTextFieldsValue("update");
            clearTextFieldsValue("delete");
            setUpdateFieldsEditable(false);

            secondTime = false;
        }
        else {
            secondTime = true;
        }
    }

    //update_tab clicked
    private void updateTabSelected(){
        if (secondTime) {
            System.out.println("Update tab selected");
            isDeleteTabSelected = false;
            isAddTabSelected = false;
            isUpdateTabSelected = true;

            //clears all selections and field values
            drugs_table.getSelectionModel().clearSelection();
            clearMessageLabel();
            clearTextFieldsValue("add");
            clearTextFieldsValue("delete");

            secondTime = false;
        }
        else {
            secondTime = true;
        }
    }

    //delete_tab clicked
    private void deleteTabSelected(){
        if (secondTime) {
            System.out.println("Delete tab selected");
            isDeleteTabSelected = true;
            isAddTabSelected = false;
            isUpdateTabSelected = false;

            //clears all selections and field values
            drugs_table.getSelectionModel().clearSelection();
            clearMessageLabel();
            clearTextFieldsValue("add");
            clearTextFieldsValue("update");
            setUpdateFieldsEditable(false);

            secondTime = false;
        }
        else {
            secondTime = true;
        }
    }

    private void sendMessage(String message){
        message_label.setText("*"+message);
    }

    //clear add/delete/update fields value
    private void clearTextFieldsValue(String tabName){
        if (tabName.equals("add")){
            add_name_field.setText("");
            add_category_field.setText("");
            add_stock_field.setText("");
            add_price_field.setText("");
        }
        if (tabName.equals("update")){
            update_name_field.setText("");
            update_category_field.setText("");
            update_stock_field.setText("");
            update_price_field.setText("");
        }
        if (tabName.equals("delete")){
            delete_name_field.setText("");
            delete_category_field.setText("");
            delete_stock_field.setText("");
            delete_price_field.setText("");
        }
    }

    private void clearMessageLabel(){
        message_label.setText("");
    }

}
