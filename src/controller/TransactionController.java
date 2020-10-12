package controller;

import iservice.MoneyTypeManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        // Khi chọn income thì các chi tiêu trong phần loại sẽ khác với outcome
        System.out.println("User choose income transaction");
        transactionGroup.getItems().clear();
        List<MoneyType> incomeTypes = new MoneyTypeManagement().findIncomeTypeList();
        for (int i = 0; i < incomeTypes.size(); i++) {
            transactionGroup.getItems().add(incomeTypes.get(i));
        }

    }

    public void chooseOutcomeTransaction(ActionEvent actionEvent) {
        // Khi chọn outcome thì các chi tiêu trong phần loại sẽ khác với income
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
        setTotalIncomeLabel(String.valueOf(totalIncome));
        setTotalOutcomeLabel(String.valueOf(totalOutcome));
        setRealMoneyLabel();
        addTransactionToTable(transactionList);
        System.out.println(transactionList);
        clearInputFields();
    }

    public Money getInputMoneyObj() {
        Money inputMoneyObj = new Money();
        long inputAmount = Long.parseLong(amountText.getText());
        inputMoneyObj.setAmount(inputAmount);

        if (incomeRadioBtn.isSelected() && transactionGroup.getValue() != null){
            inputMoneyObj.setIncome(true);
            totalIncome+=inputAmount;
        }

        else if (outcomeRadioBtn.isSelected() && transactionGroup.getValue()!=null){
            inputMoneyObj.setIncome(false);
            totalOutcome+=inputAmount;
        }
        MoneyType moneyType = (MoneyType) transactionGroup.getValue();
        inputMoneyObj.setMoneyType(moneyType);
        String inputDescription = transactionDescription.getText();
        inputMoneyObj.setDescription(inputDescription);
        LocalDate inputDate = transactionDate.getValue();
        inputMoneyObj.setDate(inputDate);
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

     //Get total income and outcome and display in label
//    private void setOverViewLabel(Money moneyObj){
//        if(moneyObj.isIncome()){
//            setTotalIncomeLabel();
//            setRealMoneyLabel();
//        }
//        else {
//            setTotalOutcomeLabel();
//            setRealMoneyLabel();
//        }
//    }
    public long getTotalIncome(){
        totalIncome += getInputMoneyObj().getAmount();
        return totalIncome;
    }

//    public long getTotalOutcome(){
//        totalOutcome += getInputMoneyObj().getAmount();
//        return totalOutcome;
//    }
//    public long getRealMoney(){
//        return (totalIncome - totalOutcome);
//    }

    private void setTotalIncomeLabel(String s){
        totalIncomeLabel.setText(String.valueOf(totalIncome));
    }
    private void setTotalOutcomeLabel(String s){
        totalOutcomeLabel.setText(String.valueOf(totalOutcome));
    }
    private void setRealMoneyLabel(){
        realMoneyLabel.setText(String.valueOf(totalIncome - totalOutcome));
    }

    // get selected item in table row
    public void confirmUserAction(){
        Money selectedMoney = allTransactionTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thao tác");
        alert.setContentText("Lựa chọn thao tác bạn muốn thực hiện");

        ButtonType buttonEdit = new ButtonType("Chỉnh sửa");
        ButtonType buttonDelete = new ButtonType("Xóa");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonEdit, buttonDelete, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonEdit){
            editActionSelected(selectedMoney);
        } else if (result.get() == buttonDelete) {
            deleteActionSelected(selectedMoney);
        } else {
            alert.close();
        }
    }

    public void editActionSelected(Money Obj){
        displayValueForEdit(Obj);
        int index = transactionList.indexOf(Obj);
        // code edit
    }
    public void deleteActionSelected(Money Obj){
        System.out.println("Delete");
        transactionList.remove(Obj);
        addTransactionToTable(transactionList);
        if(Obj.isIncome()){
            totalIncome = totalIncome - Obj.getAmount();
            setTotalIncomeLabel(String.valueOf(totalIncome));
            setRealMoneyLabel();
        }
        else{
            totalOutcome = totalOutcome - Obj.getAmount();
            setTotalOutcomeLabel(String.valueOf(totalOutcome));
            setRealMoneyLabel();
        }
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
