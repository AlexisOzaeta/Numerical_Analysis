package data.view;

import data.main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import data.model.Iteration;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

public class FrameBisectionController implements Initializable{
    
    main Main;
    int position = 0;
    String function = "f(x) =";
    double[] constant = new double[1];
    double[] exponent = new double[1];
    String[] sign = new String[1];
    
    
    @FXML
    private void clickBisection(){
        
    }
    
    @FXML
    private void clickAdd(){
        if(verifyMonomial() == true){
            if(position > 0){
                    double[] constantAux = new double[this.constant.length];
                    double[] exponentAux = new double[this.exponent.length];
                    String[] signAux = new String[this.sign.length];

                    System.arraycopy(this.constant, 0, constantAux, 0, this.constant.length);
                    System.arraycopy(this.exponent, 0, exponentAux, 0, this.exponent.length);
                    System.arraycopy(this.sign, 0, signAux, 0, this.sign.length);

                    this.constant = new double[position + 1];
                    this.exponent = new double[position + 1];
                    this.sign = new String[position + 1];

                    System.arraycopy(constantAux, 0, this.constant, 0, constantAux.length);
                    System.arraycopy(exponentAux, 0, this.exponent, 0, exponentAux.length);
                    System.arraycopy(signAux, 0, this.sign, 0, signAux.length);
            }        

            this.constant[position] = Double.parseDouble(txtConstant.getText());
            this.exponent[position] = Double.parseDouble(txtExponent.getText());
            this.sign[position] = cboxSign.getValue();

            function += " "+constant[position]+"x^"+exponent[position]+" "+sign[position];
            lblFunction.setText(function);
            
            if("= 0".equals(this.sign[position]))
                btnAdd.setDisable(true);
            
            position++;
                        
            txtConstant.setText("");
            txtExponent.setText("");
            cboxSign.getSelectionModel().select(-1);
        }else{
            //I'll add message error
        }
    }
    
    @FXML
    private void clickEmpty(){
        txtConstant.setText("");
        txtExponent.setText("");
        cboxSign.getSelectionModel().select(-1);
        txtXa.setText("");
        txtXb.setText("");
        txtError.setText("");
        lblFunction.setText("f(x) =");
        btnAdd.setDisable(false);
        this.constant = new double[1];
        this.exponent = new double[1];
        this.sign = new String[1];
    }
    
    @FXML
    private void clickCalculate(){
        if(verifyInterval() == true){
            double Xa = Double.parseDouble(txtXa.getText()), Xb = Double.parseDouble(txtXb.getText()), Xr = 0, Er = 1, valAnt = 0;
            int n = 1;

            do{   
                if(f(Xa) * f(Xb) < 0){
                    Xr = (Xa + Xb) / 2;
                    if(valAnt != 0)
                        Er = errorRelativo(Xr, valAnt);
                    //txtaData.appendText(String.format("%3d | Xa = %10.6f | Xb = %10.6f | f(Xa) = %10.6f | f(Xb) = %10.6f | Er = %7.4f %% | Xr = %10.6f %n", n++, Xa, Xb, f(Xa), f(Xb), Er, Xr));
                    addIteration(n++, Xa, Xb, f(Xa), f(Xb), Xr, Er);
                    if(f(Xa) * f(Xr) > 0)
                        Xa = Xr;
                    else
                        Xb = Xr;
                    valAnt = Xr;
                }else{
                    System.out.println(System.lineSeparator()+"No existe raíz dentro del intervalo ["+txtXa.getText()+", "+txtXb.getText()+"]");
                    Er = 0;
                }
            }while(Er > Double.parseDouble(txtError.getText()));
            lblResult.setText("La raíz está en X = "+Xr+" con un error relativo de = "+Er);
        }else{}
            //I'll add message error
    }
    
    private double f(double num){
        double result = 0;
        result += (this.constant[0] * Math.pow(num, this.exponent[0]));
        for(int i = 1; i < this.sign.length; i++){
            if("+".equals(this.sign[i - 1]))
                result += (this.constant[i] * Math.pow(num, this.exponent[i]));
            if("-".equals(this.sign[i - 1]))
                result += (this.constant[i] * Math.pow(num, this.exponent[i]));
            if("= 0".equals(this.sign[i - 1]))
                break;
        }
        return result;
    }
    
    private static double errorRelativo(double valNuevo, double valAnt){
        return Math.abs(((valNuevo - valAnt) / valNuevo) * 100);
    }
    
    private void addIteration(int i, double xa, double xb, double fxa, double fxb, double xr, double er){
        Iteration iterations = new Iteration();
        iterations.iteration.set(i);
        iterations.xa.set(xa);
        iterations.xb.set(xb);
        iterations.fxa.set(fxa);
        iterations.fxb.set(fxb);
        iterations.xr.set(xr);
        iterations.error.set(er);
        iteration.add(iterations);
    }
    
    private void fillTable(){
        columnIteration.setCellValueFactory(new PropertyValueFactory<Iteration, Integer>("iteration"));
        columnXa.setCellValueFactory(new PropertyValueFactory<Iteration, Double>("xa"));
        columnXb.setCellValueFactory(new PropertyValueFactory<Iteration, Double>("xb"));
        columnfxa.setCellValueFactory(new PropertyValueFactory<Iteration, Double>("fxa"));
        columnfxb.setCellValueFactory(new PropertyValueFactory<Iteration, Double>("fxb"));
        columnXr.setCellValueFactory(new PropertyValueFactory<Iteration, Double>("xr"));
        columnError.setCellValueFactory(new PropertyValueFactory<Iteration, Double>("error"));
        
        iteration = FXCollections.observableArrayList();
        tblData.setItems(iteration);
    }
    
    private boolean verifyMonomial(){
        boolean state = true;
        if(txtConstant.getText() == "")
            state = false;
        if(txtExponent.getText() == "")
            state = false;
        if(cboxSign.getSelectionModel().getSelectedIndex() == -1)
            state = false;
        return state;
    }
    
    private boolean verifyInterval(){
        boolean state = true;
        if(btnAdd.isDisable() == false)
            state = false;
        if(txtXa.getText() == "")
            state = false;
        if(txtXb.getText() == "")
            state = false;
        if(txtError.getText() == "")
            state = false;
        return state;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFilter();
        fillTable();
    }
    
    private void initFilter(){
//        txtConstant.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>(){
//            @Override
//            public void handle(KeyEvent evt){
//                char c = evt.getCharacter().charAt(0);
//                if(c > '0' || c < '9' || c != '-'){}else evt.consume();
//            }
//        });
//        
//        txtExponent.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>(){
//            @Override
//            public void handle(KeyEvent evt){
//                char c = evt.getCharacter().charAt(0);
//                if(c < '0' || c > '9' || c == '-' || c == '.') evt.consume();
//            }
//        });
//        
//        txtXa.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>(){
//            @Override
//            public void handle(KeyEvent evt){
//                char c = evt.getCharacter().charAt(0);
//                if(c < '0' || c > '9' || c == '-' || c == '.') evt.consume();
//            }
//        });
//        
//        txtXb.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>(){
//            @Override
//            public void handle(KeyEvent evt){
//                char c = evt.getCharacter().charAt(0);
//                if(c < '0' || c > '9' || c == '-' || c == '.') evt.consume();
//            }
//        });
//        
//        txtError.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>(){
//            @Override
//            public void handle(KeyEvent evt){
//                char c = evt.getCharacter().charAt(0);
//                if(c < '0' || c > '9' || c == '-' || c == '.') evt.consume();
//            }
//        });
        
        cboxSign.getItems().add("+");
        cboxSign.getItems().add("-");
        cboxSign.getItems().add("= 0");
    }
    
    public void setMain(main Main){
        this.Main = Main;
    }
    //Declarate buttons
    @FXML private JFXButton btnBisection;
    @FXML private JFXButton btnFalsePosition;
    @FXML private JFXButton btnFixedPoint;
    @FXML private JFXButton btnNewtonRaphson;
    @FXML private JFXButton btnSecant;
    @FXML private JFXButton btnCalculate;
    @FXML private JFXButton btnAdd;
    @FXML private JFXButton btnEmpty;
    //Declarate textfields
    @FXML private JFXTextField txtConstant;
    @FXML private JFXTextField txtExponent;
    @FXML private JFXTextField txtXa;
    @FXML private JFXTextField txtXb;
    @FXML private JFXTextField txtError;
    //Declarate labels
    @FXML private Label lblFunction;
    @FXML private Label lblResult;
    //Declarate combobox
    @FXML private JFXComboBox<String> cboxSign;
    //Declarate table, columns and observablelist
    @FXML private TableView<Iteration> tblData;
    @FXML private TableColumn columnIteration;
    @FXML private TableColumn columnXa;
    @FXML private TableColumn columnXb;
    @FXML private TableColumn columnfxa;
    @FXML private TableColumn columnfxb;
    @FXML private TableColumn columnXr;
    @FXML private TableColumn columnError;
    ObservableList<Iteration> iteration;
}

