package project.second.cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder; 
import java.security.PrivateKey;
import java.security.PublicKey;
import java.io.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Cipher  extends Application {

	
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Mali šifrator");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Mali šifrator");
        scenetitle.setId("welcome-text");
        scenetitle.setFont(Font.font("Magneto", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        
        Label mode = new Label("Trenutni mod rada:");
        
		ObservableList<String> modes = FXCollections.observableArrayList("šifriranje", "dešifrovanje");
		ComboBox<String> cbTrans = new ComboBox<String>(modes);
		cbTrans.setId("combox");
		cbTrans.setValue("šifriranje");
		
		Label in = new Label("Šifrira se poruka:");
        Label out = new Label("\t\t\t  Šifrovana poruka:");
		cbTrans.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				if(cbTrans.getValue()=="šifriranje") {
					in.setText("Šifrira se poruka:");
					out.setText("\t\t\t  Šifrovana poruka:");
				}
				else {
					in.setText("Dešifruje se poruka");
					out.setText("\t\t\t\tDešifrovana poruka:");
				}
			}
		});
		
		HBox hbMode = new HBox(35);
	    hbMode.setAlignment(Pos.CENTER_LEFT);
	    hbMode.getChildren().addAll(mode, cbTrans);
		grid.add(hbMode, 0, 1);
		
		Label algo = new Label("Odabrani algoritam:    ");
		
        ObservableList<String> algos = FXCollections.observableArrayList("Cezar", "Vižner", "RSA");
		ComboBox<String> cbAlgos = new ComboBox<String>(algos);
		cbAlgos.setValue("Cezar");
		
		final Text desc = new Text("UPUTSTVO :\n"
				+ "Cezarov algoritam je jednostavna, monoalfabetska, supstituciona šifra sa simetričnim (tajnim) ključem.\n"
				+ "Za zadati pomeraj (ključ) svaki karakter u poruci se zamenjuje svojim parnjakom, tj. karakterom u UNICODE\n"
				+ "sistemu pomerenim za vrednost pomeraja. Npr: poruka BABA, sa ključem 3 šifrira se kao EDED.\n");
		desc.setId("uputstvo");
		desc.setFill(Color.WHITE);
		cbAlgos.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				if(cbAlgos.getValue()=="Cezar") {
					//desc.setFill(Color.BLUE);
					desc.setText("UPUTSTVO :\n"
							+ "Cezarov algoritam je jednostavna, monoalfabetska, supstituciona šifra sa simetričnim (tajnim) ključem.\n"
							+ "Za zadati pomeraj (ključ) svaki karakter u poruci se zamenjuje svojim parnjakom, tj. karakterom u UNICODE\n"
							+ "sistemu pomerenim za vrednost pomeraja. Npr: poruka BABA, sa ključem 3 šifrira se kao EDED.\n");
				}
				else if(cbAlgos.getValue()=="Vižner"){
					//desc.setFill(Color.BLUE);
					desc.setText("UPUTSTVO :\n"
							+ "Vižnerov algoritam je polialfabetska, supstituciona šifra sa simetričnim (tajnim) ključem.\n"
							+ "Za razliku od monoalfabetske, ovde svaki karakter ima svoj, u opštem slučaju, različit pomeraj, zadat\n"
							+ "ključem u obliku niza karaktera. Ključ je ovde iste dužine kao poruka koja se kodira, pa je dakle reč\n"
							+ "o tzv. \"Le Chifre indechiffrable\"\n");
				}
				else {
					desc.setText("UPUTSTVO :\n"
							+ "RSA algoritam je šifra sa asimetričnim (javnim) ključem i zasniva se na faktorisanju velikih brojeva.\n"
							+ "Korišćena je implementacija RSA algoritma iz paketa \n"
							+ "Instanca klase KeyPairGenerator generiše par ključeva, javni i tajni, i čuva ih u odgovarajućim fajlovima.\n"
							+ "Instanca klase Chiper vrši enkriptovanje/dekriptovanje poruke koristeći ključeve.\n");
				}
			}
		});
		HBox hbAlgo = new HBox(13);
	    hbAlgo.setAlignment(Pos.CENTER_LEFT);
	    hbAlgo.getChildren().addAll(algo, cbAlgos);
		grid.add(hbAlgo, 0, 2);
		//grid.add(cbAlgos, 1, 2);
		
		Separator sep1 = new Separator();   sep1.setPrefWidth(180);  grid.add(sep1, 0, 3, 2, 1);
        grid.add(desc, 0, 4, 2, 1);
        Separator sep2 = new Separator();   sep2.setPrefWidth(180);  grid.add(sep2, 0, 5, 2, 1);
		
        grid.add(in, 0, 6);
        Label key = new Label("Ključ:");
        grid.add(key, 1, 6);
        
        TextArea tin = new TextArea();  tin.setMaxHeight(100); grid.add(tin, 0, 7, 1, 1);
        TextArea tkey = new TextArea(); tkey.setMaxHeight(100);  grid.add(tkey, 1, 7, 1, 1);
        TextArea tout = new TextArea();  tout.setMaxSize(350, 100);
        
        Button cd = new Button("Transformiši");
        cd.setMinWidth(50);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn.getChildren().add(cd);
		cd.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				StringBuffer in = new StringBuffer(tin.getText());
				StringBuffer out = new StringBuffer(tin.getText());
				if (cbAlgos.getValue() == "Cezar") {
					try {
						int k = Integer.parseInt(tkey.getText());
						if (k > 40)
							throw new NumberFormatException();
						if (cbTrans.getValue() == "šifriranje") {
							for (int i = 0; i < in.length(); i++) {
								out.setCharAt(i, (char) (in.charAt(i) + k));
							}
						} else {
							for (int i = 0; i < in.length(); i++) {
								out.setCharAt(i, (char) (out.charAt(i) - k));
							}
						}
						tout.setText(out.toString());
					} catch (NumberFormatException e) {
						tout.setText("Greska pri unosu: Kljuc mora biti ceo broj.");
					}
				}
				else if (cbAlgos.getValue() == "Vižner") {
					try {
						StringBuffer key = new StringBuffer(tkey.getText());
						if (key.length()!=in.length()) throw new NumberFormatException();
						if (cbTrans.getValue() == "šifriranje") {
							for (int i = 0; i < in.length(); i++) {
								out.setCharAt(i, (char) (in.charAt(i) + key.charAt(i)));
							}
						} else {
							for (int i = 0; i < in.length(); i++) {
								out.setCharAt(i, (char) (out.charAt(i) - key.charAt(i)));
							}
						}
						tout.setText(out.toString());
					} catch (NumberFormatException e) {
						tout.setText("Greska pri unosu:\nDužina ključa mora biti jednaka dužini ulazne poruke.");
					}
				}
				else {	
					try {

					      // Check if the pair of keys are present else generate those.
					      if (!RSA.areKeysPresent()) {
					        // Method generates a pair of keys using the RSA algorithm and stores it
					        // in their respective files
					    	  RSA.generateKey();
					      }
					      
					      final String originalText = tin.getText();
					      ObjectInputStream inputStream = null;
					      
					      
						if (cbTrans.getValue() == "šifriranje") {
							inputStream = new ObjectInputStream(new FileInputStream(RSA.PUBLIC_KEY));
							final PublicKey publicKey = (PublicKey) inputStream.readObject();
							final byte[] cipherText = RSA.encrypt(originalText, publicKey);
							//tout.setText(cipherText.toString());
							tout.setText(RSA.base64Encode(cipherText));
							tkey.setText("Javni i privatni ključ generisani su u fajlovima:\n"
									+ "C:/keys/public.key\n"
									+ "C:/keys/private.key\n"
									+ "respektivno.");
						} else {
							inputStream = new ObjectInputStream(new FileInputStream(RSA.PRIVATE_KEY));
							final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
							final String plainText = RSA.decrypt(RSA.base64Decode(tin.getText()), privateKey);
							tout.setText(plainText);
						}
					      System.out.println("Original Text: " + originalText);
					      //System.out.println("Encrypted Text: " +cipherText.toString());
					      //System.out.println("Decrypted Text: " + plainText);
					      inputStream.close();
					    } catch (Exception e) {
					      e.printStackTrace();
					    }
				}
			}
		});
		grid.add(hbBtn, 0, 8, 2, 1);
		
		HBox hbLout = new HBox(10);
	    hbLout.setAlignment(Pos.BASELINE_CENTER);
	    hbLout.getChildren().add(out);
		grid.add(hbLout, 0, 9);
		HBox hbBto = new HBox(10);
	    hbBto.setAlignment(Pos.CENTER);
	    hbBto.getChildren().add(tout);
	    grid.add(hbBto, 0, 10, 2, 1);
	    
        Scene scene = new Scene(grid, 700, 550);
        primaryStage.setScene(scene);
        
        //scene.getStylesheets().add
        //(Cipher.class.getResource("cipher.css").toExternalForm());
        File f = new File("cipher.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        
        primaryStage.show();
	}

}
