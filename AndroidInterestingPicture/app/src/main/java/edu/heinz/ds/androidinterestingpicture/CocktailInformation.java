package edu.heinz.ds.androidinterestingpicture;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

/**
 * This class is used for rending information on android GUI.
 *  @author Jingyi Wu, Keqing Xing
 *  Andrew id: jingyiw2, kxing
 */
public class CocktailInformation extends AppCompatActivity {
    CocktailInformation me = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * The click listener will need a reference to this object, so that upon successfully finding data from web service, it
         * can callback to this object with all required fields. The "this" of the OnClick will be the OnClickListener.
         */
        final CocktailInformation ma = this;
        /* Find the "submit" button, and add a listener to it */
        Button submitButton = findViewById(R.id.submit);
        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
                System.out.println("searchTerm = " + searchTerm);
                GetData gp = new GetData();
                gp.search(searchTerm, me, ma); // Done asynchronously in another thread.  It calls ip.pictureReady() in this thread when complete.
            }
        });
    }
    /*
     * This is called by the GetData object when all information is ready.
     * This allows for passing back all data fields for updating the views.
     */
    @SuppressLint("SetTextI18n")
    public void infoReady(Map drink) {
        TextView searchView = findViewById(R.id.searchTerm);
        TextView displayCocktail = findViewById(R.id.displayCocktail);
        TextView displayName = findViewById(R.id.displayName);
        TextView displayCategory = findViewById(R.id.displayCategory);
        TextView displayInstruction = findViewById(R.id.displayInstruction);
        ImageView pictureView = findViewById(R.id.cocktailPicture);
        TextView displayIngredient1 = findViewById(R.id.displayIngredient1);
        TextView displayIngredient2 = findViewById(R.id.displayIngredient2);
        TextView displayIngredient3 = findViewById(R.id.displayIngredient3);
        TextView displayIngredient4 = findViewById(R.id.displayIngredient4);

        if (drink != null) {
            displayCocktail.setText("Details about "+searchView.getText()+" are as below");
//            System.out.println("==================================");
//            System.out.println(String.valueOf(drink.get("strDrink")));
//            System.out.println(String.valueOf(drink.get("strCategory")));
//            System.out.println(String.valueOf(drink.get("strInstructions")));
            displayName.setText(String.valueOf(drink.get("strDrink")));
            displayCategory.setText(String.valueOf(drink.get("strCategory")));
            displayInstruction.setText(String.valueOf(drink.get("strInstructions")));
            pictureView.setImageBitmap((Bitmap) drink.get("strDrinkThumb"));
            System.out.println("picture");
            pictureView.setVisibility(View.VISIBLE);
            displayIngredient1.setText(drink.get("strIngredient1")+" "+String.valueOf(drink.get("strMeasure1")));
            displayIngredient2.setText(drink.get("strIngredient2")+" "+String.valueOf(drink.get("strMeasure2")));
            displayIngredient3.setText(drink.get("strIngredient3")+" "+String.valueOf(drink.get("strMeasure3")));
            if (drink.get("strIngredient4")!=null && drink.get("strMeasure4")!=null){
                displayIngredient4.setText(drink.get("strIngredient4")+" "+String.valueOf(drink.get("strMeasure4")));
            } else {
                displayIngredient4.setText("");
                displayIngredient4.setVisibility(View.INVISIBLE);
            }
        } else {
            displayCocktail.setText("Sorry, I could not find details about "+searchView.getText());
            displayName.setText("");
            displayName.setVisibility(View.INVISIBLE);
            displayCategory.setText("");
            displayCategory.setVisibility(View.INVISIBLE);
            displayInstruction.setText("");
            displayInstruction.setVisibility(View.INVISIBLE);
//            pictureView.setImageResource(R.mipmap.ic_launcher);
            System.out.println("No picture");
            pictureView.setVisibility(View.INVISIBLE);
            displayIngredient1.setText("");
            displayIngredient1.setVisibility(View.INVISIBLE);
            displayIngredient2.setText("");
            displayIngredient2.setVisibility(View.INVISIBLE);
            displayIngredient3.setText("");
            displayIngredient3.setVisibility(View.INVISIBLE);
            displayIngredient4.setText("");
            displayIngredient4.setVisibility(View.INVISIBLE);
        }
        searchView.setText("");
        pictureView.invalidate();
    }
}
