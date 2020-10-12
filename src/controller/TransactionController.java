package controller;

import iservice.MoneyTypeManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.MoneyType;
import model.Money;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class TransactionController implements Initializable {
    //  all transaction components
    @FXML
    private TableView<Money> allTransactionTable;
    @FXML
    private TableColumn<Money, LocalDate> transactionDateColumn;
    @FXML
    private TableColumn<Money, Long> transactionAmountColumn;
    @FXML
    private TableColumn<Money, MoneyType> transactionMoneyTypeColumn;
    @FXML
    private TableColumn<Money, String> transactionDetailColumn;
    @FXML
    private Label totalIncomeLabel;
    @FXML
    private Label totalOutcomeLabel;
    @FXML
    private Label realMoneyLabel;

    // edit transaction components
    @FXML
    private Label amountNoticeLabel;
    @FXML
    private Button saveTransactionBtn;
    @FXML
    private TextField amountText;
    @FXML
    private RadioButton incomeRadioBtn;
    @FXML
    private RadioButton outcomeRadioBtn;
    @FXML
    private ChoiceBox transactionGroup;
    @FXML
    private TextField transactionDescription;
    @FXML
    private DatePicker transactionDate;

    public static ObservableList<Money> transactionList = FXCollections.observableArrayList();
    private long totalIncome = 0;
    private long totalOutcome = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allTransactionTable.setPlaceholder(new Label("Bạn chưa có giao dịch nào."));
    }


    // Validate amount text field
    public void validateAmountTextField() {
        amountText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d+$") && !newValue.isEmpty()) {
                saveTransactionBtn.setDisable(true);
                amountNoticeLabel.setVisible(true);
            } else {
                saveTransactionBtn.setDisable(false);
                amountNoticeLabel.setVisible(false);
            }
        });
    }

    // When choose income or outcome, the dropdown list of transaction types are changed
    public void chooseIncomeTransaction(ActionEvent actionEvent) {
        System.out.println("User choose income transaction");
        transactionGroup.getItems().clear();
        List<MoneyType> incomeTypes = new MoneyTypeManagement().findIncomeTypeList();
        for (int i = 0; i < incomeTypes.size(); i++) {
            transactionGroup.getItems().add(incomeTypes.get(i));
        }

    }

    public void chooseOutcomeTransaction(ActionEvent actionEvent) {
        System.out.println("User choose outcome transaction");
        transactionGroup.getItems().clear();
        List<MoneyType> outcomeTypes = new MoneyTypeManagement().findOutcomeTypeList();
        for (int i = 0; i < outcomeTypes.size(); i++) {
            transactionGroup.getItems().add(outcomeTypes.get(i));
        }
    }


    // Save button
    public void saveTransaction(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Money inputMoneyObj = getInputMoneyObj();
        transactionList.add(inputMoneyObj);
        addTransactionToTable(transactionList);
        clearInputFields();
        System.out.println(transactionList);
        getOverviewNum();
        setLabel();
    }

    public Money getInputMoneyObj() {
        Money inputMoneyObj;
        UUID uuid = UUID.randomUUID();
        long inputAmount = Long.parseLong(amountText.getText());
        boolean isIncome = true;
        if (incomeRadioBtn.isSelected() && transactionGroup.getValue() != null){
            isIncome = true;
        }

        else if (outcomeRadioBtn.isSelected() && transactionGroup.getValue()!=null){
            isIncome = false;
        }
        MoneyType moneyType = (MoneyType) transactionGroup.getValue();
        String inputDescription = transactionDescription.getText();
        LocalDate inputDate = transactionDate.getValue();
        inputMoneyObj = new Money(uuid,inputAmount,isIncome,inputDescription,moneyType,inputDate);
        return inputMoneyObj;
    }

    public void clearInputFields() {
        amountText.clear();
        transactionDescription.clear();
        transactionDate.getEditor().clear();
        transactionGroup.getItems().clear();
        outcomeRadioBtn.setSelected(false);
        incomeRadioBtn.setSelected(false);
    }

    // Add transactions to table
    public void addTransactionToTable(ObservableList<Money> list){
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        transactionDetailColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        transactionMoneyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("moneyType"));
        transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        allTransactionTable.setItems(list);
    }
    private void getOverviewNum(){
        long income = 0;
        long outcome = 0;
        for (int i = 0; i < transactionList.size(); i++) {
            if(transactionList.get(i).isIncome())
                income+=transactionList.get(i).getAmount();
            else
               outcome +=transactionList.get(i).getAmount();
        }
        totalIncome = income;
        totalOutcome = outcome;
    }
    private void setLabel(){
        totalIncomeLabel.setText(String.valueOf(totalIncome));
        totalOutcomeLabel.setText(String.valueOf(totalOutcome));
        realMoneyLabel.setText(String.valueOf(totalIncome - totalOutcome));
    }

    // Action when user tap a cell in table
        public void confirmUserAction(){
        Money selectedMoney = allTransactionTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thao tác");
        alert.setHeaderText(null);
        alert.setContentText("Lựa chọn thao tác bạn muốn thực hiện");

        ButtonType buttonEdit = new ButtonType("Chỉnh sửa");
        ButtonType buttonDelete = new ButtonType("Xóa");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonEdit, buttonDelete, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonEdit){
            editActionSelected(selectedMoney);
        } else if (result.get() == buttonDelete) {
            confirmDeleteDialog(selectedMoney);
        } else {
            alert.close();
        }
    }

    public void confirmDeleteDialog(Money money){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thao tác");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn xóa giao dịch này?");

        ButtonType buttonYes = new ButtonType("Xóa");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonYes){
            deleteActionSelected(money);
        } else {
            alert.close();
        }
    }


    public void editActionSelected(Money Obj){
        displayValueForEdit(Obj);
        System.out.println("old" + Obj);
        saveTransactionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editTransaction(Obj);
                addTransactionToTable(transactionList);
                allTransactionTable.refresh();
                System.out.println("list" + transactionList);
                clearInputFields();
                getOverviewNum();
                setLabel();
            }
        });
    }

    public void editTransaction(Money Obj){
        Obj.setAmount(Long.parseLong(amountText.getText()));
        Obj.setMoneyType((MoneyType) transactionGroup.getValue());
        Obj.setDescription(transactionDescription.getText());
        Obj.setDate(transactionDate.getValue());
        boolean isIncome = true;
        if (incomeRadioBtn.isSelected() && transactionGroup.getValue() != null) {
            isIncome = true;
        } else if (outcomeRadioBtn.isSelected() && transactionGroup.getValue() != null) {
            isIncome = false;
        }
        Obj.setIncome(isIncome);
        System.out.println("new obj"+ Obj);
    }

    public void deleteActionSelected(Money Obj){
        System.out.println("Delete");
        transactionList.remove(Obj);
        addTransactionToTable(transactionList);
        getOverviewNum();
        setLabel();
    }

    public void displayValueForEdit(Money Obj){
        amountText.setText(String.valueOf(Obj.getAmount()));
        if(Obj.isIncome())
            incomeRadioBtn.setSelected(true);
        else
            outcomeRadioBtn.setSelected(true);
        transactionGroup.setValue(Obj.getMoneyType());
        transactionDescription.setText(Obj.getDescription());
        transactionDate.setValue(Obj.getDate());
    }


}
