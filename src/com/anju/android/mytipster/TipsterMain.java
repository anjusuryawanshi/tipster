package com.anju.android.mytipster;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class TipsterMain extends Activity {

	// Widgets in the app
	private EditText txtAmount;
	private EditText txtPeople;
	private EditText txtTipOther;
	private RadioGroup rdoGroupTips;
	private Button btnCalculate;
	private Button btnReset;
	
	private TextView txtTipAmount;
    private TextView txtTotalToPay;
    private TextView txtTipPerPerson;
 
	
	//For the id of the radio button selected
	private int radioCheckedId = -1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipster_main);
        
        //Access all the widgets by their id
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        txtAmount.requestFocus();
        txtPeople = (EditText) findViewById(R.id.txtPeople);
        txtTipOther = (EditText) findViewById(R.id.txtTipOther);
        //On App load, disable the 'Other tip' percentage text field
        txtTipOther.setEnabled(false);
        
        rdoGroupTips = (RadioGroup) findViewById(R.id.RadioGroupTips);
        
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        //On app load, disable the Calculate button
        btnCalculate.setEnabled(false);
        btnReset = (Button) findViewById(R.id.btnReset);
        
        txtTipAmount = (TextView) findViewById(R.id.txtTipAmount);
        txtTotalToPay = (TextView) findViewById(R.id.txtTotalToPay);
        txtTipPerPerson = (TextView) findViewById(R.id.txtTipPerPerson);
        
        /*
         * Attach a OnCheckedChangeListener to the radio group to
         * monitor radio buttons selected by user
         */
        rdoGroupTips.setOnCheckedChangeListener(new OnCheckedChangeListener(){
        	
        	@Override
        	public void onCheckedChanged(RadioGroup group, int checkedId){
        		if(checkedId == R.id.radioFifteen || checkedId == R.id.radioTwenty){
        			txtTipOther.setEnabled(false);        			
        			btnCalculate.setEnabled(txtAmount.getText().length() > 0 
        					&& txtPeople.getText().length() > 0);
        		}
        		if (checkedId == R.id.radioOther){
        			txtTipOther.setEnabled(true);
        			txtTipOther.requestFocus();
        			btnCalculate.setEnabled(txtAmount.getText().length() > 0
        									&& txtPeople.getText().length() > 0
        									&& txtTipOther.getText().length() > 0);
        		}
        		radioCheckedId = checkedId;
        	}
        	
      });
        
        
        
        OnKeyListener mKeyListener = new OnKeyListener(){

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.txtAmount:
				case R.id.txtPeople:
					
					  btnCalculate.setEnabled(txtAmount.getText().length() > 0
							  && txtPeople.getText().length() > 0);
					  break;
				case R.id.txtTipOther:
					  btnCalculate.setEnabled(txtAmount.getText().length() > 0
							  && txtPeople.getText().length() > 0
							  && txtTipOther.getText().length() > 0);
					  break;
				
				}
				return false;
			}

			
        	
        };
        
        txtAmount.setOnKeyListener(mKeyListener);
        txtPeople.setOnKeyListener(mKeyListener);
        txtTipOther.setOnKeyListener(mKeyListener);
        
        OnClickListener mClickListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId() == R.id.btnCalculate){
					calculate();
				}
				else{
					reset();
				}
			}
        	
        };
        btnCalculate.setOnClickListener(mClickListener);
        btnReset.setOnClickListener(mClickListener);
        
    }

    protected void reset() {
		// TODO Auto-generated method stub
    	txtTipAmount.setText("");
        txtTotalToPay.setText("");
        txtTipPerPerson.setText("");
        txtAmount.setText("");
        txtPeople.setText("");
        txtTipOther.setText("");
        rdoGroupTips.clearCheck();
        rdoGroupTips.check(R.id.radioFifteen);
        // set focus on the first field
        txtAmount.requestFocus();
		
	}

	protected void calculate() {
		// TODO Auto-generated method stub
		double billAmount = Double.parseDouble(txtAmount.getText().toString());
		int totalPeople = Integer.parseInt(txtPeople.getText().toString());
		double percentage = 0.0;
		boolean isError = false;
		
		if(billAmount < 1.0){
			showErrorAlert("Enter a valid Total Amount.", txtAmount.getId());
			isError = true;
		}
		if(totalPeople < 1){
			showErrorAlert("Enter a valid value for No. of People.", txtPeople.getId());
			isError = true;
		}
		/*
	     * If user never changes radio selection, then it means
	     * the default selection of 15% is in effect. But its
	     * safer to verify
	     */
		if(radioCheckedId == -1){
			radioCheckedId = rdoGroupTips.getCheckedRadioButtonId();
		}
		
		if(radioCheckedId == R.id.radioFifteen){
			percentage = 15.00;
		}
		else if (radioCheckedId == R.id.radioTwenty){
			percentage = 20.00;
		}else if(radioCheckedId == R.id.radioOther){
			percentage = Double.parseDouble(txtTipOther.getText().toString());
			if(percentage < 1.00){
				showErrorAlert("Enter a valid Tip Percetage.",txtTipOther.getId());
				isError = true;
		}
		}
		if (!isError){
			Double tipAmount = (billAmount * percentage)/100;
			Double totalToPay = billAmount + tipAmount;
			Double perPersonPays = totalToPay/totalPeople;
			
			txtTipAmount.setText(tipAmount.toString());
			txtTotalToPay.setText(totalToPay.toString());
			txtTipPerPerson.setText(perPersonPays.toString());
		}
		
	}

	private void showErrorAlert(String errorMessage, final int fieldId) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setTitle("Error")
		.setMessage(errorMessage).setNeutralButton("Close", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				findViewById(fieldId).requestFocus();
			}
		}).show();
			
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_tipster_main, menu);
        return true;
    }
}
