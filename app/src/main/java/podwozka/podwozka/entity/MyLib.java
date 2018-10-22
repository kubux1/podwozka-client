package podwozka.podwozka.entity;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyLib {

    public ArrayList<EditText> getAllEditTextInActivity(LinearLayout layout) {
        ArrayList<EditText> EditTextList = new ArrayList<>();

        for (int i = 0; i < layout.getChildCount(); i++)
            if (layout.getChildAt(i) instanceof EditText)
                EditTextList.add((EditText) layout.getChildAt(i));
        return  EditTextList;
    }

    public Map<String, String> EditTextValuesToMap(ArrayList<EditText> EditTextList, boolean insertEmptyFields) {
        Map<String, String> car = new HashMap<>();
        for(int i=0;i < EditTextList.size();i++){
           String text = EditTextList.get(i).getText().toString();
           if(insertEmptyFields == false & text.equals("")){
               car.put("EmptyFieldName", EditTextList.get(i).getHint().toString());
               break;
            } else {
               car.put(EditTextList.get(i).getTag().toString(), text);
           }
        }
        return car;
    }
}
