package com.lostagain.JamGwt.GwtJamImplementations;

import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.Interfaces.JamCuypersCodeSpecificFunctions;
import com.lostagain.JamGwt.JAM;

public class GwtCuypersCodeSpecificFunctions implements JamCuypersCodeSpecificFunctions {

	public  void setSoldierURL(String iconloc, int iconframes) {
		
		JAM.solider.setURL("GameIcons/"
				+ JAMcore.iconsizestring + "/" + iconloc,
				iconframes);
	}

	public  void setStatueHeadUrl(String iconlocscl, int iconframes) {
		
		JAM.StatueHead.setURL("GameIcons/"
				+ JAMcore.iconsizestring + "/" + iconlocscl,
				iconframes);
		
	}

	@Override
	public void addSecret(String secretname, String secreturl) {
		// reveal button;
		JAM.SecretsPopupPanelButton.setVisible(true);

		JAM.SecretPanel.addLinkItem(secretname, secreturl);

	
	}

	@Override
	public void clearSecrets() {

		JAM.SecretPanel.clear();
	}

	
	
	
}
