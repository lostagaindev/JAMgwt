package com.lostagain.JamGwt;

import java.util.logging.Logger;

import com.lostagain.Jam.GamesInterfaceTextCore;
import com.lostagain.Jam.JAMcore;
import com.lostagain.JamGwt.JargScene.ServerOptions;



/** All the games interface text is stored here, this lets
 * it be changed easily */
public class GamesInterfaceText extends GamesInterfaceTextCore {

	public static Logger Log = Logger.getLogger("JAM.GamesInterfaceText");
	
	public GamesInterfaceText() {
	}
	
	
	
	
	
	/* (should no longer use this as we have file managers elsewhere)
	
	public void LoadTextFromFile(String URL){
		
		//first we grab the file and copy it into an assosative array;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
				URL);
				
				try {
				      requestBuilder.sendRequest("", new RequestCallback() { //$NON-NLS-1$
				        public void onError(Request request, Throwable exception) {
				        	System.out.println("http get failed"); //$NON-NLS-1$
				        }

				        public void onResponseReceived(Request request, Response response) {
				        	//JAM.DebugWindow.addText("text file recieved"+response.getText());
				        	
				        	LoadText(response.getText());
				        	
				        }
				      });
				    } catch (RequestException ex) {
				    	System.out.println("can not connect to games text file"); //$NON-NLS-1$
				    }
				
	}		
	*/

	
	//TODO: make this reference a super.assignStringsToInterface for things now in core
	@Override
	public void assignStringsToInterface() {
		// update controll panel text
		JAM.ControlPanel.updateText();
		
		//load game options
		JAM.loadGamePopup.refreshText();
		
		//save game options
		JAM.saveGamePopup.refreshText();
		


		// update interface language
		JAM.ControllPanelShadows.caption.setText(GamesInterfaceText.MainGame_SaveOrLoadYourGame);
		
		
		JAMcore.PlayersInventoryFrame.setCaptionText(GamesInterfaceText.MainGame_Inventory);
		
		JAM.PlayersNotepadFrame.caption.setText(GamesInterfaceText.MainGame_Charecter_Profiles);
		JAM.SecretPanelFrame.caption.setText(GamesInterfaceText.MainGame_Secrets_Found);

		// //close all title

		JAM.closeallwindows.setTitle(GamesInterfaceText.MainGame_CloseAlWindows);

		JAMcore.EnterAns.get().setText(GamesInterfaceText.MainGame_submit);

		

		// Update login box bits
		Log.info("setting up login box text "
				+ GamesInterfaceText.Login_SignUp);

		ServerOptions.user_login.signup
		.setText(GamesInterfaceText.Login_SignUp);
		ServerOptions.user_login.lab_password
		.setText(GamesInterfaceText.Login_Password);
		ServerOptions.user_login.lab_rememberme
		.setText(GamesInterfaceText.Login_Rememberme);
		ServerOptions.user_login.guestlogin
		.setText(GamesInterfaceText.Login_GuestLogin);
		ServerOptions.user_login.forgotpassword
		.setText(GamesInterfaceText.Login_ForgotPassword);
		ServerOptions.user_login.lab_name
		.setText(GamesInterfaceText.Login_Username);
		ServerOptions.user_login.clearcookies
		.setText(GamesInterfaceText.Login_ClearCookies);
		ServerOptions.user_login.lab_loginplease
		.setText(GamesInterfaceText.Login_PleaseLogin);

		// CluePanel popup
		JAM.CluePopUp.instructions
		.setText(GamesInterfaceText.CluePanel_instructions);
		JAM.CluePopUp.instructions2
		.setText(GamesInterfaceText.CluePanel_instructions2);
		JAM.CluePopUp.chapterheader
		.setText(GamesInterfaceText.CluePanel_chapterheader);
		JAM.CluePopUp.buy.setText(GamesInterfaceText.CluePanel_buy);
		JAM.CluePopUp.cancel
		.setText(GamesInterfaceText.CluePanel_cancel);
	}
	
}
